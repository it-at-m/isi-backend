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
import org.apache.commons.lang3.ObjectUtils;
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
 * "Authorities" und Nutzerinformationen extrahiert.
 */
@Slf4j
public class UserInfoDataService {

    @Data
    public static final class UserInfoData {

        private Map<String, Object> claims;

        private Collection<SimpleGrantedAuthority> authorities;
    }

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
        this.cache = new CaffeineCache(
            CachingConfiguration.NAME_AUTHENTICATION_CACHE,
            Caffeine.newBuilder()
                .expireAfterWrite(CachingConfiguration.AUTHENTICATION_CACHE_EXPIRATION_TIME_SECONDS, TimeUnit.SECONDS)
                .ticker(Ticker.systemTicker())
                .build()
        );
    }

    /**
     * Ruft den /userinfo Endpoint und extrahiert {@link GrantedAuthority}s aus dem "authorities"
     * Claim sowie weitere personalisierte Claims.
     *
     *
     * @param jwt der JWT
     * @return die {@link GrantedAuthority}s sowie weitere personalisierte Claims.
     */
    public UserInfoData loadUserInfoData(final Jwt jwt) {
        // Rückgeben der UserInfoData aus Cache falls vorhanden.
        final var valueWrapper = this.cache.get(jwt.getTokenValue());
        if (
            ObjectUtils.isNotEmpty(valueWrapper) &&
            ObjectUtils.isNotEmpty(valueWrapper.get()) &&
            UserInfoData.class.equals(valueWrapper.get().getClass())
        ) {
            final var userInfoData = (UserInfoData) valueWrapper.get();
            log.debug("Resolved UserInfoData (from cache): {}", userInfoData);
            return userInfoData;
        }

        // Ermitteln der UserInfoData
        final var userInfoData = new UserInfoData();
        try {
            final Map<String, Object> userInfoEndpointData = this.getDataFromUserInfoEndpoint(jwt);

            final var authorities = this.getAuthoritiesFromUserInfoEndpointData(userInfoEndpointData);
            userInfoData.setAuthorities(authorities);
            final var claims = this.getClaimsFromUserInfoEndpointData(userInfoEndpointData);
            userInfoData.setClaims(claims);

            log.debug("Resolved UserInfoData (from /userinfo Endpoint): {}", userInfoData);

            // Hinterlegen der UserInfoData im Cache.
            this.cache.put(jwt.getTokenValue(), userInfoData);
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

    /**
     * Extrahiert {@link GrantedAuthority}s aus dem "authorities" Claim.
     *
     * @param userInfoEndpointData erhalten vom /userinfo Endpoint.
     * @return die {@link GrantedAuthority}s gem. Claim "authorities" des /userinfo Endpoints sowie weitere personalisierte Claims.
     */
    protected List<SimpleGrantedAuthority> getAuthoritiesFromUserInfoEndpointData(
        final Map<String, Object> userInfoEndpointData
    ) {
        final var authorities = new ArrayList<SimpleGrantedAuthority>();
        if (userInfoEndpointData.containsKey(CLAIM_AUTHORITIES)) {
            authorities.addAll(this.asAuthorities(userInfoEndpointData.get(CLAIM_AUTHORITIES)));
        }
        return authorities;
    }

    /**
     * Extrahiert aus dem Claim im Parameter die {@link SimpleGrantedAuthority}.
     *
     * @param authoritiesClaim mit den Authorities.
     * @return die {@link GrantedAuthority}s des im Parameter gegebenen Claims.
     */
    protected List<SimpleGrantedAuthority> asAuthorities(final Object authoritiesClaim) {
        final var authorities = new ArrayList<SimpleGrantedAuthority>();
        if (authoritiesClaim instanceof Collection<?>) {
            authorities.addAll(
                ((Collection<?>) authoritiesClaim)
                    .stream()
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .toList()
            );
        }
        return authorities;
    }

    /**
     * Extrahiert die personalisierten Claims.
     *
     * @param userInfoEndpointData erhalten vom /userinfo Endpoint mit den personalisierten Claims.
     * @return die gefundenen personalisierten Claims.
     */
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

    /**
     * Holt mit dem im Parameter gegebenen Access-Token die Nutzerinformationen vom /userinfo Endpoint.
     *
     * @param jwt zum Holen der Nutzerinformationen.
     * @return die Nutzerinformationen vom /userinfo Endpoint.
     */
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
