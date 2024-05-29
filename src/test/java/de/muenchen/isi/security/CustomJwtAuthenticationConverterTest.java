package de.muenchen.isi.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CustomJwtAuthenticationConverterTest {

    @Mock
    private UserInfoDataService userInfoDataService;

    private CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    @BeforeEach
    public void beforeEach() {
        this.customJwtAuthenticationConverter = new CustomJwtAuthenticationConverter(userInfoDataService);
        Mockito.reset(userInfoDataService);
    }

    @Test
    void convert() throws IllegalAccessException {
        final var jwt = new Jwt(
            "the-tokenvalue",
            Instant.now().minusSeconds(10),
            Instant.now().plusSeconds(10),
            Map.of("header1", "the-header-value"),
            Map.of("sub", "123456789", UserInfoDataService.CLAIM_SURNAME, "surname-to-overwrite")
        );

        var response = new UserInfoDataService.UserInfoData();
        response.setAuthorities(List.of(new SimpleGrantedAuthority("authority-1")));
        response.setClaims(
            Map.of(
                UserInfoDataService.CLAIM_SURNAME,
                "the-surname",
                UserInfoDataService.CLAIM_DEPARTMENT,
                "the-department"
            )
        );

        Mockito.when(userInfoDataService.loadUserInfoData(jwt)).thenReturn(response);

        var result = customJwtAuthenticationConverter.convert(jwt);

        var jwtExpected = new Jwt(
            "the-tokenvalue",
            Instant.now().minusSeconds(10),
            Instant.now().plusSeconds(10),
            Map.of("header1", "the-header-value"),
            Map.of(
                "sub",
                "123456789",
                UserInfoDataService.CLAIM_SURNAME,
                "the-surname",
                UserInfoDataService.CLAIM_DEPARTMENT,
                "the-department"
            )
        );

        var expected = new JwtAuthenticationToken(jwtExpected, List.of(new SimpleGrantedAuthority("authority-1")));

        assertThat(result.getAuthorities(), is(expected.getAuthorities()));
        var jwtResultFromJwtAuthenticationToken = (Jwt) FieldUtils.readField(result, "token", true);
        var jwtExpectedFromJwtAuthenticationToken = (Jwt) FieldUtils.readField(expected, "token", true);
        assertThat(
            jwtResultFromJwtAuthenticationToken.getTokenValue(),
            is(jwtExpectedFromJwtAuthenticationToken.getTokenValue())
        );
        assertThat(jwtResultFromJwtAuthenticationToken.getIssuedAt(), isA(Instant.class));
        assertThat(jwtResultFromJwtAuthenticationToken.getExpiresAt(), isA(Instant.class));
        assertThat(
            jwtResultFromJwtAuthenticationToken.getHeaders(),
            is(jwtExpectedFromJwtAuthenticationToken.getHeaders())
        );
        assertThat(
            jwtResultFromJwtAuthenticationToken.getClaims(),
            is(jwtExpectedFromJwtAuthenticationToken.getClaims())
        );

        Mockito.verify(userInfoDataService, Mockito.times(1)).loadUserInfoData(jwt);
    }
}
