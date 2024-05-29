package de.muenchen.isi.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserInfoDataServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private UserInfoDataService userInfoDataService;

    @BeforeEach
    public void beforeEach() {
        this.userInfoDataService = new UserInfoDataService("userinfo-uri", restTemplate);
        Mockito.reset(restTemplate);
    }

    @Test
    void asAuthoritiesWithNullValue() {
        var result = userInfoDataService.asAuthorities(null);

        assertThat(result, is(List.of()));
    }

    @Test
    void asAuthoritiesWithNonCollection() {
        var result = userInfoDataService.asAuthorities("not-a-collection");

        assertThat(result, is(List.of()));
    }

    @Test
    void asAuthoritiesWithCollection() {
        var authorities = List.of("authority-1", "authority-2", "authority-3", "authority-4");

        var result = userInfoDataService.asAuthorities(authorities);

        var expected = List.of(
            new SimpleGrantedAuthority("authority-1"),
            new SimpleGrantedAuthority("authority-2"),
            new SimpleGrantedAuthority("authority-3"),
            new SimpleGrantedAuthority("authority-4")
        );

        assertThat(result, is(expected));
    }

    @Test
    void getClaimsFromUserInfoEndpointDataWithEmptyInput() {
        var userInfoEndpointData = new HashMap<String, Object>();

        var result = userInfoDataService.getClaimsFromUserInfoEndpointData(userInfoEndpointData);

        assertThat(result, is(Map.of()));
    }

    @Test
    void getClaimsFromUserInfoEndpointDataInputWithNullValueClaims() {
        var userInfoEndpointData = new HashMap<String, Object>();
        userInfoEndpointData.put("not returned claim", new Object());
        userInfoEndpointData.put(UserInfoDataService.CLAIM_SURNAME, null);
        userInfoEndpointData.put(UserInfoDataService.CLAIM_GIVENNAME, null);
        userInfoEndpointData.put(UserInfoDataService.CLAIM_DEPARTMENT, null);
        userInfoEndpointData.put(UserInfoDataService.CLAIM_EMAIL, null);
        userInfoEndpointData.put(UserInfoDataService.CLAIM_USERNAME, null);

        var result = userInfoDataService.getClaimsFromUserInfoEndpointData(userInfoEndpointData);

        assertThat(result, is(Map.of()));
    }

    @Test
    void getClaimsFromUserInfoEndpointDataInputWithClaims() {
        var userInfoEndpointData = new HashMap<String, Object>();
        userInfoEndpointData.put("not returned claim", new Object());
        userInfoEndpointData.put(UserInfoDataService.CLAIM_SURNAME, "the-surname");
        userInfoEndpointData.put(UserInfoDataService.CLAIM_GIVENNAME, "the-givenname");
        userInfoEndpointData.put(UserInfoDataService.CLAIM_DEPARTMENT, "the-department");
        userInfoEndpointData.put(UserInfoDataService.CLAIM_EMAIL, "the-email");
        userInfoEndpointData.put(UserInfoDataService.CLAIM_USERNAME, "the-username");

        var result = userInfoDataService.getClaimsFromUserInfoEndpointData(userInfoEndpointData);

        var expected = Map.of(
            UserInfoDataService.CLAIM_SURNAME,
            "the-surname",
            UserInfoDataService.CLAIM_GIVENNAME,
            "the-givenname",
            UserInfoDataService.CLAIM_DEPARTMENT,
            "the-department",
            UserInfoDataService.CLAIM_EMAIL,
            "the-email",
            UserInfoDataService.CLAIM_USERNAME,
            "the-username"
        );

        assertThat(result, is(expected));
    }

    @Test
    void getDataFromUserInfoEndpoint() {
        final var jwt = new Jwt(
            "the-tokenvalue",
            Instant.now().minusSeconds(10),
            Instant.now().plusSeconds(10),
            Map.of("header1", new Object()),
            Map.of("claim1", new Object())
        );

        final var headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer the-tokenvalue");
        final var entity = new HttpEntity<String>(headers);

        final var response = new HashMap<String, Object>();
        response.put("response-key", "response-value");
        Mockito
            .when(restTemplate.exchange("userinfo-uri", HttpMethod.GET, entity, Map.class))
            .thenReturn(ResponseEntity.ok(response));

        final var result = this.userInfoDataService.getDataFromUserInfoEndpoint(jwt);

        assertThat(result, is(response));

        Mockito.verify(this.restTemplate, Mockito.times(1)).exchange("userinfo-uri", HttpMethod.GET, entity, Map.class);
    }
}
