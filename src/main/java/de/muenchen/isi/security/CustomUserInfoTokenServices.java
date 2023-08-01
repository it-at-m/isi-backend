package de.muenchen.isi.security;

import de.muenchen.isi.configuration.CachingConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

/**
 * This class extends the class {@link UserInfoTokenServices} by the caching functionality for the method
 * {@link CustomUserInfoTokenServices#loadAuthentication(String)}.
 * <p>
 * The configuration for the cache is done in class {@link CachingConfiguration}.
 * <p>
 * If the annotation {@link EnableCaching} is not present within the application,
 * the caching functionality is not available. The above mentioned annotation is defined
 * in class {@link CachingConfiguration}.
 */
@Slf4j
@Component
@Profile("!no-security")
public class CustomUserInfoTokenServices extends UserInfoTokenServices {

    public static final String NAME_AUTHENTICATION_CACHE = "authentication_cache";

    public CustomUserInfoTokenServices(
        @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") final String issuerUri,
        @Value("${spring.security.oauth2.client.registration.server-to-server.client-id}") final String clientId
    ) {
        super(issuerUri + "/protocol/openid-connect/userinfo", clientId);
    }

    /**
     * The method is caching the authentication using the access token given in the parameter as a key.
     *
     * @param accessToken The access token.
     * @return The {@link OAuth2Authentication} according the access token given in the parameter.
     */
    @Override
    @Cacheable(NAME_AUTHENTICATION_CACHE)
    public OAuth2Authentication loadAuthentication(final String accessToken) {
        log.debug("Loading and caching OAuth2Authentication");
        return super.loadAuthentication(accessToken);
    }
}
