/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.configuration;

import de.muenchen.isi.security.CustomJwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The central class for configuration of all security aspects.
 */
@Configuration
@Profile("!no-security")
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    /**
     * Absichern der Rest-Endpunkte mit Definition der Ausnahmen.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(request ->
                request
                    .requestMatchers("/**")
                    .authenticated()
                    .requestMatchers(
                        // allow access to /v3/api-docs
                        "/v3/api-docs",
                        // allow access to /actuator/info
                        "/actuator/info",
                        // allow access to /actuator/health for OpenShift Health Check
                        "/actuator/health",
                        // allow access to /actuator/health/liveness for OpenShift Liveness Check
                        "/actuator/health/liveness",
                        // allow access to /actuator/health/readiness for OpenShift Readiness Check
                        "/actuator/health/readiness",
                        // allow access to /actuator/metrics for Prometheus monitoring in OpenShift
                        "/actuator/metrics"
                    )
                    .permitAll()
            )
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer.jwt(jwt ->
                    // Verwenden eines CustomConverters um die Rechte vom UserInfoEndpunkt zu extrahieren.
                    jwt.jwtAuthenticationConverter(this.customJwtAuthenticationConverter)
                )
            );
        return http.build();
    }

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager(
        final ClientRegistrationRepository clientRegistrationRepository,
        final OAuth2AuthorizedClientService authorizedClientService
    ) {
        final OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
            .builder()
            .clientCredentials()
            .build();

        final AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
            new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService
            );
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
