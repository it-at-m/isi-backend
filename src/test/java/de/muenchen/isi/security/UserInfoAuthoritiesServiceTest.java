package de.muenchen.isi.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.api.advice.RestExceptionHandler;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codelibs.jhighlight.fastutil.Hash;
import org.h2.engine.User;
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserInfoAuthoritiesServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private UserInfoAuthoritiesService userInfoAuthoritiesService;

    @BeforeEach
    public void beforeEach() {
        this.userInfoAuthoritiesService = new UserInfoAuthoritiesService("userinfo-uri", restTemplate);
        Mockito.reset(restTemplate);
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

        final var result = this.userInfoAuthoritiesService.getDataFromUserInfoEndpoint(jwt);

        assertThat(result, is(response));

        Mockito.verify(this.restTemplate, Mockito.times(1)).exchange("userinfo-uri", HttpMethod.GET, entity, Map.class);
    }
}
