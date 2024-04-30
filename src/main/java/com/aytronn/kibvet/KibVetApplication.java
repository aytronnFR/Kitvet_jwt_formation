package com.aytronn.kibvet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.time.Instant;

@SpringBootApplication
@EnableMethodSecurity
@Slf4j
public class KibVetApplication {

    public static void main(String[] args) {
        SpringApplication.run(KibVetApplication.class, args);
    }

    /**
     * Application ready event.
     *
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        log.info("Application started {}", Instant.now());
    }
}
