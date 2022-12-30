package de.muenchen.isi.security;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
@Profile("!no-security")
@RequiredArgsConstructor
public class ClientCredentialsRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    /**
     * Die Client Registration ID der folgenden Property:
     * spring.security.oauth2.client.registration.server-to-server.xxx
     */
    private static final String CLIENT_REGISTRATION_ID = "server-to-server";

    private static final String PRINCIPAL_NAME = "isi-backend";

    private final AuthorizedClientServiceOAuth2AuthorizedClientManager auth2AuthorizedClientManager;

    /**
     * Der Interceptor fügt an die Header des {@link HttpRequest} den Header {@link HttpHeaders#AUTHORIZATION}
     * mit dem mittels Access-Token.
     * Der Access-Token wurde mittels des Client Credentials Grant Flow vom sso-Dienst geholt.
     */
    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException {
        // Erstellen eines Request für den Server-to-Server Client Credentials Code Grant Flow.
        final OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(CLIENT_REGISTRATION_ID)
                .principal(PRINCIPAL_NAME)
                .build();

        // Request zum Holen des Access-Tokens vom sso.
        final OAuth2AuthorizedClient authorizedClient = this.auth2AuthorizedClientManager.authorize(authorizeRequest);
        final OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

        // Anfügen des Authorization-Headers mit Bearer-Token an den RestTemplate-Request
        final var authorizationHeaderValue = "Bearer " + accessToken.getTokenValue();
        request.getHeaders().add(HttpHeaders.AUTHORIZATION, authorizationHeaderValue);

        return execution.execute(request, body);
    }

}
