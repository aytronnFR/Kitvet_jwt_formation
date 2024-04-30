package com.aytronn.kibvet.config.security;

import com.aytronn.kibvet.config.security.JwtTokenFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.aytronn.kibvet.enums.Permission.ADMIN_CREATE;
import static com.aytronn.kibvet.enums.Permission.ADMIN_DELETE;
import static com.aytronn.kibvet.enums.Permission.ADMIN_READ;
import static com.aytronn.kibvet.enums.Permission.ADMIN_UPDATE;
import static com.aytronn.kibvet.enums.Permission.EDITOR_CREATE;
import static com.aytronn.kibvet.enums.Permission.EDITOR_DELETE;
import static com.aytronn.kibvet.enums.Permission.EDITOR_READ;
import static com.aytronn.kibvet.enums.Permission.EDITOR_UPDATE;
import static com.aytronn.kibvet.enums.Role.ADMIN;
import static com.aytronn.kibvet.enums.Role.EDITOR;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@Slf4j
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, AuthenticationProvider authenticationProvider, LogoutHandler logoutHandler) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    @Order(1)
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/api/v1/editor/**").hasAnyRole(ADMIN.name(), EDITOR.name())
                            .requestMatchers(GET, "/api/v1/editor/**").hasAnyAuthority(ADMIN_READ.name(), EDITOR_READ.name())
                            .requestMatchers(POST, "/api/v1/editor/**").hasAnyAuthority(ADMIN_CREATE.name(), EDITOR_CREATE.name())
                            .requestMatchers(PUT, "/api/v1/editor/**").hasAnyAuthority(ADMIN_UPDATE.name(), EDITOR_UPDATE.name())
                            .requestMatchers(DELETE, "/api/v1/editor/**").hasAnyAuthority(ADMIN_DELETE.name(), EDITOR_DELETE.name())
                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}
