package de.muenchen.isi.domain.service.util;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationUtils {


    /**
     * Die Methode extrahiert die Nutzer Rollen aus dem {@link DefaultOAuth2AuthenticatedPrincipal}
     *
     * @return die Rollen vom Nutzer
     *
     */
    public static List<String> getUserRoles() {
        ArrayList<String> userRoles = new ArrayList<>();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!ObjectUtils.isEmpty(authentication)) {
            final DefaultOAuth2AuthenticatedPrincipal principal = (DefaultOAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            if (!ObjectUtils.isEmpty(principal)) {
                final JSONObject resourceAccess = (JSONObject) principal.getAttributes().get("resource_access");
                final JSONObject isi = (JSONObject) resourceAccess.get("isi");
                final JSONArray roles = (JSONArray) isi.get("roles");
                for(Object role : roles) {
                    userRoles.add(String.valueOf(role));
                }
            }
        }
        return userRoles;
    }
}
