package de.muenchen.isi.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
     * Die Methode extrahiert die Authorities des Nutzers aus der {@link Authentication} des {@link SecurityContextHolder}.
     *
     * @return Liste der Authorities aus {@link AuthoritiesEnum} des Nutzers
     */
    public List<AuthoritiesEnum> getUserAuthorities() {
        ArrayList<AuthoritiesEnum> authorities = new ArrayList<>();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isNotEmpty(authentication)) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (EnumUtils.isValidEnum(AuthoritiesEnum.class, authority.getAuthority())) {
                    authorities.add(AuthoritiesEnum.valueOf(authority.getAuthority()));
                } else {
                    log.error("Authority {} nicht in AuthoritiesEnum vorhanden.\n", authority.getAuthority());
                }
            }
        }
        return authorities;
    }

    /**
     * Die Methode extrahiert den Nutzernamen aus dem {@link Jwt} der {@link Authentication} des {@link SecurityContextHolder}.
     *
     * @return den Nutzernamen oder einen Platzhalter fall kein {@link Jwt} verfügbar
     */
    public String getUsername() {
        String username = null;
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
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
     * Die Methode extrahiert den Nutzernamen aus dem {@link Jwt} der {@link Authentication} des {@link SecurityContextHolder}.
     *
     * @return den Nutzernamen oder einen Platzhalter falls kein {@link Jwt} verfügbar
     */
    public String getUserSub() {
        String sub = null;
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isNotEmpty(authentication) && !(authentication.getPrincipal() instanceof String)) {
            final var jwt = (Jwt) authentication.getPrincipal();
            if (ObjectUtils.isNotEmpty(jwt)) {
                sub = jwt.getClaimAsString(TOKEN_USER_SUB);
            }
        }
        return StringUtils.isNotBlank(sub) ? sub : SUB_UNAUTHENTICATED_USER;
    }

    /**
     * Die Methode extrahiert die Nutzerrollen des Nutzers aus dem {@link Jwt} der {@link Authentication} des {@link SecurityContextHolder}.
     *
     * @return Liste der Nutzerrollen des Nutzers
     */
    public List<String> getUserRoles() {
        final var roles = new ArrayList<String>();
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isNotEmpty(authentication) && !(authentication.getPrincipal() instanceof String)) {
            final var jwt = (Jwt) authentication.getPrincipal();
            if (ObjectUtils.isNotEmpty(jwt)) {
                final var resourceAccess = (Map<String, Object>) jwt.getClaim(TOKEN_RESOURCE_ACCESS);
                if (ObjectUtils.isNotEmpty(resourceAccess)) {
                    final var isi = (Map<String, Object>) resourceAccess.get(TOKEN_ISI);
                    if (ObjectUtils.isNotEmpty(isi)) {
                        final var rolesInToken = (List<String>) isi.get(TOKEN_ROLES);
                        if (ObjectUtils.isNotEmpty(rolesInToken)) {
                            roles.addAll(rolesInToken);
                        }
                    }
                }
            }
        }
        return roles;
    }
}
