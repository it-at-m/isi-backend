package de.muenchen.isi.security;

import de.muenchen.isi.security.AuthoritiesEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

@Slf4j
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
            try {
                final DefaultOAuth2AuthenticatedPrincipal principal =
                    (DefaultOAuth2AuthenticatedPrincipal) authentication.getPrincipal();
                if (!ObjectUtils.isEmpty(principal)) {
                    for (GrantedAuthority authority : principal.getAuthorities()) {
                        userRoles.add(AuthoritiesEnum.valueOf(authority.getAuthority()));
                    }
                }
            } catch (final ClassCastException exception) {
                log.error(exception.getMessage(), exception);
            } catch (final IllegalArgumentException exception) {
                log.error(exception.getMessage(), exception);
            }
        }
        return userRoles;
    }
}
