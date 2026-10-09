package de.muenchen.isi.domain.service.benutzer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.infrastructure.entity.benutzer.Benutzer;
import de.muenchen.isi.infrastructure.repository.benutzer.BenutzerRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BenutzerServiceTest {

    private static final String USER_SUB = "user-sub";

    private static final String FEHLERMELDUNG = "nicht authentifiziert";

    @Mock
    private BenutzerRepository benutzerRepository;

    @Spy
    private AuthenticationUtils authenticationUtils;

    private BenutzerService benutzerService;

    @BeforeEach
    void setUp() {
        benutzerService = new BenutzerService(benutzerRepository, authenticationUtils);
        Mockito.reset(benutzerRepository, authenticationUtils);
        when(authenticationUtils.getUserSub()).thenReturn(USER_SUB);
        when(authenticationUtils.isSubFromUnauthenticatedUser(USER_SUB)).thenReturn(false);
    }

    @Test
    void getOrCreateBenutzerLiefertBestehendenDatensatz() throws Exception {
        final var benutzer = new Benutzer();
        when(benutzerRepository.findByPersonalID(USER_SUB)).thenReturn(Optional.of(benutzer));

        assertThat(benutzerService.getOrCreateBenutzer(FEHLERMELDUNG), is(benutzer));
    }

    @Test
    void getOrCreateBenutzerLegtNeuenDatensatzMitUserSubAn() throws Exception {
        when(benutzerRepository.findByPersonalID(USER_SUB)).thenReturn(Optional.empty());

        assertThat(benutzerService.getOrCreateBenutzer(FEHLERMELDUNG).getPersonalID(), is(USER_SUB));
    }

    @Test
    void nichtAuthentifizierterNutzerWirdAbgelehnt() {
        when(authenticationUtils.isSubFromUnauthenticatedUser(USER_SUB)).thenReturn(true);

        assertThrows(UserRoleNotAllowedException.class, () -> benutzerService.findBenutzer(FEHLERMELDUNG));
        assertThrows(UserRoleNotAllowedException.class, () -> benutzerService.getOrCreateBenutzer(FEHLERMELDUNG));
    }

    @Test
    void saveUebersetztVersionskonflikt() {
        when(benutzerRepository.saveAndFlush(any())).thenThrow(
            new ObjectOptimisticLockingFailureException(Benutzer.class, "id")
        );

        assertThrows(OptimisticLockingException.class, () -> benutzerService.save(new Benutzer()));
    }

    @Test
    void saveUebersetztParallelesErstanlegen() {
        // Zwei parallele Requests legen erstmals an; der zweite verletzt die Eindeutigkeit von personalid.
        when(benutzerRepository.saveAndFlush(any())).thenThrow(
            new DataIntegrityViolationException("benutzer_personalid_key")
        );

        assertThrows(OptimisticLockingException.class, () -> benutzerService.save(new Benutzer()));
    }
}
