package de.muenchen.isi.domain.service.transition;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
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
        String[] authoritiesRoles = new String[9];
        if (customUser.roles()[0].equals("lhm-isi-admin")) {
            authoritiesRoles[0] = "ISI_BACKEND_FREIGABE_ABFRAGE";
            authoritiesRoles[1] = "ISI_BACKEND_ABBRECHEN_ABFRAGE";
            authoritiesRoles[2] = "ISI_BACKEND_ANGABEN_ANPASSEN_ABFRAGE";
            authoritiesRoles[3] = "ISI_BACKEND_IN_BEARBEITUNG_SETZTEN";
            authoritiesRoles[4] = "ISI_BACKEND_KORRIGIEREN";
            authoritiesRoles[5] = "ISI_BACKEND_KEINE_BEARBEITUNG_NOETIG_ABFRAGE";
            authoritiesRoles[6] = "ISI_BACKEND_VERSCHICKEN_DER_STELLUNGNAHME_ABFRAGE";
            authoritiesRoles[7] = "ISI_BACKEND_BEDARFSMELDUNG_ERFOLGT_ABFRAGE";
            authoritiesRoles[8] = "ISI_BACKEND_SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG_ABFRAGE";
        }

        if (customUser.roles()[0].equals("lhm-isi-sachbearbeiter_kita_schule_PLAN")) {
            authoritiesRoles[0] = "ISI_BACKEND_ABBRECHEN_ABFRAGE";
            authoritiesRoles[1] = "ISI_BACKEND_ANGABEN_ANPASSEN_ABFRAGE";
            authoritiesRoles[2] = "ISI_BACKEND_IN_BEARBEITUNG_SETZTEN";
            authoritiesRoles[3] = "ISI_BACKEND_KORRIGIEREN";
            authoritiesRoles[4] = "ISI_BACKEND_KEINE_BEARBEITUNG_NOETIG_ABFRAGE";
            authoritiesRoles[5] = "ISI_BACKEND_VERSCHICKEN_DER_STELLUNGNAHME_ABFRAGE";
            authoritiesRoles[6] = "ISI_BACKEND_BEDARFSMELDUNG_ERFOLGT_ABFRAGE";
            authoritiesRoles[7] = "ISI_BACKEND_SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG_ABFRAGE";
        }

        if (customUser.roles()[0].equals("lhm-isi-abfrageersteller")) {
            authoritiesRoles[0] = "ISI_BACKEND_FREIGABE_ABFRAGE";
            authoritiesRoles[1] = "ISI_BACKEND_BEDARFSMELDUNG_ERFOLGT_ABFRAGE";
        }

        if (customUser.roles()[0].equals("lhm-isi-nutzer")) {
            authoritiesRoles[0] = "ISI_BACKEND_READ_ABFRAGE";
        }

        System.out.println("--- LÄNGE --- :" + authoritiesRoles.length);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        JSONArray roles = new JSONArray();

        Collections.addAll(roles, customUser.roles());

        JSONObject isi = new JSONObject();
        isi.put("roles", roles);
        JSONObject resourceAccessObject = new JSONObject();
        resourceAccessObject.put("isi", isi);

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(authoritiesRoles);
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
