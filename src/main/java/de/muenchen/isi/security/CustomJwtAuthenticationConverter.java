package de.muenchen.isi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Ersetzt den {@link JwtAuthenticationConverter} wegen Deprecation.
 * Siehe {@link JwtAuthenticationConverter#extractAuthorities(Jwt)}.
 * <p>
 * Dieser Converter orientiert sich an {@link JwtBearerTokenAuthenticationConverter}.
 */
@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final CustomUserInfoTokenServices userInfoTokenServices;

    /**
     * Erstellt das {@link AbstractAuthenticationToken} für den SecurityContext des entsprechenden Threads.
     *
     * @param jwt als Access-Token.
     * @return den {@link AbstractAuthenticationToken} erstellt aus dem Access-Token, angereichert um
     * die "authorities" (Rechte) vom Userinfo-Endpunkt des SSO.
     */
    @Override
    public AbstractAuthenticationToken convert(final Jwt jwt) {
        final OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                jwt.getTokenValue(),
                jwt.getIssuedAt(),
                jwt.getExpiresAt()
        );
        final Map<String, Object> attributes = jwt.getClaims();
        // Holen der Authorities vom UserInfoEndpunkt des SSO
        final Collection<GrantedAuthority> authorities = this.loadGrantedAuthoritiesFromUserInfoEndpoint(accessToken);
        final OAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(attributes, authorities);
        return new BearerTokenAuthentication(principal, accessToken, authorities);
    }

    protected Collection<GrantedAuthority> loadGrantedAuthoritiesFromUserInfoEndpoint(final OAuth2AccessToken accessToken) {
        final var authentication = this.userInfoTokenServices.loadAuthentication(accessToken.getTokenValue());
        return authentication.getUserAuthentication().getAuthorities().stream()
                .map(grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority()))
                .collect(Collectors.toSet());
    }

}
