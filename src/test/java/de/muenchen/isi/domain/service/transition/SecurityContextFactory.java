package de.muenchen.isi.domain.service.transition;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class SecurityContextFactory implements WithSecurityContextFactory<MockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(MockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        JSONArray roles = new JSONArray();

        Collections.addAll(roles, customUser.roles());

        JSONObject isi = new JSONObject();
        isi.put("roles", roles);
        JSONObject resourceAccessObject = new JSONObject();
        resourceAccessObject.put("isi", isi);

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ISI_BACKEND_FREIGABE_ABFRAGE");
        DefaultOAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(
                "Name",
                Map.of(
                        "sub", customUser.sub(),
                        "surname", customUser.surname(),
                        "givenname", customUser.givenname(),
                        "department", customUser.department(),
                        "email", customUser.email(),
                        "username", customUser.username(),
                        "resource_access", resourceAccessObject
                ),
                authorities
        );
        BearerTokenAuthentication auth = new BearerTokenAuthentication(principal, new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "test-token", null, null), authorities);
        context.setAuthentication(auth);
        return context;
    }
}
