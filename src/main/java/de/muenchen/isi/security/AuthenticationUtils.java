package de.muenchen.isi.security;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationUtils {

    private static final String NAME_UNAUTHENTICATED_USER = "unauthenticated";

    private static final String TOKEN_USER_NAME = "username";

    private static final String TOKEN_USER_SUB = "sub";

    private static final String SUB_UNAUTHENTICATED_USER = "123456789";

    private static final String TOKEN_RESOURCE_ACCESS = "resource_access";

    private static final String TOKEN_ISI = "isi";

    private static final String TOKEN_ROLES = "roles";

    public static final String ROLE_ADMIN = "admin";

    public static final String ROLE_ABFRAGEERSTELLUNG = "abfrageerstellung";

    public static final String ROLE_SACHBEARBEITUNG = "sachbearbeitung";

    /**
     * Die Methode extrahiert die Authorities des Nutzers aus dem {@link DefaultOAuth2AuthenticatedPrincipal}
     *
     * @return Liste der Authorities aus {@link AuthoritiesEnum} des Nutzers
     */
    public List<AuthoritiesEnum> getUserAuthorities() {
        ArrayList<AuthoritiesEnum> userRoles = new ArrayList<>();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication)) {
            try {
                final DefaultOAuth2AuthenticatedPrincipal principal =
                    (DefaultOAuth2AuthenticatedPrincipal) authentication.getPrincipal();
                if (!ObjectUtils.isEmpty(principal)) {
                    for (GrantedAuthority authority : principal.getAuthorities()) {
                        if (EnumUtils.isValidEnum(AuthoritiesEnum.class, authority.getAuthority())) {
                            userRoles.add(AuthoritiesEnum.valueOf(authority.getAuthority()));
                        } else {
                            log.error("Authority {} nicht in AuthoritiesEnum vorhanden.\n", authority.getAuthority());
                        }
                    }
                }
            } catch (final ClassCastException | IllegalArgumentException exception) {
                log.error(exception.getMessage(), exception);
            }
        }
        return userRoles;
    }

    /**
     * Die Methode extrahiert den Nutzernamen aus dem im SecurityContext vorhandenen {@link Jwt}.
     *
     * @return den Nutzernamen oder einen Platzhalter fall kein {@link Jwt} verfügbar
     */
    public String getUsername() {
        String username = null;
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication)) {
            final var principal = authentication.getPrincipal();
            if (Objects.equals(Jwt.class, principal.getClass())) {
                final var jwt = (Jwt) principal;
                username = jwt.getClaimAsString(TOKEN_USER_NAME);
            }
        }
        return StringUtils.isNotBlank(username) ? username : NAME_UNAUTHENTICATED_USER;
    }

    /**
     * Die Methode extrahiert den Nutzernamen aus dem im SecurityContext vorhandenen {@link Jwt}.
     *
     * @return den Nutzernamen oder einen Platzhalter falls kein {@link Jwt} verfügbar
     */
    public String getUserSub() {
        String sub = null;
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication)) {
            final DefaultOAuth2AuthenticatedPrincipal principal =
                (DefaultOAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            if (!ObjectUtils.isEmpty(principal)) {
                sub = principal.getAttribute(TOKEN_USER_SUB).toString();
            }
        }
        return StringUtils.isNotBlank(sub) ? sub : SUB_UNAUTHENTICATED_USER;
    }

    /**
     * Die Methode extrahiert die Nutzerrollen des Nutzers aus dem {@link DefaultOAuth2AuthenticatedPrincipal}
     *
     * @return Liste der Nutzerrollen des Nutzers
     */
    public List<String> getUserRoles() {
        List<String> roles = new ArrayList<>();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication)) {
            final DefaultOAuth2AuthenticatedPrincipal principal =
                (DefaultOAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            if (!ObjectUtils.isEmpty(principal)) {
                JSONObject resourceAccess = principal.getAttribute(TOKEN_RESOURCE_ACCESS);
                if (!resourceAccess.isEmpty()) {
                    JSONObject isi = (JSONObject) resourceAccess.get(TOKEN_ISI);
                    if (!isi.isEmpty()) {
                        JSONArray rolesArray = (JSONArray) isi.get(TOKEN_ROLES);
                        if (!rolesArray.isEmpty()) {
                            rolesArray.forEach(role -> roles.add(role.toString()));
                        }
                    }
                }
            }
        }

        return roles;
    }
}
