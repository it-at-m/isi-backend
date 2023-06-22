/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application class for starting the microservice.
 */
@Configuration
@ComponentScan(basePackages = { "org.springframework.data.jpa.convert.threeten", "de.muenchen.isi" })
@EntityScan(basePackages = { "org.springframework.data.jpa.convert.threeten", "de.muenchen.isi" })
@EnableJpaRepositories(basePackages = { "de.muenchen.isi" })
@EnableAutoConfiguration
public class IsiBackendApplication {

    public static void main(final String[] args) {
        SpringApplication.run(IsiBackendApplication.class, args);
    }
}
