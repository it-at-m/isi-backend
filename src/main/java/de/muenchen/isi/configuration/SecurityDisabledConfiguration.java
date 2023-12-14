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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Profile("no-security")
@EnableWebSecurity
public class SecurityDisabledConfiguration {

    /**
     * Deaktivierung der Security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            //.authorizeHttpRequests(request -> request.requestMatchers("/**").permitAll().anyRequest().permitAll())
            .authorizeHttpRequests(request ->
                request.requestMatchers(AntPathRequestMatcher.antMatcher("/**")).permitAll().anyRequest().permitAll()
            )
            .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
