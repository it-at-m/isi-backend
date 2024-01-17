package de.muenchen.isi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Ein custom {@link JwtAuthenticationConverter}, der die Authorities mittels
 * {@link UserInfoAuthoritiesService} vom /userinfo Endpoint des OIDC Providers bezieht.
 */
@RequiredArgsConstructor
@Profile("!no-security")
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserInfoAuthoritiesService userInfoAuthoritiesService;

    @Override
    public AbstractAuthenticationToken convert(final Jwt source) {
        return new JwtAuthenticationToken(source, this.userInfoAuthoritiesService.loadAuthorities(source));
    }
}
