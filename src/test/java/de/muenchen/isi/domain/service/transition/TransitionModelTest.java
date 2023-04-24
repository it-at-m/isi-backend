package de.muenchen.isi.domain.service.transition;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.common.TransitionModel;
import de.muenchen.isi.domain.service.AbfrageService;
import de.muenchen.isi.domain.service.AbfrageStatusService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import de.muenchen.isi.rest.TestData;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(
    classes = { IsiBackendApplication.class },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = { "tomcat.gracefulshutdown.pre-wait-seconds=0" }
)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
public class TransitionModelTest {

    @Autowired
    private AbfrageService abfrageService;

    @Autowired
    private AbfrageStatusService abfrageStatusService;

    @Autowired
    private InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    @BeforeEach
    public void beforeEach() {
        this.infrastrukturabfrageRepository.deleteAll();
    }

    @Test
    void statusAenderungEntityNotFoundExcpetion() {
        final var uuid = UUID.randomUUID();
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid)
        );
    }

    @Test
    @Transactional
    @MockCustomUser
    void possbileTransitionsAngelegtAndRoleAdmin()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        TransitionModel expected = new TransitionModel();
        expected.setIndex(1);
        expected.setButtonName("FREIGABE");
        expected.setUrl("freigabe");
        expected.setDialogText("Die Abfrage wird zur Bearbeitung weitergeleitet und kann nicht mehr geändert werden.");

        assertThat(possibleTransitions.size(), is(1));
        assertThat(possibleTransitions.get(0), is(expected));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-sachbearbeiter_kita_schule_PLAN" })
    void possbileTransitionsAngelegtAndRoleSachbearbeiter()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-abfrageersteller" })
    void possbileTransitionsAngelegtAndRoleAbfrageersteller()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        TransitionModel expected = new TransitionModel();
        expected.setIndex(1);
        expected.setButtonName("FREIGABE");
        expected.setUrl("freigabe");
        expected.setDialogText("Die Abfrage wird zur Bearbeitung weitergeleitet und kann nicht mehr geändert werden.");

        assertThat(possibleTransitions.size(), is(1));
        assertThat(possibleTransitions.get(0), is(expected));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-nutzer" })
    void possbileTransitionsAngelegtAndRoleNutzer()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }

    @Test
    @Transactional
    @MockCustomUser
    void possbileTransitionsOffenAndRoleAdmin()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        List<TransitionModel> expected = new ArrayList<>();

        TransitionModel firstPossibleTransition = new TransitionModel();
        firstPossibleTransition.setIndex(2);
        firstPossibleTransition.setButtonName("IN BEARBEITUNG SETZEN");
        firstPossibleTransition.setUrl("in-bearbeitung-setzen");
        firstPossibleTransition.setDialogText("Die Abfrage wird nun in Bearbeitung gesetzt.");
        expected.add(firstPossibleTransition);

        TransitionModel secondPossibleTransition = new TransitionModel();
        secondPossibleTransition.setIndex(3);
        secondPossibleTransition.setButtonName("STORNIEREN");
        secondPossibleTransition.setUrl("abbrechen");
        secondPossibleTransition.setDialogText("Die Abfrage wird abbgebrochen.");

        expected.add(secondPossibleTransition);

        TransitionModel thirdPossibleTransition = new TransitionModel();
        thirdPossibleTransition.setIndex(4);
        thirdPossibleTransition.setButtonName("ZURÜCK AN ABFRAGEERSTELLER");
        thirdPossibleTransition.setUrl("zurueck-an-abfrageersteller");
        thirdPossibleTransition.setDialogText("Die Abfrage wird an den Abfrageersteller zurückgegeben.");

        expected.add(thirdPossibleTransition);

        assertThat(possibleTransitions.size(), is(3));
        assertThat(possibleTransitions.get(0), is(firstPossibleTransition));
        assertThat(possibleTransitions.get(1), is(secondPossibleTransition));
        assertThat(possibleTransitions.get(2), is(thirdPossibleTransition));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-sachbearbeiter_kita_schule_PLAN" })
    void possbileTransitionsOffenAndRoleSachbearbeiter()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        List<TransitionModel> expected = new ArrayList<>();

        TransitionModel firstPossibleTransition = new TransitionModel();
        firstPossibleTransition.setIndex(2);
        firstPossibleTransition.setButtonName("IN BEARBEITUNG SETZEN");
        firstPossibleTransition.setUrl("in-bearbeitung-setzen");
        firstPossibleTransition.setDialogText("Die Abfrage wird nun in Bearbeitung gesetzt.");

        expected.add(firstPossibleTransition);

        TransitionModel secondPossibleTransition = new TransitionModel();
        secondPossibleTransition.setIndex(3);
        secondPossibleTransition.setButtonName("STORNIEREN");
        secondPossibleTransition.setUrl("abbrechen");
        secondPossibleTransition.setDialogText("Die Abfrage wird abbgebrochen.");

        expected.add(secondPossibleTransition);

        TransitionModel thirdPossibleTransition = new TransitionModel();
        thirdPossibleTransition.setIndex(4);
        thirdPossibleTransition.setButtonName("ZURÜCK AN ABFRAGEERSTELLER");
        thirdPossibleTransition.setUrl("zurueck-an-abfrageersteller");
        thirdPossibleTransition.setDialogText("Die Abfrage wird an den Abfrageersteller zurückgegeben.");

        expected.add(thirdPossibleTransition);

        assertThat(possibleTransitions.size(), is(3));
        assertThat(possibleTransitions.get(0), is(firstPossibleTransition));
        assertThat(possibleTransitions.get(1), is(secondPossibleTransition));
        assertThat(possibleTransitions.get(2), is(thirdPossibleTransition));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-abfrageersteller" })
    void possbileTransitionsOffenAndRoleAbfrageersteller()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-nutzer" })
    void possbileTransitionsOffenAndRoleNutzer()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }

    @Test
    @Transactional
    @MockCustomUser
    void possbileTransitionsBearbeitungPlanAndRoleAdmin()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        List<TransitionModel> expected = new ArrayList<>();

        TransitionModel firstPossibleTransition = new TransitionModel();
        firstPossibleTransition.setIndex(3);
        firstPossibleTransition.setButtonName("STORNIEREN");
        firstPossibleTransition.setUrl("abbrechen");
        firstPossibleTransition.setDialogText("Die Abfrage wird abbgebrochen.");

        expected.add(firstPossibleTransition);

        TransitionModel secondPossibleTransition = new TransitionModel();
        secondPossibleTransition.setIndex(4);
        secondPossibleTransition.setButtonName("ZURÜCK AN ABFRAGEERSTELLER");
        secondPossibleTransition.setUrl("zurueck-an-abfrageersteller");
        secondPossibleTransition.setDialogText("Die Abfrage wird an den Abfrageersteller zurückgegeben.");

        expected.add(secondPossibleTransition);

        TransitionModel thirdPossibleTransition = new TransitionModel();
        thirdPossibleTransition.setIndex(5);
        thirdPossibleTransition.setButtonName("ABFRAGE SCHLIEßEN");
        thirdPossibleTransition.setUrl("abfrage-schliessen");
        thirdPossibleTransition.setDialogText("Die Abfrage wird erfolgreich geschlossen.");

        expected.add(thirdPossibleTransition);

        TransitionModel fourthPossibleTransition = new TransitionModel();
        fourthPossibleTransition.setIndex(6);
        fourthPossibleTransition.setButtonName("AN FACHREFERATE");
        fourthPossibleTransition.setUrl("verschicken-der-stellungnahme");
        fourthPossibleTransition.setDialogText("Die Abfrage wird an die Fachreferate weitergeleitet.");

        expected.add(fourthPossibleTransition);

        assertThat(possibleTransitions.size(), is(4));
        assertThat(possibleTransitions.get(0), is(firstPossibleTransition));
        assertThat(possibleTransitions.get(1), is(secondPossibleTransition));
        assertThat(possibleTransitions.get(2), is(thirdPossibleTransition));
        assertThat(possibleTransitions.get(3), is(fourthPossibleTransition));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-sachbearbeiter_kita_schule_PLAN" })
    void possbileTransitionsBearbeitungPlanAndRoleSachbearbeiter()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        List<TransitionModel> expected = new ArrayList<>();

        TransitionModel firstPossibleTransition = new TransitionModel();
        firstPossibleTransition.setIndex(3);
        firstPossibleTransition.setButtonName("STORNIEREN");
        firstPossibleTransition.setUrl("abbrechen");
        firstPossibleTransition.setDialogText("Die Abfrage wird abbgebrochen.");

        expected.add(firstPossibleTransition);

        TransitionModel secondPossibleTransition = new TransitionModel();
        secondPossibleTransition.setIndex(4);
        secondPossibleTransition.setButtonName("ZURÜCK AN ABFRAGEERSTELLER");
        secondPossibleTransition.setUrl("zurueck-an-abfrageersteller");
        secondPossibleTransition.setDialogText("Die Abfrage wird an den Abfrageersteller zurückgegeben.");

        expected.add(secondPossibleTransition);

        TransitionModel thirdPossibleTransition = new TransitionModel();
        thirdPossibleTransition.setIndex(5);
        thirdPossibleTransition.setButtonName("ABFRAGE SCHLIEßEN");
        thirdPossibleTransition.setUrl("abfrage-schliessen");
        thirdPossibleTransition.setDialogText("Die Abfrage wird erfolgreich geschlossen.");

        expected.add(thirdPossibleTransition);

        TransitionModel fourthPossibleTransition = new TransitionModel();
        fourthPossibleTransition.setIndex(6);
        fourthPossibleTransition.setButtonName("AN FACHREFERATE");
        fourthPossibleTransition.setUrl("verschicken-der-stellungnahme");
        fourthPossibleTransition.setDialogText("Die Abfrage wird an die Fachreferate weitergeleitet.");

        expected.add(fourthPossibleTransition);

        assertThat(possibleTransitions.size(), is(4));
        assertThat(possibleTransitions.get(0), is(firstPossibleTransition));
        assertThat(possibleTransitions.get(1), is(secondPossibleTransition));
        assertThat(possibleTransitions.get(2), is(thirdPossibleTransition));
        assertThat(possibleTransitions.get(3), is(fourthPossibleTransition));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-abfrageersteller" })
    void possbileTransitionsBearbeitungPlanAndRoleAbfrageersteller()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-nutzer" })
    void possbileTransitionsBearbeitungPlanAndRoleNutzer()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }

    @Test
    @Transactional
    @MockCustomUser
    void possbileTransitionsBearbeitungFachreferateAndRoleAdmin()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        List<TransitionModel> expected = new ArrayList<>();

        TransitionModel firstPossibleTransition = new TransitionModel();
        firstPossibleTransition.setIndex(3);
        firstPossibleTransition.setButtonName("STORNIEREN");
        firstPossibleTransition.setUrl("abbrechen");
        firstPossibleTransition.setDialogText("Die Abfrage wird abbgebrochen.");

        expected.add(firstPossibleTransition);

        TransitionModel secondPossibleTransition = new TransitionModel();
        secondPossibleTransition.setIndex(7);
        secondPossibleTransition.setButtonName("ZURÜCK AN PLAN");
        secondPossibleTransition.setUrl("zurueck-an-plan");
        secondPossibleTransition.setDialogText("Die Abfrage wird zu PLAN weitergeleitet.");

        expected.add(secondPossibleTransition);

        TransitionModel thirdPossibleTransition = new TransitionModel();
        thirdPossibleTransition.setIndex(8);
        thirdPossibleTransition.setButtonName("BEDARF MELDEN");
        thirdPossibleTransition.setUrl("bedarfsmeldung-erfolgt");
        thirdPossibleTransition.setDialogText("Die Bedarfsmeldung der Abfrage ist erfolgreich.");

        expected.add(thirdPossibleTransition);

        assertThat(possibleTransitions.size(), is(3));
        assertThat(possibleTransitions.get(0), is(firstPossibleTransition));
        assertThat(possibleTransitions.get(1), is(secondPossibleTransition));
        assertThat(possibleTransitions.get(2), is(thirdPossibleTransition));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-sachbearbeiter_kita_schule_PLAN" })
    void possbileTransitionsBearbeitungFachreferateAndRoleSachbearbeiter()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        List<TransitionModel> expected = new ArrayList<>();

        TransitionModel firstPossibleTransition = new TransitionModel();
        firstPossibleTransition.setIndex(3);
        firstPossibleTransition.setButtonName("STORNIEREN");
        firstPossibleTransition.setUrl("abbrechen");
        firstPossibleTransition.setDialogText("Die Abfrage wird abbgebrochen.");

        expected.add(firstPossibleTransition);

        TransitionModel secondPossibleTransition = new TransitionModel();
        secondPossibleTransition.setIndex(7);
        secondPossibleTransition.setButtonName("ZURÜCK AN PLAN");
        secondPossibleTransition.setUrl("zurueck-an-plan");
        secondPossibleTransition.setDialogText("Die Abfrage wird zu PLAN weitergeleitet.");
        expected.add(secondPossibleTransition);

        TransitionModel thirdPossibleTransition = new TransitionModel();
        thirdPossibleTransition.setIndex(8);
        thirdPossibleTransition.setButtonName("BEDARF MELDEN");
        thirdPossibleTransition.setUrl("bedarfsmeldung-erfolgt");
        thirdPossibleTransition.setDialogText("Die Bedarfsmeldung der Abfrage ist erfolgreich.");

        expected.add(thirdPossibleTransition);

        assertThat(possibleTransitions.size(), is(3));
        assertThat(possibleTransitions.get(0), is(firstPossibleTransition));
        assertThat(possibleTransitions.get(1), is(secondPossibleTransition));
        assertThat(possibleTransitions.get(2), is(thirdPossibleTransition));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-abfrageersteller" })
    void possbileTransitionsBearbeitungFachreferateAndRoleAbfrageersteller()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-nutzer" })
    void possbileTransitionsBearbeitungFachreferateAndRoleNutzer()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }

    @Test
    @Transactional
    @MockCustomUser
    void possbileTransitionsBedarfsmeldungErfolgtAndRoleAdmin()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        List<TransitionModel> expected = new ArrayList<>();

        TransitionModel firstPossibleTransition = new TransitionModel();
        firstPossibleTransition.setIndex(3);
        firstPossibleTransition.setButtonName("STORNIEREN");
        firstPossibleTransition.setUrl("abbrechen");
        firstPossibleTransition.setDialogText("Die Abfrage wird abbgebrochen.");

        expected.add(firstPossibleTransition);

        TransitionModel secondPossibleTransition = new TransitionModel();
        secondPossibleTransition.setIndex(9);
        secondPossibleTransition.setButtonName("ABFRAGE ABSCHLIEßEN");
        secondPossibleTransition.setUrl("speicher-von-soz-infrastruktur-versorgung");
        secondPossibleTransition.setDialogText("Die Abfrage wird erfolgreich geschlossen.");

        expected.add(secondPossibleTransition);

        assertThat(possibleTransitions.size(), is(2));
        assertThat(possibleTransitions.get(0), is(firstPossibleTransition));
        assertThat(possibleTransitions.get(1), is(secondPossibleTransition));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-sachbearbeiter_kita_schule_PLAN" })
    void possbileTransitionsBedarfsmeldungErfolgtAndRoleSachbearbeiter()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        List<TransitionModel> expected = new ArrayList<>();

        TransitionModel firstPossibleTransition = new TransitionModel();
        firstPossibleTransition.setIndex(3);
        firstPossibleTransition.setButtonName("STORNIEREN");
        firstPossibleTransition.setUrl("abbrechen");
        firstPossibleTransition.setDialogText("Die Abfrage wird abbgebrochen.");

        expected.add(firstPossibleTransition);

        assertThat(possibleTransitions.size(), is(1));
        assertThat(possibleTransitions.get(0), is(firstPossibleTransition));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-abfrageersteller" })
    void possbileTransitionsBedarfsmeldungErfolgtAndRoleAbfrageersteller()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        List<TransitionModel> expected = new ArrayList<>();

        TransitionModel firstPossibleTransition = new TransitionModel();
        firstPossibleTransition.setIndex(9);
        firstPossibleTransition.setButtonName("ABFRAGE ABSCHLIEßEN");
        firstPossibleTransition.setUrl("speicher-von-soz-infrastruktur-versorgung");
        firstPossibleTransition.setDialogText("Die Abfrage wird erfolgreich geschlossen.");

        expected.add(firstPossibleTransition);

        assertThat(possibleTransitions.size(), is(1));
        assertThat(possibleTransitions.get(0), is(firstPossibleTransition));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-nutzer" })
    void possbileTransitionsBedarfsmeldungErfolgtAndRoleNutzer()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }

    @Test
    @Transactional
    @MockCustomUser
    void possbileTransitionsErledigtRoleAdmin()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        List<TransitionModel> expected = new ArrayList<>();

        TransitionModel firstPossibleTransition = new TransitionModel();
        firstPossibleTransition.setIndex(10);
        firstPossibleTransition.setButtonName("ERNEUTE BEARBEITUNG");
        firstPossibleTransition.setUrl("erneute-bearbeitung");
        firstPossibleTransition.setDialogText("Die Abfrage wird wird an PLAN zur Bearbeitung zurückgesendet.");
        expected.add(firstPossibleTransition);

        assertThat(possibleTransitions.size(), is(1));
        assertThat(possibleTransitions.get(0), is(firstPossibleTransition));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-sachbearbeiter_kita_schule_PLAN" })
    void possbileTransitionsErledigtRoleSachbearbeiter()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        List<TransitionModel> expected = new ArrayList<>();

        TransitionModel firstPossibleTransition = new TransitionModel();
        firstPossibleTransition.setIndex(10);
        firstPossibleTransition.setButtonName("ERNEUTE BEARBEITUNG");
        firstPossibleTransition.setUrl("erneute-bearbeitung");
        firstPossibleTransition.setDialogText("Die Abfrage wird wird an PLAN zur Bearbeitung zurückgesendet.");
        expected.add(firstPossibleTransition);

        assertThat(possibleTransitions.size(), is(1));
        assertThat(possibleTransitions.get(0), is(firstPossibleTransition));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-abfrageersteller" })
    void possbileTransitionsErledigtRoleAbfrageersteller()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }

    @Test
    @Transactional
    @MockCustomUser(roles = { "lhm-isi-nutzer" })
    void possbileTransitionsErledigtRoleNutzer()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }

    @Test
    @Transactional
    @MockCustomUser
    void possbileTransitionsAbbruch()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();
        List<TransitionModel> possibleTransitions =
            this.abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndAuthorities(uuid);

        assertThat(possibleTransitions.size(), is(0));
    }
}
