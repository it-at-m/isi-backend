/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.configuration;

import de.muenchen.isi.security.CustomJwtAuthenticationConverter;
import de.muenchen.isi.security.UserInfoAuthoritiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * The central class for configuration of all security aspects.
 */
@Configuration
@Profile("!no-security")
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityEnabledBackendConfiguration {

    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${spring.security.oauth2.resource.user-info-uri}")
    private String userInfoUri;

    /**
     * Absichern der Rest-Endpunkte mit Definition der Ausnahmen.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(request ->
                request
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/**"))
                    .authenticated()
                    .requestMatchers(
                        // allow access to /v3/api-docs
                        AntPathRequestMatcher.antMatcher("/v3/api-docs"),
                        // allow access to /actuator/info
                        AntPathRequestMatcher.antMatcher("/actuator/info"),
                        // allow access to /actuator/health for OpenShift Health Check
                        AntPathRequestMatcher.antMatcher("/actuator/health"),
                        // allow access to /actuator/health/liveness for OpenShift Liveness Check
                        AntPathRequestMatcher.antMatcher("/actuator/health/liveness"),
                        // allow access to /actuator/health/readiness for OpenShift Readiness Check
                        AntPathRequestMatcher.antMatcher("/actuator/health/readiness"),
                        // allow access to /actuator/metrics for Prometheus monitoring in OpenShift
                        AntPathRequestMatcher.antMatcher("/actuator/metrics")
                    )
                    .permitAll()
            )
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwt ->
                    // Verwenden eines CustomConverters um die Rechte vom UserInfoEndpunkt zu extrahieren.
                    jwt.jwtAuthenticationConverter(
                        new CustomJwtAuthenticationConverter(
                            new UserInfoAuthoritiesService(userInfoUri, restTemplateBuilder.build())
                        )
                    )
                )
            );
        return http.build();
    }
}
