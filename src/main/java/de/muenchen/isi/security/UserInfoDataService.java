/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.isi.security;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import de.muenchen.isi.configuration.CachingConfiguration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestTemplate;

/**
 * Service, der einen OIDC /userinfo Endpoint aufruft (mit JWT Bearer Auth) und dort die enthaltenen
 * "Authorities" extrahiert.
 */
@Slf4j
public class UserInfoDataService {

    @Data
    public static final class UserInfoData {

        private Map<String, Object> claims;

        private Collection<SimpleGrantedAuthority> authorities;
    }

    public static final String NAME_AUTHENTICATION_CACHE = "authentication_cache";

    public static final String CLAIM_AUTHORITIES = "authorities";

    public static final String CLAIM_SURNAME = "surname";

    public static final String CLAIM_GIVENNAME = "givenname";

    public static final String CLAIM_DEPARTMENT = "department";

    public static final String CLAIM_EMAIL = "email";

    public static final String CLAIM_USERNAME = "username";

    private final String userInfoUri;
    private final RestTemplate restTemplate;
    private final Cache cache;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param userInfoUri userinfo Endpoint URI
     * @param restTemplate ein {@link RestTemplate}
     */
    public UserInfoDataService(final String userInfoUri, final RestTemplate restTemplate) {
        this.userInfoUri = userInfoUri;
        this.restTemplate = restTemplate;
        this.cache =
            new CaffeineCache(
                NAME_AUTHENTICATION_CACHE,
                Caffeine
                    .newBuilder()
                    .expireAfterWrite(
                        CachingConfiguration.AUTHENTICATION_CACHE_EXPIRATION_TIME_SECONDS,
                        TimeUnit.SECONDS
                    )
                    .ticker(Ticker.systemTicker())
                    .build()
            );
    }

    /**
     * Ruft den /userinfo Endpoint und extrahiert {@link GrantedAuthority}s aus dem "authorities"
     * Claim.
     *
     * @param jwt der JWT
     * @return die {@link GrantedAuthority}s gem. Claim "authorities" des /userinfo Endpoints
     */
    public UserInfoData loadUserInfoData(final Jwt jwt) {
        final var valueWrapper = this.cache.get(jwt.getSubject());
        if (valueWrapper != null) {
            // value present in cache
            @SuppressWarnings("unchecked")
            final var userInfoData = (UserInfoData) valueWrapper.get();
            log.debug("Resolved UserInfoData (from cache): {}", userInfoData);
            return userInfoData;
        }

        final var userInfoData = new UserInfoData();
        try {
            final Map<String, Object> userInfoEndpointData = this.getDataFromUserInfoEndpoint(jwt);

            final var authorities = getAuthoritiesFromUserInfoEndpointData(userInfoEndpointData);
            userInfoData.setAuthorities(authorities);
            final var claims = getClaimsFromUserInfoEndpointData(userInfoEndpointData);
            userInfoData.setClaims(claims);

            log.debug("Resolved UserInfoData (from /userinfo Endpoint): {}", userInfoData);

            // store to Cache
            this.cache.put(jwt.getSubject(), userInfoData);
        } catch (Exception exception) {
            log.error(
                String.format(
                    "Could not fetch user details from %s - user is granted NO authorities",
                    this.userInfoUri
                ),
                exception
            );
        }

        return userInfoData;
    }

    protected List<SimpleGrantedAuthority> getAuthoritiesFromUserInfoEndpointData(
        final Map<String, Object> userInfoEndpointData
    ) {
        final var authorities = new ArrayList<SimpleGrantedAuthority>();
        if (userInfoEndpointData.containsKey(CLAIM_AUTHORITIES)) {
            authorities.addAll(asAuthorities(userInfoEndpointData.get(CLAIM_AUTHORITIES)));
        }
        return authorities;
    }

    protected List<SimpleGrantedAuthority> asAuthorities(final Object object) {
        final var authorities = new ArrayList<SimpleGrantedAuthority>();
        if (object instanceof Collection<?>) {
            authorities.addAll(
                ((Collection<?>) object).stream().map(Object::toString).map(SimpleGrantedAuthority::new).toList()
            );
        }
        return authorities;
    }

    protected Map<String, Object> getClaimsFromUserInfoEndpointData(final Map<String, Object> userInfoEndpointData) {
        final var claims = new HashMap<String, Object>();

        final var surname = userInfoEndpointData.get(CLAIM_SURNAME);
        Optional.ofNullable(surname).ifPresent(claimValue -> claims.put(CLAIM_SURNAME, claimValue));
        final var givenname = userInfoEndpointData.get(CLAIM_GIVENNAME);
        Optional.ofNullable(givenname).ifPresent(claimValue -> claims.put(CLAIM_GIVENNAME, claimValue));
        final var department = userInfoEndpointData.get(CLAIM_DEPARTMENT);
        Optional.ofNullable(department).ifPresent(claimValue -> claims.put(CLAIM_DEPARTMENT, claimValue));
        final var email = userInfoEndpointData.get(CLAIM_EMAIL);
        Optional.ofNullable(email).ifPresent(claimValue -> claims.put(CLAIM_EMAIL, claimValue));
        final var username = userInfoEndpointData.get(CLAIM_USERNAME);
        Optional.ofNullable(username).ifPresent(claimValue -> claims.put(CLAIM_USERNAME, claimValue));

        return claims;
    }

    protected Map<String, Object> getDataFromUserInfoEndpoint(final Jwt jwt) {
        log.debug("Fetching user-info for token subject: {}", jwt.getSubject());
        final var headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue());
        final var entity = new HttpEntity<String>(headers);

        @SuppressWarnings("unchecked")
        final Map<String, Object> userInfoEndpointData = restTemplate
            .exchange(this.userInfoUri, HttpMethod.GET, entity, Map.class)
            .getBody();
        log.debug("Response from user-info Endpoint: {}", userInfoEndpointData);

        return userInfoEndpointData;
    }
}
