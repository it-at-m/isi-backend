/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@Profile("no-security")
@EnableWebSecurity
public class NoSecurityConfiguration {

    /**
     * Deaktivierung der Security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable()
                .and()
                .antMatcher("/**").authorizeRequests()
                .anyRequest().permitAll()
                .and().csrf().disable();
        return http.build();
    }

}
