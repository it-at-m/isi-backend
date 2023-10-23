package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.StringLengthExceededException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
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
@SpringBootTest(classes = { IsiBackendApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
class AbfrageStatusServiceTest {

    @Autowired
    private AbfrageService abfrageService;

    @Autowired
    private AbfrageStatusService abfrageStatusService;

    @Autowired
    private AbfrageRepository abfrageRepository;

    @BeforeEach
    public void beforeEach() {
        this.abfrageRepository.deleteAll();
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
        final var anmerkung = "";
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
    }

    @Test
    @Transactional
    void freigabeInfrasturkturabfrageVonAngelegt()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));
        final var uuid = abfrage.getId();

        this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonOffen()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));
    }

    @Test
    @Transactional
    void inBearbeitungSetztVonOffen()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonInBearbeitungSachbearbeitung()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonInBearbeitungFachreferate()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonBedarfsmeldungErfolgt()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    void zurueckAnAbfrageerstellungVonInBearbeitungSachbearbeitung()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void zurueckAnAbfrageerstellungVonOffen()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void zurueckAnSachbearbeitungVonInBearbeitungFachreferate()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void erledigtOhneFachreferatVonInBearbeitungSachbearbeitung()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void verschickenDerStellungnahmeVonInBearbeitungSachbearbeitung()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void bedarfsmeldungErfolgtVonInBearbeitungFachreferate()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void erledigtMitFachreferatVonBedarfsmeldungErfolgt()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void erneuteBearbeitungSachbearbeitungVonErledigtMitFachereferat()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void erneuteBearbeitungSachbearbeitungVonErledigtOhneFachereferat()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void addAbfrageAnmerkungTest()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "Test";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getAnmerkung(), is("Bitte die Abfrage zeitnah behandeln\nTest"));
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));
    }

    @Test
    @Transactional
    void addAbfrageAnmerkungStringLengthExceededExceptionTest()
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        final var anmerkung =
            "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestI";
        AbfrageModel abfrage = TestData.createBauleitplanverfahrenModel();
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        Assertions.assertThrows(
            StringLengthExceededException.class,
            () -> this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getAnmerkung(), is("Bitte die Abfrage zeitnah behandeln"));
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));
    }
}
