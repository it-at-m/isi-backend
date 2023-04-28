package de.muenchen.isi.domain.service.util;

import de.muenchen.isi.security.AuthoritiesEnum;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationUtils {

    /**
     * Die Methode extrahiert die Nutzer Authorities aus dem {@link DefaultOAuth2AuthenticatedPrincipal}
     *
     * @return Liste der Nutzer {@link AuthoritiesEnum}
     */
    public static List<AuthoritiesEnum> getUserAuthorities() {
        ArrayList<AuthoritiesEnum> userRoles = new ArrayList<>();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication)) {
            final DefaultOAuth2AuthenticatedPrincipal principal =
                (DefaultOAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            if (!ObjectUtils.isEmpty(principal)) {
                for (GrantedAuthority authority : principal.getAuthorities()) {
                    userRoles.add(AuthoritiesEnum.valueOf(authority.getAuthority()));
                }
            }
        }
        return userRoles;
    }
}
