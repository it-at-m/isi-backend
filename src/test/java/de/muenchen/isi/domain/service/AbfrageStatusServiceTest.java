package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.TestData;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.exception.StringLengthExceededException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.service.calculation.CalculationService;
import de.muenchen.isi.domain.service.email.SendWorkAssignmentInformationService;
import de.muenchen.isi.domain.service.etlInterface.EtlInterfaceService;
import de.muenchen.isi.domain.service.reporting.ReportingdataTransferService;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @MockitoBean
    private CalculationService calculationService;

    @MockitoBean
    private ReportingdataTransferService reportingdataTransferService;

    @MockitoBean
    private EtlInterfaceService etlInterfaceService;

    @MockitoBean
    private SendWorkAssignmentInformationService sendWorkAssignmentInformationService;

    @Test
    void getAbfrageIdHeaderSuccessfull() throws EntityNotFoundException {
        final var uuid = UUID.randomUUID();
        final Message<StatusAbfrageEvents> message = MessageBuilder.withPayload(StatusAbfrageEvents.FREIGABE)
            .setHeader("abfrage_id", uuid)
            .build();

        final var uuuidExpected = this.abfrageStatusService.getAbfrageId(message.getHeaders());

        assertThat(uuid, is(uuuidExpected));
    }

    @Test
    void getAbfrageIdHeaderEntityNotFoundException() {
        final var uuid = UUID.randomUUID();
        final Message<StatusAbfrageEvents> message = MessageBuilder.withPayload(StatusAbfrageEvents.FREIGABE)
            .setHeader("abfrageid", uuid)
            .build();
        Assertions.assertThrows(EntityNotFoundException.class, () ->
            this.abfrageStatusService.getAbfrageId(message.getHeaders())
        );
    }

    @Test
    void statusAenderungEntityNotFoundExcpetion() {
        final var uuid = UUID.randomUUID();
        final var anmerkung = "";
        Assertions.assertThrows(EntityNotFoundException.class, () ->
            this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        Assertions.assertThrows(EntityNotFoundException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        Assertions.assertThrows(EntityNotFoundException.class, () ->
            this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        Assertions.assertThrows(EntityNotFoundException.class, () ->
            this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        Assertions.assertThrows(EntityNotFoundException.class, () ->
            this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        Assertions.assertThrows(EntityNotFoundException.class, () ->
            this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        Assertions.assertThrows(EntityNotFoundException.class, () ->
            this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
    }

    @Test
    @Transactional
    @MockCustomUser
    void freigabeInfrasturkturabfrageVonAngelegt()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.freigabeInfrasturkturabfrageVonAngelegt(TestData.createBauleitplanverfahrenModel());
        this.freigabeInfrasturkturabfrageVonAngelegt(TestData.createBaugenehmigungsverfahrenModel());
    }

    void freigabeInfrasturkturabfrageVonAngelegt(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));
        final var uuid = abfrage.getId();

        this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG));

        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.START_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPLANUNG_BEDARFE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPLANUNG_BEDARFE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.freigabeAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    @MockCustomUser
    void abbrechenAbfrageVonUebermitteltZurBearbeitung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.abbrechenAbfrageVonUebermitteltZurBearbeitung(TestData.createBauleitplanverfahrenModel());
        this.abbrechenAbfrageVonUebermitteltZurBearbeitung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void abbrechenAbfrageVonUebermitteltZurBearbeitung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));
    }

    @Test
    @Transactional
    @MockCustomUser
    void inBearbeitungSetztVonUebermitteltZurBearbeitung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.inBearbeitungSetztVonUebermitteltZurBearbeitung(TestData.createBauleitplanverfahrenModel());
        this.inBearbeitungSetztVonUebermitteltZurBearbeitung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void inBearbeitungSetztVonUebermitteltZurBearbeitung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.START_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.inBearbeitungSetzenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    @MockCustomUser
    void abbrechenAbfrageVonStartBearbeitung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.abbrechenAbfrageVonStartBearbeitung(TestData.createBauleitplanverfahrenModel());
        this.abbrechenAbfrageVonStartBearbeitung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void abbrechenAbfrageVonStartBearbeitung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    @MockCustomUser
    void abbrechenAbfrageVonEinpflegenBedarfsmeldung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.abbrechenAbfrageVonEinpflegenBedarfsmeldung(TestData.createBauleitplanverfahrenModel());
        this.abbrechenAbfrageVonEinpflegenBedarfsmeldung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void abbrechenAbfrageVonEinpflegenBedarfsmeldung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    @MockCustomUser
    void abbrechenAbfrageVonEinplanungBedarfe()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.abbrechenAbfrageVonEinplanungBedarfe(TestData.createBauleitplanverfahrenModel());
        this.abbrechenAbfrageVonEinplanungBedarfe(TestData.createBaugenehmigungsverfahrenModel());
    }

    void abbrechenAbfrageVonEinplanungBedarfe(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPLANUNG_BEDARFE);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.abbrechenAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));
    }

    @Test
    @Transactional
    @MockCustomUser
    void zurueckAnAbfrageerstellungVonStartBearbeitung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.zurueckAnAbfrageerstellungVonStartBearbeitung(TestData.createBauleitplanverfahrenModel());
        this.zurueckAnAbfrageerstellungVonStartBearbeitung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void zurueckAnAbfrageerstellungVonStartBearbeitung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPLANUNG_BEDARFE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPLANUNG_BEDARFE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    @MockCustomUser
    void zurueckAnAbfrageerstellungVonUebermitteltZurBearbeitung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.zurueckAnAbfrageerstellungVonUebermitteltZurBearbeitung(TestData.createBauleitplanverfahrenModel());
        this.zurueckAnAbfrageerstellungVonUebermitteltZurBearbeitung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void zurueckAnAbfrageerstellungVonUebermitteltZurBearbeitung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPLANUNG_BEDARFE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPLANUNG_BEDARFE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    @MockCustomUser
    void zurueckAnSachbearbeitungVonEinpflegenBedarfsmeldung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.zurueckAnSachbearbeitungVonEinpflegenBedarfsmeldung(TestData.createBauleitplanverfahrenModel());
        this.zurueckAnSachbearbeitungVonEinpflegenBedarfsmeldung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void zurueckAnSachbearbeitungVonEinpflegenBedarfsmeldung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.START_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    @MockCustomUser
    void erledigtOhneFachreferatVonStartBearbeitung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.erledigtOhneFachreferatVonStartBearbeitung(TestData.createBauleitplanverfahrenModel());
        this.erledigtOhneFachreferatVonStartBearbeitung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void erledigtOhneFachreferatVonStartBearbeitung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPLANUNG_BEDARFE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPLANUNG_BEDARFE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    @MockCustomUser
    void verschickenDerStellungnahmeVonStartBearbeitung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.verschickenDerStellungnahmeVonStartBearbeitung(TestData.createBauleitplanverfahrenModel());
        this.verschickenDerStellungnahmeVonStartBearbeitung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void verschickenDerStellungnahmeVonStartBearbeitung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPLANUNG_BEDARFE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPLANUNG_BEDARFE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.verschickenDerStellungnahme(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    @MockCustomUser
    void bedarfsmeldungErfolgtVonEinpflegenBedarfsmeldung()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.bedarfsmeldungErfolgtVonEinpflegenBedarfsmeldung(TestData.createBauleitplanverfahrenModel());
        this.bedarfsmeldungErfolgtVonEinpflegenBedarfsmeldung(TestData.createBaugenehmigungsverfahrenModel());
    }

    void bedarfsmeldungErfolgtVonEinpflegenBedarfsmeldung(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPLANUNG_BEDARFE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.START_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPLANUNG_BEDARFE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPLANUNG_BEDARFE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.bedarfsmeldungErfolgt(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    @MockCustomUser
    void erledigtMitFachreferatVonEinplanungBedarfe()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.erledigtMitFachreferatVonEinplanungBedarfe(TestData.createBauleitplanverfahrenModel());
        this.erledigtMitFachreferatVonEinplanungBedarfe(TestData.createBaugenehmigungsverfahrenModel());
    }

    void erledigtMitFachreferatVonEinplanungBedarfe(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPLANUNG_BEDARFE);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.START_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_MIT_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    @MockCustomUser
    void erneuteBearbeitungSachbearbeitungVonErledigtMitFachereferat()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.erneuteBearbeitungSachbearbeitungVonErledigtMitFachereferat(TestData.createBauleitplanverfahrenModel());
        this.erneuteBearbeitungSachbearbeitungVonErledigtMitFachereferat(
                TestData.createBaugenehmigungsverfahrenModel()
            );
    }

    void erneuteBearbeitungSachbearbeitungVonErledigtMitFachereferat(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.START_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.START_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPLANUNG_BEDARFE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPLANUNG_BEDARFE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    @MockCustomUser
    void erneuteBearbeitungSachbearbeitungVonErledigtOhneFachereferat()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.erneuteBearbeitungSachbearbeitungVonErledigtOhneFachereferat(TestData.createBauleitplanverfahrenModel());
        this.erneuteBearbeitungSachbearbeitungVonErledigtOhneFachereferat(
                TestData.createBaugenehmigungsverfahrenModel()
            );
    }

    void erneuteBearbeitungSachbearbeitungVonErledigtOhneFachereferat(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.ERLEDIGT_OHNE_FACHREFERAT);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung);

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.START_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.UEBERMITTELT_ZUR_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erledigtMitFachreferat(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.START_BEARBEITUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPFLEGEN_BEDARFSMELDUNG));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.EINPLANUNG_BEDARFE);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.EINPLANUNG_BEDARFE));

        abfrage = this.abfrageService.getById(uuid);
        abfrage.setStatusAbfrage(StatusAbfrage.ABBRUCH);
        this.abfrageService.save(abfrage);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () ->
            this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(uuid, anmerkung)
        );
        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));
    }

    @Test
    @Transactional
    @MockCustomUser
    void addAbfrageAnmerkungTest()
        throws UniqueViolationException, OptimisticLockingException, StringLengthExceededException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.addAbfrageAnmerkungTest(TestData.createBauleitplanverfahrenModel());
        this.addAbfrageAnmerkungTest(TestData.createBaugenehmigungsverfahrenModel());
    }

    void addAbfrageAnmerkungTest(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException, StringLengthExceededException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = "Test";
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
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
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, CalculationException, ReportingException, UserRoleNotAllowedException {
        this.addAbfrageAnmerkungStringLengthExceededExceptionTest(TestData.createBauleitplanverfahrenModel());
        this.addAbfrageAnmerkungStringLengthExceededExceptionTest(TestData.createBaugenehmigungsverfahrenModel());
    }

    void addAbfrageAnmerkungStringLengthExceededExceptionTest(final AbfrageModel abfrageToTest)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, CalculationException, ReportingException, UserRoleNotAllowedException {
        final var anmerkung = new String(new char[1000]).replace("\0", "A");
        AbfrageModel abfrage = abfrageToTest;
        abfrage = this.abfrageService.save(abfrage);
        abfrage.setStatusAbfrage(StatusAbfrage.START_BEARBEITUNG);
        abfrage = this.abfrageService.save(abfrage);
        final var uuid = abfrage.getId();

        Assertions.assertThrows(StringLengthExceededException.class, () ->
            this.abfrageStatusService.erledigtOhneFachreferat(uuid, anmerkung)
        );

        abfrage = this.abfrageService.getById(uuid);
        assertThat(abfrage.getAnmerkung(), is("Bitte die Abfrage zeitnah behandeln"));
        assertThat(abfrage.getStatusAbfrage(), is(StatusAbfrage.START_BEARBEITUNG));
    }
}
