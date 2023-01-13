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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    /**
     * Absichern der Rest-Endpunkte mit Definition der Ausnahmen.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .antMatcher("/**").authorizeRequests()
                // allow access to /actuator/info
                .antMatchers("/actuator/info").permitAll()
                // allow access to /actuator/health for OpenShift Health Check
                .antMatchers("/actuator/health").permitAll()
                // allow access to /actuator/health/liveness for OpenShift Liveness Check
                .antMatchers("/actuator/health/liveness").permitAll()
                // allow access to /actuator/health/readiness for OpenShift Readiness Check
                .antMatchers("/actuator/health/readiness").permitAll()
                // allow access to /actuator/metrics for Prometheus monitoring in OpenShift
                .antMatchers("/actuator/metrics").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                // Verwenden eines CustomConverters um die Rechte vom UserInfoEndpunkt zu extrahieren.
                .jwtAuthenticationConverter(this.customJwtAuthenticationConverter);
        return http.build();
    }

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager(final ClientRegistrationRepository clientRegistrationRepository,
                                                                                                  final OAuth2AuthorizedClientService authorizedClientService) {

        final OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
                .builder()
                .clientCredentials()
                .build();

        final AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService
        );
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

}
