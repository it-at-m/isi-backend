package de.muenchen.isi.domain.service.common;

import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.security.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Stellt den Nutzerbezug für personenbezogene Daten bereit.
 * <p>
 * Der Nutzerbezug wird ausschließlich aus dem Token abgeleitet und niemals aus dem Request übernommen.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

    private final AuthenticationUtils authenticationUtils;

    /**
     * Gibt den userSub des authentifizierten Nutzers zurück, sofern es kein Fallback-Wert ist.
     *
     * @param fehlermeldung die Fehlermeldung für den Fall, dass der Nutzer nicht authentifiziert ist.
     *                      Sie benennt den fachlichen Kontext des Aufrufers.
     * @return den userSub aus {@link AuthenticationUtils}.
     * @throws UserRoleNotAllowedException falls der Nutzer den Fallback-Sub aus {@link AuthenticationUtils}
     *                                     zugewiesen hat.
     */
    public String getSubFromAuthenticatedUser(final String fehlermeldung) throws UserRoleNotAllowedException {
        final String userSub = authenticationUtils.getUserSub();
        if (authenticationUtils.isSubFromUnauthenticatedUser(userSub)) {
            throw new UserRoleNotAllowedException(fehlermeldung);
        }
        return userSub;
    }
}
