/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.configuration;

import de.muenchen.isi.security.CustomJwtAuthenticationConverter;
import de.muenchen.isi.security.UserInfoDataService;
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
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

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
        var pathMatcher = PathPatternRequestMatcher.withDefaults();

        http
            .authorizeHttpRequests(request ->
                request
                    .requestMatchers(
                        // allow access to /v3/api-docs/**
                        pathMatcher.matcher("/v3/api-docs/**"),
                        // allow access to /swagger-resources/**
                        pathMatcher.matcher("/swagger-resources/**"),
                        // allow access to /swagger-ui
                        pathMatcher.matcher("/swagger-ui/**"),
                        // allow access to /swagger-ui.html
                        pathMatcher.matcher("/swagger-ui.html"),
                        // allow access to /actuator/info
                        pathMatcher.matcher("/actuator/info"),
                        // allow access to /actuator/health for OpenShift Health Check
                        pathMatcher.matcher("/actuator/health"),
                        // allow access to /actuator/health/liveness for OpenShift Liveness Check
                        pathMatcher.matcher("/actuator/health/liveness"),
                        // allow access to /actuator/health/readiness for OpenShift Readiness Check
                        pathMatcher.matcher("/actuator/health/readiness"),
                        // allow access to /actuator/metrics for Prometheus monitoring in OpenShift
                        pathMatcher.matcher("/actuator/metrics")
                    )
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            )
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwt ->
                    // Verwenden eines CustomConverters um die Rechte vom UserInfoEndpunkt zu extrahieren.
                    jwt.jwtAuthenticationConverter(
                        new CustomJwtAuthenticationConverter(
                            new UserInfoDataService(userInfoUri, restTemplateBuilder.build())
                        )
                    )
                )
            );
        return http.build();
    }
}
