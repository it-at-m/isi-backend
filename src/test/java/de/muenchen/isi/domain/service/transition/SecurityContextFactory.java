package de.muenchen.isi.domain.service.transition;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@Slf4j
public class SecurityContextFactory implements WithSecurityContextFactory<MockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(MockCustomUser customUser) {
        List<String> authoritiesRoles = new ArrayList<>();
        if (customUser.roles()[0].equals("admin")) {
            authoritiesRoles.add("ISI_BACKEND_FREIGABE_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_ABBRECHEN_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_ZURUECK_AN_ABFRAGEERSTELLUNG_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_IN_BEARBEITUNG_SETZTEN_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_ZURUECK_AN_SACHBEARBEITUNG_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_KEINE_BEARBEITUNG_NOETIG_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_VERSCHICKEN_DER_STELLUNGNAHME_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_BEDARFSMELDUNG_ERFOLGTE_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_ERNEUTE_BEARBEITUNG_ABFRAGE");
        }

        if (customUser.roles()[0].equals("sachbearbeitung")) {
            authoritiesRoles.add("ISI_BACKEND_ABBRECHEN_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_ZURUECK_AN_ABFRAGEERSTELLUNG_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_IN_BEARBEITUNG_SETZTEN_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_ZURUECK_AN_SACHBEARBEITUNG_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_KEINE_BEARBEITUNG_NOETIG_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_VERSCHICKEN_DER_STELLUNGNAHME_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_BEDARFSMELDUNG_ERFOLGTE_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_ERNEUTE_BEARBEITUNG_ABFRAGE");
        }

        if (customUser.roles()[0].equals("abfrageerstellung")) {
            authoritiesRoles.add("ISI_BACKEND_FREIGABE_ABFRAGE");
            authoritiesRoles.add("ISI_BACKEND_SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG_ABFRAGE");
        }

        if (customUser.roles()[0].equals("nutzer")) {
            authoritiesRoles.add("ISI_BACKEND_READ_ABFRAGE");
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        JSONArray roles = new JSONArray();

        Collections.addAll(roles, customUser.roles());

        JSONObject isi = new JSONObject();
        isi.put("roles", roles);
        JSONObject resourceAccessObject = new JSONObject();
        resourceAccessObject.put("isi", isi);
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(
            authoritiesRoles.toArray(String[]::new)
        );
        DefaultOAuth2AuthenticatedPrincipal principal = new DefaultOAuth2AuthenticatedPrincipal(
            "Name",
            Map.of(
                "sub",
                customUser.sub(),
                "surname",
                customUser.surname(),
                "givenname",
                customUser.givenname(),
                "department",
                customUser.department(),
                "email",
                customUser.email(),
                "username",
                customUser.username(),
                "resource_access",
                resourceAccessObject
            ),
            authorities
        );
        BearerTokenAuthentication auth = new BearerTokenAuthentication(
            principal,
            new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "test-token", null, null),
            authorities
        );
        context.setAuthentication(auth);
        return context;
    }
}
