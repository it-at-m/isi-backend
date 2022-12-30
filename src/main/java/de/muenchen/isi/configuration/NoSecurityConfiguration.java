/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;


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

    @Bean
    public RestTemplate restTemplate() {
        /**
         * Add {@link HttpComponentsClientHttpRequestFactory} to rest template to allow
         * {@link org.springframework.http.HttpMethod.PATCH} requests.
         */
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

}
