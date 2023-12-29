package de.muenchen.isi.configuration;

import lombok.RequiredArgsConstructor;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.ReportingApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ReportingApiConfiguration {

    /**
     * Erstellt eine {@link ReportingApi} Bean für Requests an die Reporting-EAI.
     *
     * @param url URL für den Aufruf der Reporting-EAI.
     */
    @Bean
    public ReportingApi muenchenAdressenApi(@Value("${isi.reporting.url:}") final String url) {
        return new ReportingApi(this.reportingApiClient(url));
    }

    private ApiClient reportingApiClient(final String url) {
        final var webClient = WebClient.builder().build();
        final var apiClient = new ApiClient(webClient);
        apiClient.setBasePath(url);
        return apiClient;
    }
}
