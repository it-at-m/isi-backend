package de.muenchen.isi.security;

import de.muenchen.isi.infrastructure.entity.common.BearbeitendePerson;
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

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_ABFRAGEERSTELLUNG = "abfrageerstellung";
    public static final String ROLE_SACHBEARBEITUNG = "sachbearbeitung";
    public static final String ROLE_ANWENDER = "anwender";
    private static final String UNAUTHENTICATED_USER = "unauthenticated";
    private static final String TOKEN_USER_NAME = "username";
    private static final String TOKEN_SURNAME = "surname";
    private static final String TOKEN_GIVENNAME = "givenname";
    private static final String TOKEN_EMAIL = "email";
    private static final String TOKEN_DEPARTMENT = "department";
    private static final String TOKEN_USER_SUB = "sub";
    private static final String SUB_UNAUTHENTICATED_USER = "123456789";
    private static final String TOKEN_RESOURCE_ACCESS = "resource_access";
    private static final String TOKEN_ISI = "isi";
    private static final String TOKEN_ROLES = "roles";

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
        return StringUtils.isNotBlank(username) ? username : UNAUTHENTICATED_USER;
    }

    /**
     * Die Methode extrahiert den Nachnamen aus dem {@link Jwt} der {@link Authentication} des {@link SecurityContextHolder}.
     *
     * @return den Nachnamen oder einen Platzhalter fall kein {@link Jwt} verfügbar
     */
    public String getSurname() {
        String surname = null;
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication)) {
            final var principal = authentication.getPrincipal();
            if (Objects.equals(Jwt.class, principal.getClass())) {
                final var jwt = (Jwt) principal;
                surname = jwt.getClaimAsString(TOKEN_SURNAME);
            }
        }
        return StringUtils.isNotBlank(surname) ? surname : UNAUTHENTICATED_USER;
    }

    /**
     * Die Methode extrahiert den Vornamen aus dem {@link Jwt} der {@link Authentication} des {@link SecurityContextHolder}.
     *
     * @return die vorname oder einen Platzhalter fall kein {@link Jwt} verfügbar
     */
    public String getGivenname() {
        String givenname = null;
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication)) {
            final var principal = authentication.getPrincipal();
            if (Objects.equals(Jwt.class, principal.getClass())) {
                final var jwt = (Jwt) principal;
                givenname = jwt.getClaimAsString(TOKEN_GIVENNAME);
            }
        }
        return StringUtils.isNotBlank(givenname) ? givenname : UNAUTHENTICATED_USER;
    }

    /**
     * Die Methode extrahiert die Emailadresse aus dem {@link Jwt} der {@link Authentication} des {@link SecurityContextHolder}.
     *
     * @return die Emailadresse oder einen Platzhalter fall kein {@link Jwt} verfügbar
     */
    public String getEmail() {
        String email = null;
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication)) {
            final var principal = authentication.getPrincipal();
            if (Objects.equals(Jwt.class, principal.getClass())) {
                final var jwt = (Jwt) principal;
                email = jwt.getClaimAsString(TOKEN_EMAIL);
            }
        }
        return StringUtils.isNotBlank(email) ? email : UNAUTHENTICATED_USER;
    }

    /**
     * Die Methode extrahiert die Organisationseinheit aus dem {@link Jwt} der {@link Authentication} des {@link SecurityContextHolder}.
     *
     * @return die Organisationseinheit oder einen Platzhalter falls kein {@link Jwt} verfügbar
     */
    public String getOrganisationseinheit() {
        String department = null;
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication)) {
            final var principal = authentication.getPrincipal();
            if (Objects.equals(Jwt.class, principal.getClass())) {
                final var jwt = (Jwt) principal;
                department = jwt.getClaimAsString(TOKEN_DEPARTMENT);
            }
        }
        return StringUtils.isNotBlank(department) ? department : UNAUTHENTICATED_USER;
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
                final var resourceAccess = jwt.getClaim(TOKEN_RESOURCE_ACCESS);
                if (ObjectUtils.isNotEmpty(resourceAccess)) {
                    final var isi = ((Map<?, ?>) resourceAccess).get(TOKEN_ISI);
                    if (ObjectUtils.isNotEmpty(isi) && isi instanceof Map) {
                        final var rolesInToken = ((Map<?, ?>) isi).get(TOKEN_ROLES);
                        if (ObjectUtils.isNotEmpty(rolesInToken) && rolesInToken instanceof List) {
                            roles.addAll((List<String>) rolesInToken);
                        }
                    }
                }
            }
        }
        return roles;
    }

    public boolean isRoleAnwender() {
        return getUserRoles().stream().allMatch(s -> s.contains(ROLE_ANWENDER));
    }

    /**
     * Die Methode erstellt die zu {@link BearbeitendePerson} mit den aus dem {@link Jwt} der {@link Authentication}
     * des {@link SecurityContextHolder} extrahierten Informationen.
     *
     * @return die zu {@link BearbeitendePerson} oder einen Platzhalter falls kein {@link Jwt} verfügbar
     */
    public BearbeitendePerson getBearbeitendePerson() {
        final var bearbeitendePerson = new BearbeitendePerson();
        final var surname = this.getSurname();
        final var givenname = this.getGivenname();
        bearbeitendePerson.setName(givenname + " " + surname);
        final var email = this.getEmail();
        bearbeitendePerson.setEmail(email);
        final var organisationseinheit = this.getOrganisationseinheit();
        bearbeitendePerson.setOrganisationseinheit(organisationseinheit);
        return bearbeitendePerson;
    }
}
