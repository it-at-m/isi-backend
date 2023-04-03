package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import de.muenchen.isi.rest.TestData;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
    classes = { IsiBackendApplication.class },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = { "tomcat.gracefulshutdown.pre-wait-seconds=0" }
)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
class AbfrageStatusServiceTest {

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
    void getAbfrageIdHeaderSuccessfull() throws EntityNotFoundException {
        final var uuid = UUID.randomUUID();
        final Message<StatusAbfrageEvents> message = MessageBuilder
            .withPayload(StatusAbfrageEvents.FREIGABE)
            .setHeader("abfrage_id", uuid)
            .build();

        final var uuuidExpected = this.abfrageStatusService.getAbfrageId(message.getHeaders());

        assertThat(uuid, is(uuuidExpected));
    }

    @Test
    void getAbfrageIdHeaderEntityNotFoundException() {
        final var uuid = UUID.randomUUID();
        final Message<StatusAbfrageEvents> message = MessageBuilder
            .withPayload(StatusAbfrageEvents.FREIGABE)
            .setHeader("abfrageid", uuid)
            .build();
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.getAbfrageId(message.getHeaders())
        );
    }

    @Test
    void statusAenderungEntityNotFoundExcpetion() {
        final var uuid = UUID.randomUUID();

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.freigabeAbfrage(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.speichernDerVarianten(uuid)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid)
        );
    }

    @Test
    @Transactional
    void freigabeInfrasturkturabfrageVonAngelegt()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.freigabeAbfrage(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonOffen()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonInErfassung()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonInBearbeitungPlan()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonInBearbeitungFachreferate()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonBedarfsmeldungErfolgt()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));
    }

    @Test
    @Transactional
    void angabenAnpassenAbfrageVonInBearbeitungPlan()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.angabenAnpassenAbfrage(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void angabenAnpassenAbfrageVonInErfassung()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.angabenAnpassenAbfrage(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void angabenAnpassenAbfrageVonOffen()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.angabenAnpassenAbfrage(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void weitereAbfragevariantenAnlegenVonInBearbeitungPlan()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void weitereAbfragevariantenAnlegenVonInBearbeitungFachreferate()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void weitereAbfragevariantenAnlegenVonBedarfsmeldungErfolgt()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void weitereAbfragevariantenAnlegenErledigtVonErledigt()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void keineZusaetzlicheAbfragevarianteVonOffen()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void zusaetzlicheAbfragevarianteAnlegenVonOffen()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void speichernDerVariantenVonInErfassung()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.speichernDerVarianten(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernDerVarianten(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernDerVarianten(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernDerVarianten(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernDerVarianten(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernDerVarianten(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernDerVarianten(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernDerVarianten(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void keineBearbeitungNoetigVonInBearbeitungPlan()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.keineBearbeitungNoetig(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void verschickenDerStellungnahmeVonInBearbeitungPlan()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.verschickenDerStellungnahme(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void bedarfsmeldungErfolgtVonInBearbeitungFachreferate()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.bedarfsmeldungErfolgt(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void speichernVonSozialinfrastrukturVersorgungVonBedarfsmeldungErfolgt()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.updateInfrastrukturabfrageWithStatus(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }
}
