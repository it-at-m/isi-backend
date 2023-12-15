package de.muenchen.isi.domain.service.transition;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

        final var isi = new HashMap<String, Object>();
        isi.put("roles", Arrays.asList(customUser.roles()));
        final var resourceAccessObject = new HashMap<String, Object>();
        resourceAccessObject.put("isi", isi);

        final var jwtPrincipal = new Jwt(
            "the-token-value",
            Instant.now(),
            Instant.now().plusSeconds(1800),
            Map.of("header1", new Object()),
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
            )
        );

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(
            authoritiesRoles.toArray(String[]::new)
        );
        final var authentication = new JwtAuthenticationToken(jwtPrincipal, authorities);
        context.setAuthentication(authentication);
        return context;
    }
}
