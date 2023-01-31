package de.muenchen.isi.domain.service;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapperImpl;
import de.muenchen.isi.domain.mapper.AbfragevarianteDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapperImpl;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrageEvents;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        classes = {IsiBackendApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"tomcat.gracefulshutdown.pre-wait-seconds=0"})
@ActiveProfiles(profiles = {TestConstants.SPRING_UNIT_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE})
@MockitoSettings(strictness = Strictness.LENIENT)
class AbfrageStatusServiceTest {

    private final AbfrageDomainMapper abfrageDomainMapper = new AbfrageDomainMapperImpl(
            new AbfragevarianteDomainMapperImpl(
                    new BauabschnittDomainMapperImpl()
            ),
            new DokumentDomainMapperImpl()
    );
    @MockBean
    private AbfrageService abfrageService;
    @Autowired
    private AbfrageStatusService abfrageStatusService;
    @Mock
    private InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    @AfterEach
    public void afterEach() {
        this.abfrageService = new AbfrageService(
                this.abfrageDomainMapper,
                this.infrastrukturabfrageRepository
        );
        Mockito.reset(this.infrastrukturabfrageRepository);
    }


    @Test
    void getAbfrageIdHeaderSuccessfull() throws EntityNotFoundException {
        final var uuid = UUID.randomUUID();
        final Message<StatusAbfrageEvents> message = MessageBuilder.withPayload(StatusAbfrageEvents.FREIGABE).setHeader("abfrage_id", uuid).build();

        final var uuuidExpected = this.abfrageStatusService.getAbfrageId(message);

        assertThat(uuid, is(uuuidExpected));
    }

    @Test
    void getAbfrageIdHeaderEntityNotFoundException() {
        final var uuid = UUID.randomUUID();
        final Message<StatusAbfrageEvents> message = MessageBuilder.withPayload(StatusAbfrageEvents.FREIGABE).setHeader("abfrageid", uuid).build();
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.getAbfrageId(message));
    }

    @Test
    void statusAenderungEntityNotFoundExcpetion() throws EntityNotFoundException {
        final var uuid = UUID.randomUUID();
        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenThrow(new EntityNotFoundException("test"));

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.freigabeAbfrage(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.speichernDerVarianten(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));
    }

    @Test
    void freigabeInfrasturkturabfrageVonAngelegt() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.freigabeAbfrage(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.freigabeAbfrage(uuid));
    }

    @Test
    void abbrechenAbfrageVonOffen() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.abbrechenAbfrage(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);

        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));
    }

    @Test
    void abbrechenAbfrageVonInErfassung() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.abbrechenAbfrage(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);

        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));
    }

    @Test
    void abbrechenAbfrageVonInBearbeitungPlan() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.abbrechenAbfrage(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));
    }

    @Test
    void abbrechenAbfrageVonInBearbeitungFachreferate() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.abbrechenAbfrage(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));
    }

    @Test
    void abbrechenAbfrageVonBedarfsmeldungErfolgt() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.abbrechenAbfrage(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.abbrechenAbfrage(uuid));
    }

    @Test
    void angabenAnpassenAbfrageVonInErfassung() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.angabenAnpassenAbfrage(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid));
    }

    @Test
    void angabenAnpassenAbfrageVonOffen() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.angabenAnpassenAbfrage(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.angabenAnpassenAbfrage(uuid));
    }

    @Test
    void weitereAbfragevariantenAnlegenVonInBearbeitungPlan() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));
    }

    @Test
    void weitereAbfragevariantenAnlegenVonInBearbeitungFachreferate() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));
    }

    @Test
    void weitereAbfragevariantenAnlegenVonBedarfsmeldungErfolgt() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));
    }

    @Test
    void weitereAbfragevariantenAnlegenErledigtVonErledigt() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));
    }

    @Test
    void keineZusaetzlicheAbfragevarianteVonOffen() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));
    }

    @Test
    void zusaetzlicheAbfragevarianteAnlegenVonOffen() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));
    }

    @Test
    void speichernDerVariantenVonInErfassung() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.speichernDerVarianten(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernDerVarianten(uuid));
    }

    @Test
    void keineBearbeitungNoetigVonInBearbeitungPlan() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.keineBearbeitungNoetig(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.keineBearbeitungNoetig(uuid));
    }

    @Test
    void verschickenDerStellungnahmeVonInBearbeitungPlan() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.verschickenDerStellungnahme(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.verschickenDerStellungnahme(uuid));
    }

    @Test
    void bedarfsmeldungErfolgtVonInBearbeitungFachreferate() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.bedarfsmeldungErfolgt(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.bedarfsmeldungErfolgt(uuid));
    }

    @Test
    void speichernVonSozialinfrastrukturVersorgungVonBedarfsmeldungErfolgt() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        final ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid);

        Mockito.verify(this.abfrageService).updateInfrastrukturabfrage(captor.capture());

        final InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(this.abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(this.abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));
    }
}
