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
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Service, der einen OIDC /userinfo Endpoint aufruft (mit JWT Bearer Auth) und dort die enthaltenen
 * "Authorities" extrahiert.
 */
@Slf4j
public class UserInfoAuthoritiesService {

    private static final String NAME_AUTHENTICATION_CACHE = "authentication_cache";

    private static final String CLAIM_AUTHORITIES = "authorities";

    private final String userInfoUri;
    private final RestTemplate restTemplate;
    private final Cache cache;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param userInfoUri userinfo Endpoint URI
     * @param restTemplate ein {@link RestTemplate}
     */
    public UserInfoAuthoritiesService(final String userInfoUri, final RestTemplate restTemplate) {
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
    public Collection<SimpleGrantedAuthority> loadAuthorities(Jwt jwt) {
        ValueWrapper valueWrapper = this.cache.get(jwt.getSubject());
        if (valueWrapper != null) {
            // value present in cache
            @SuppressWarnings("unchecked")
            Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) valueWrapper.get();
            log.debug("Resolved authorities (from cache): {}", authorities);
            return authorities;
        }

        log.debug("Fetching user-info for token subject: {}", jwt.getSubject());
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue());
        final HttpEntity<String> entity = new HttpEntity<>(headers);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = restTemplate
                .exchange(this.userInfoUri, HttpMethod.GET, entity, Map.class)
                .getBody();

            log.debug("Response from user-info Endpoint: {}", map);
            if (map.containsKey(CLAIM_AUTHORITIES)) {
                authorities = asAuthorities(map.get(CLAIM_AUTHORITIES));
            }
            log.debug("Resolved Authorities (from /userinfo Endpoint): {}", authorities);
            // store
            this.cache.put(jwt.getSubject(), authorities);
        } catch (Exception e) {
            log.error(
                String.format(
                    "Could not fetch user details from %s - user is granted NO authorities",
                    this.userInfoUri
                ),
                e
            );
        }

        return authorities;
    }

    private static List<SimpleGrantedAuthority> asAuthorities(Object object) {
        final var authorities = new ArrayList<SimpleGrantedAuthority>();
        if (object instanceof Collection<?> collection) {
            object = collection.toArray(new Object[0]);
        }
        if (ObjectUtils.isArray(object)) {
            authorities.addAll(
                Stream
                    .of(((Object[]) object))
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList())
            );
        }
        return authorities;
    }
}
