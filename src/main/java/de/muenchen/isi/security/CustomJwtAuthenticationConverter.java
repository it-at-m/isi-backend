package de.muenchen.isi.security;

import lombok.RequiredArgsConstructor;
import org.apache.commons.exec.util.MapUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Ein custom {@link JwtAuthenticationConverter}, der die Authorities mittels
 * {@link UserInfoDataService} vom /userinfo Endpoint des OIDC Providers bezieht.
 */
@RequiredArgsConstructor
@Profile("!no-security")
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserInfoDataService userInfoDataService;

    @Override
    public AbstractAuthenticationToken convert(final Jwt source) {
        final var userInfoData = this.userInfoDataService.loadUserInfoData(source);
        final var tokenValue = source.getTokenValue();
        final var issuedAt = source.getIssuedAt();
        final var expiresAt = source.getExpiresAt();
        final var headers = source.getHeaders();
        final var mergedClaims = MapUtils.merge(source.getClaims(), userInfoData.getClaims());
        final var jwtEnrichedWithUserInfoData = new Jwt(tokenValue, issuedAt, expiresAt, headers, mergedClaims);
        return new JwtAuthenticationToken(jwtEnrichedWithUserInfoData, userInfoData.getAuthorities());
    }
}
