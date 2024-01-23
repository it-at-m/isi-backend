package de.muenchen.isi.configuration;

import de.muenchen.isi.reporting.client.ApiClient;
import de.muenchen.isi.reporting.client.api.AbfrageReportingEaiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServerToServerApiConfiguration {

    @Value("${isi.reporting.url}")
    private String isiReportingUrl;

    @Bean
    @Profile("!no-security")
    public AbfrageReportingEaiApi securedAbfrageReportingEaiApi(
        final ClientRegistrationRepository clientRegistrationRepository,
        final OAuth2AuthorizedClientService authorizedClientService
    ) {
        final var webClient = webClient(clientRegistrationRepository, authorizedClientService);
        final var apiClient = new ApiClient(webClient);
        apiClient.setBasePath(isiReportingUrl);
        return new AbfrageReportingEaiApi(apiClient);
    }

    @Bean
    @Profile("no-security")
    public AbfrageReportingEaiApi abfrageReportingEaiApi() {
        final var webClient = WebClient.builder().build();
        final var apiClient = new ApiClient(webClient);
        apiClient.setBasePath(isiReportingUrl);
        return new AbfrageReportingEaiApi(apiClient);
    }

    private WebClient webClient(
        final ClientRegistrationRepository clientRegistrationRepository,
        final OAuth2AuthorizedClientService authorizedClientService
    ) {
        final var oauth = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
            new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService
            )
        );
        oauth.setDefaultClientRegistrationId("server-to-server");
        return WebClient.builder().apply(oauth.oauth2Configuration()).build();
    }
}
