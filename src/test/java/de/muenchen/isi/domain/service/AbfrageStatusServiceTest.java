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
@SpringBootTest(classes = { IsiBackendApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
@MockitoSettings(strictness = Strictness.LENIENT)
class AbfrageStatusServiceTest {

    @Autowired
    private AbfrageAltService abfrageService;

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
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid, anmerkung)
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
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid, anmerkung)
        );
    }

    @Test
    @Transactional
    void freigabeInfrasturkturabfrageVonAngelegt()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.OFFEN, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(
                abfrage.getId(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG,
                anmerkung
            );
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.BEDARFSMELDUNG_ERFOLGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ABBRUCH, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonOffen()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.OFFEN, anmerkung);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ANGELEGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));
    }

    @Test
    @Transactional
    void inBearbeitungSetztVonOffen()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.OFFEN, anmerkung);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ANGELEGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonInBearbeitungSachbearbeitung()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(
                abfrage.getId(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG,
                anmerkung
            );

        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ANGELEGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonInBearbeitungFachreferate()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE, anmerkung);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ANGELEGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    void abbrechenAbfrageVonBedarfsmeldungErfolgt()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.BEDARFSMELDUNG_ERFOLGT, anmerkung);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ANGELEGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    void zurueckAnAbfrageerstellungVonInBearbeitungSachbearbeitung()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(
                abfrage.getId(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG,
                anmerkung
            );

        final var uuid = abfrage.getId();

        this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.BEDARFSMELDUNG_ERFOLGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ABBRUCH, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void zurueckAnAbfrageerstellungVonOffen()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.OFFEN, anmerkung);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.BEDARFSMELDUNG_ERFOLGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ABBRUCH, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void zurueckAnSachbearbeitungVonInBearbeitungFachreferate()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE, anmerkung);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung);
        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ABBRUCH, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void keineBearbeitungNoetigVonInBearbeitungSachbearbeitung()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(
                abfrage.getId(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG,
                anmerkung
            );

        final var uuid = abfrage.getId();

        this.abfrageStatusService.keineBearbeitungNoetig(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ANGELEGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.OFFEN, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.BEDARFSMELDUNG_ERFOLGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ABBRUCH, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void verschickenDerStellungnahmeVonInBearbeitungSachbearbeitung()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(
                abfrage.getId(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG,
                anmerkung
            );

        final var uuid = abfrage.getId();

        this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ANGELEGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.OFFEN, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.BEDARFSMELDUNG_ERFOLGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ABBRUCH, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void bedarfsmeldungErfolgtVonInBearbeitungFachreferate()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE, anmerkung);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ANGELEGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.OFFEN, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(
                abfrage.getId(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG,
                anmerkung
            );
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.BEDARFSMELDUNG_ERFOLGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ABBRUCH, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void speichernVonSozialinfrastrukturVersorgungVonBedarfsmeldungErfolgt()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.BEDARFSMELDUNG_ERFOLGT, anmerkung);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ANGELEGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.OFFEN, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(
                abfrage.getId(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG,
                anmerkung
            );
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ABBRUCH, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void erneuteBearbeitungVonErledigtMitFachereferat()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT, anmerkung);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ANGELEGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.OFFEN, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(
                abfrage.getId(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG,
                anmerkung
            );
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.BEDARFSMELDUNG_ERFOLGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ABBRUCH, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void erneuteBearbeitungVonErledigtOhneFachereferat()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT, anmerkung);

        final var uuid = abfrage.getId();

        this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ANGELEGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.OFFEN, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(
                abfrage.getId(),
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG,
                anmerkung
            );
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.BEDARFSMELDUNG_ERFOLGT, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));

        abfrage = this.abfrageService.getInfrastrukturabfrageById(uuid);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.ABBRUCH, anmerkung);
        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.abfrageStatusService.erneuteBearbeitenAbfrage(uuid, anmerkung)
        );
        saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    void addAbfrageAnmerkungTest()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung = "Test";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG, "");

        final var uuid = abfrage.getId();

        this.abfrageStatusService.keineBearbeitungNoetig(uuid, anmerkung);

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getAnmerkung(), is("Bitte die Abfrage zeitnah behandeln\nTest"));
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));
    }

    @Test
    @Transactional
    void addAbfrageAnmerkungStringLengthExceededExceptionTest()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException {
        final var anmerkung =
            "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestI";
        InfrastrukturabfrageModel abfrage = TestData.createInfrastrukturabfrageModel();
        abfrage = this.abfrageService.saveInfrastrukturabfrage(abfrage);
        this.abfrageService.changeStatusAbfrage(abfrage.getId(), StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG, "");

        final var uuid = abfrage.getId();

        Assertions.assertThrows(
            StringLengthExceededException.class,
            () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid, anmerkung)
        );

        InfrastrukturabfrageModel saved = this.abfrageService.getInfrastrukturabfrageById(uuid);
        assertThat(saved.getAbfrage().getAnmerkung(), is("Bitte die Abfrage zeitnah behandeln"));
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG));
    }
}
