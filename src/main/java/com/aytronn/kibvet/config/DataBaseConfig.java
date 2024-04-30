package com.aytronn.kibvet.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.cfg.AvailableSettings.JTA_PLATFORM;

@Configuration
@EnableConfigurationProperties({HibernateProperties.class, JpaProperties.class})
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class DataBaseConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource dataSource() {
        final DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(this.dataSourceProperties.getUrl());
        dataSourceBuilder.username(this.dataSourceProperties.getUsername());
        dataSourceBuilder.password(this.dataSourceProperties.getPassword());
        dataSourceBuilder.driverClassName(this.dataSourceProperties.getDriverClassName());
        return dataSourceBuilder.build();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            PersistenceManagedTypes persistenceManagedTypes,
            HibernateProperties hibernateProperties,
            JpaProperties properties,
            ConfigurableListableBeanFactory beanFactory,
            ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy,
            ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy,
            ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        final List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizersList =
                determineHibernatePropertiesCustomizers(
                        physicalNamingStrategy.getIfAvailable(),
                        implicitNamingStrategy.getIfAvailable(),
                        beanFactory,
                        hibernatePropertiesCustomizers.orderedStream().toList());
        final Map<String, Object> vendorProperties =
                new LinkedHashMap<>(
                        hibernateProperties.determineHibernateProperties(
                                properties.getProperties(),
                                new HibernateSettings()
                                        .hibernatePropertiesCustomizers(
                                                hibernatePropertiesCustomizersList)));
        vendorProperties.put(JTA_PLATFORM, new NoJtaPlatform());

        return builder.dataSource(dataSource())
                .managedTypes(persistenceManagedTypes)
                .properties(vendorProperties)
                .build();
    }

    private List<HibernatePropertiesCustomizer> determineHibernatePropertiesCustomizers(
            PhysicalNamingStrategy physicalNamingStrategy,
            ImplicitNamingStrategy implicitNamingStrategy,
            ConfigurableListableBeanFactory beanFactory,
            List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        final List<HibernatePropertiesCustomizer> customizers = new ArrayList<>();
        if (ClassUtils.isPresent(
                "org.hibernate.resource.beans.container.spi.BeanContainer",
                getClass().getClassLoader())) {
            customizers.add(
                    properties ->
                            properties.put(
                                    AvailableSettings.BEAN_CONTAINER,
                                    new SpringBeanContainer(beanFactory)));
        }
        if (physicalNamingStrategy != null || implicitNamingStrategy != null) {
            customizers.add(
                    new NamingStrategiesHibernatePropertiesCustomizer(
                            physicalNamingStrategy, implicitNamingStrategy));
        }
        customizers.addAll(hibernatePropertiesCustomizers);
        return customizers;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean({
            LocalContainerEntityManagerFactoryBean.class,
            EntityManagerFactory.class
    })
    static class PersistenceManagedTypesConfiguration {

        @Bean
        @Primary
        @ConditionalOnMissingBean
        static PersistenceManagedTypes persistenceManagedTypes(
                BeanFactory beanFactory, ResourceLoader resourceLoader) {
            final String[] packagesToScan = getPackagesToScan(beanFactory);
            return new PersistenceManagedTypesScanner(resourceLoader).scan(packagesToScan);
        }

        private static String[] getPackagesToScan(BeanFactory beanFactory) {
            List<String> packages = EntityScanPackages.get(beanFactory).getPackageNames();
            if (packages.isEmpty() && AutoConfigurationPackages.has(beanFactory)) {
                packages = AutoConfigurationPackages.get(beanFactory);
            }
            return StringUtils.toStringArray(packages);
        }
    }

    private record NamingStrategiesHibernatePropertiesCustomizer(
            PhysicalNamingStrategy physicalNamingStrategy,
            ImplicitNamingStrategy implicitNamingStrategy)
            implements HibernatePropertiesCustomizer {
        @Override
        public void customize(Map<String, Object> hibernateProperties) {
            if (this.physicalNamingStrategy != null) {
                hibernateProperties.put(
                        "hibernate.physical_naming_strategy", this.physicalNamingStrategy);
            }
            if (this.implicitNamingStrategy != null) {
                hibernateProperties.put(
                        "hibernate.implicit_naming_strategy", this.implicitNamingStrategy);
            }
        }
    }
}
