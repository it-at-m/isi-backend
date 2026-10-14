package de.muenchen.isi.domain.service.benutzer;

import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.infrastructure.entity.benutzer.Benutzer;
import de.muenchen.isi.infrastructure.repository.benutzer.BenutzerRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

/**
 * Zugriff auf die persönlichen Einstellungen des authentifizierten Nutzers.
 * <p>
 * Kapselt den Nutzerbezug für alle fachlichen Services, die auf dem {@link Benutzer}-Datensatz
 * arbeiten. Der Bezug wird ausschließlich über die {@link AuthenticationUtils} hergestellt und
 * niemals aus dem Request übernommen.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BenutzerService {

    private final BenutzerRepository benutzerRepository;

    private final AuthenticationUtils authenticationUtils;

    /**
     * @param fehlermeldung die verwendet wird, falls der Nutzer nicht authentifiziert ist.
     * @return den Datensatz des authentifizierten Nutzers, sofern einer existiert.
     * @throws UserRoleNotAllowedException falls der Nutzer nicht authentifiziert ist.
     */
    public Optional<Benutzer> findBenutzer(final String fehlermeldung) throws UserRoleNotAllowedException {
        return benutzerRepository.findByPersonalID(this.getSubFromAuthenticatedUser(fehlermeldung));
    }

    /**
     * Gibt den Datensatz des authentifizierten Nutzers zurück und legt ihn an, falls er noch nicht
     * existiert. Der neue Datensatz ist noch nicht persistiert.
     *
     * @param fehlermeldung die verwendet wird, falls der Nutzer nicht authentifiziert ist.
     * @return den bestehenden oder einen neuen {@link Benutzer}.
     * @throws UserRoleNotAllowedException falls der Nutzer nicht authentifiziert ist.
     */
    public Benutzer getOrCreateBenutzer(final String fehlermeldung) throws UserRoleNotAllowedException {
        final var userSub = this.getSubFromAuthenticatedUser(fehlermeldung);
        return benutzerRepository.findByPersonalID(userSub).orElseGet(() -> {
            final var benutzer = new Benutzer();
            benutzer.setPersonalID(userSub);
            return benutzer;
        });
    }

    /**
     * Speichert den Datensatz des Nutzers.
     *
     * @param benutzer der zu speichernde Datensatz.
     * @return den gespeicherten Datensatz.
     * @throws OptimisticLockingException falls bereits eine neuere Version gespeichert ist oder
     *         parallel bereits ein Datensatz für diesen Nutzer angelegt wurde.
     */
    public Benutzer save(final Benutzer benutzer) throws OptimisticLockingException {
        try {
            return benutzerRepository.saveAndFlush(benutzer);
        } catch (final ObjectOptimisticLockingFailureException | DataIntegrityViolationException exception) {
            // Speichern zwei parallele Requests erstmals, verletzt der zweite die Eindeutigkeit von
            // personalid. Da die Transaktion danach nur noch zurückgerollt werden kann, ist ein erneutes
            // Lesen und Speichern hier nicht möglich; der Konflikt wird deshalb wie ein Versionskonflikt
            // behandelt.
            final var message = "Die Daten wurden in der Zwischenzeit geändert. Bitte laden Sie die Seite neu!";
            throw new OptimisticLockingException(message, exception);
        }
    }

    /**
     * @param fehlermeldung die verwendet wird, falls der Nutzer nicht authentifiziert ist.
     * @return den userSub aus {@link AuthenticationUtils}.
     * @throws UserRoleNotAllowedException falls der Nutzer den Fallback-Sub zugewiesen hat.
     */
    private String getSubFromAuthenticatedUser(final String fehlermeldung) throws UserRoleNotAllowedException {
        return authenticationUtils.getSubFromAuthenticatedUser(fehlermeldung);
    }
}
