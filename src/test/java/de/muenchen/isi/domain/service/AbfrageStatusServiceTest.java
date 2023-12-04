package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.TestData;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.StringLengthExceededException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.service.transition.MockCustomUser;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
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
    @MockCustomUser
    void freigabeInfrasturkturabfrageVonAngelegt()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.freigabeInfrasturkturabfrageVonAngelegt(TestData.createBauleitplanverfahrenModel());
        this.freigabeInfrasturkturabfrageVonAngelegt(TestData.createBaugenehmigungsverfahrenModel());
    }

    void freigabeInfrasturkturabfrageVonAngelegt(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void abbrechenAbfrageVonOffen()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.abbrechenAbfrageVonOffen(TestData.createBauleitplanverfahrenModel());
        this.abbrechenAbfrageVonOffen(TestData.createBaugenehmigungsverfahrenModel());
    }

    void abbrechenAbfrageVonOffen(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void inBearbeitungSetztVonOffen()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.inBearbeitungSetztVonOffen(TestData.createBauleitplanverfahrenModel());
        this.inBearbeitungSetztVonOffen(TestData.createBaugenehmigungsverfahrenModel());
    }

    void inBearbeitungSetztVonOffen(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void abbrechenAbfrageVonInBearbeitungSachbearbeitung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.abbrechenAbfrageVonInBearbeitungSachbearbeitung(TestData.createBauleitplanverfahrenModel());
        this.abbrechenAbfrageVonInBearbeitungSachbearbeitung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void abbrechenAbfrageVonInBearbeitungSachbearbeitung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void abbrechenAbfrageVonInBearbeitungFachreferate()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.abbrechenAbfrageVonInBearbeitungFachreferate(TestData.createBauleitplanverfahrenModel());
        this.abbrechenAbfrageVonInBearbeitungFachreferate(TestData.createBaugenehmigungsverfahrenModel());
    }

    void abbrechenAbfrageVonInBearbeitungFachreferate(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void abbrechenAbfrageVonBedarfsmeldungErfolgt()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.abbrechenAbfrageVonBedarfsmeldungErfolgt(TestData.createBauleitplanverfahrenModel());
        this.abbrechenAbfrageVonBedarfsmeldungErfolgt(TestData.createBaugenehmigungsverfahrenModel());
    }

    void abbrechenAbfrageVonBedarfsmeldungErfolgt(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void zurueckAnAbfrageerstellungVonInBearbeitungSachbearbeitung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.zurueckAnAbfrageerstellungVonInBearbeitungSachbearbeitung(TestData.createBauleitplanverfahrenModel());
        this.zurueckAnAbfrageerstellungVonInBearbeitungSachbearbeitung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void zurueckAnAbfrageerstellungVonInBearbeitungSachbearbeitung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void zurueckAnAbfrageerstellungVonOffen()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.zurueckAnAbfrageerstellungVonOffen(TestData.createBauleitplanverfahrenModel());
        this.zurueckAnAbfrageerstellungVonOffen(TestData.createBaugenehmigungsverfahrenModel());
    }

    void zurueckAnAbfrageerstellungVonOffen(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void zurueckAnSachbearbeitungVonInBearbeitungFachreferate()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.zurueckAnSachbearbeitungVonInBearbeitungFachreferate(TestData.createBauleitplanverfahrenModel());
        this.zurueckAnSachbearbeitungVonInBearbeitungFachreferate(TestData.createBaugenehmigungsverfahrenModel());
    }

    void zurueckAnSachbearbeitungVonInBearbeitungFachreferate(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void erledigtOhneFachreferatVonInBearbeitungSachbearbeitung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.erledigtOhneFachreferatVonInBearbeitungSachbearbeitung(TestData.createBauleitplanverfahrenModel());
        this.erledigtOhneFachreferatVonInBearbeitungSachbearbeitung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void erledigtOhneFachreferatVonInBearbeitungSachbearbeitung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void verschickenDerStellungnahmeVonInBearbeitungSachbearbeitung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.verschickenDerStellungnahmeVonInBearbeitungSachbearbeitung(TestData.createBauleitplanverfahrenModel());
        this.verschickenDerStellungnahmeVonInBearbeitungSachbearbeitung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void verschickenDerStellungnahmeVonInBearbeitungSachbearbeitung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void bedarfsmeldungErfolgtVonInBearbeitungFachreferate()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.bedarfsmeldungErfolgtVonInBearbeitungFachreferate(TestData.createBauleitplanverfahrenModel());
        this.bedarfsmeldungErfolgtVonInBearbeitungFachreferate(TestData.createBaugenehmigungsverfahrenModel());
    }

    void bedarfsmeldungErfolgtVonInBearbeitungFachreferate(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void erledigtMitFachreferatVonBedarfsmeldungErfolgt()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.erledigtMitFachreferatVonBedarfsmeldungErfolgt(TestData.createBauleitplanverfahrenModel());
        this.erledigtMitFachreferatVonBedarfsmeldungErfolgt(TestData.createBaugenehmigungsverfahrenModel());
    }

    void erledigtMitFachreferatVonBedarfsmeldungErfolgt(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void erneuteBearbeitungSachbearbeitungVonErledigtMitFachereferat()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.erneuteBearbeitungSachbearbeitungVonErledigtMitFachereferat(TestData.createBauleitplanverfahrenModel());
        this.erneuteBearbeitungSachbearbeitungVonErledigtMitFachereferat(
                TestData.createBaugenehmigungsverfahrenModel()
            );
    }

    void erneuteBearbeitungSachbearbeitungVonErledigtMitFachereferat(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void erneuteBearbeitungSachbearbeitungVonErledigtOhneFachereferat()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.erneuteBearbeitungSachbearbeitungVonErledigtOhneFachereferat(TestData.createBauleitplanverfahrenModel());
        this.erneuteBearbeitungSachbearbeitungVonErledigtOhneFachereferat(
                TestData.createBaugenehmigungsverfahrenModel()
            );
    }

    void erneuteBearbeitungSachbearbeitungVonErledigtOhneFachereferat(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void addAbfrageAnmerkungTest()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException {
        this.addAbfrageAnmerkungTest(TestData.createBauleitplanverfahrenModel());
        this.addAbfrageAnmerkungTest(TestData.createBaugenehmigungsverfahrenModel());
    }

    void addAbfrageAnmerkungTest(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, UserRoleNotAllowedException {
        final var anmerkung = "Test";
        AbfrageModel abfrage = abfrageToTest;
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
    @MockCustomUser
    void addAbfrageAnmerkungStringLengthExceededExceptionTest()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, UserRoleNotAllowedException {
        this.addAbfrageAnmerkungStringLengthExceededExceptionTest(TestData.createBauleitplanverfahrenModel());
        this.addAbfrageAnmerkungStringLengthExceededExceptionTest(TestData.createBaugenehmigungsverfahrenModel());
    }

    void addAbfrageAnmerkungStringLengthExceededExceptionTest(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, UserRoleNotAllowedException {
        final var anmerkung =
            "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestI";
        AbfrageModel abfrage = abfrageToTest;
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
