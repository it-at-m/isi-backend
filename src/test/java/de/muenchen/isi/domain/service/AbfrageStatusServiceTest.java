package de.muenchen.isi.domain.service;

import de.muenchen.isi.IsiBackendApplication;
import de.muenchen.isi.TestConstants;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapperImpl;
import de.muenchen.isi.domain.mapper.AbfragevarianteDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
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

    @MockBean
    private AbfrageService abfrageService;

    @Autowired
    private AbfrageStatusService abfrageStatusService;

    private final AbfrageDomainMapper abfrageDomainMapper = new AbfrageDomainMapperImpl(
            new AbfragevarianteDomainMapperImpl(
                    new BauabschnittDomainMapperImpl()
            )
    );

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
    void statusAenderungEntityNotFoundExcpetion() throws EntityNotFoundException {
        final var uuid = UUID.randomUUID();
        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenThrow(new EntityNotFoundException("test"));

        Assertions.assertThrows(EntityNotFoundException.class, () -> abfrageStatusService.freigabeAbfrage(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> abfrageStatusService.abbrechenAbfrage(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> abfrageStatusService.angabenAnpassenAbfrage(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> abfrageStatusService.speichernDerVarianten(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> abfrageStatusService.keineBearbeitungNoetig(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> abfrageStatusService.verschickenDerStellungnahme(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> abfrageStatusService.bedarfsmeldungErfolgt(uuid));
        Assertions.assertThrows(EntityNotFoundException.class, () -> abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));
    }

    @Test
    void freigabeInfrasturkturabfrageVonAngelegt() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.freigabeAbfrage(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.OFFEN));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.freigabeAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.freigabeAbfrage(uuid));
    }

    @Test
    void abbrechenAbfrageVonOffen() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.abbrechenAbfrage(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);

        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.abbrechenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.abbrechenAbfrage(uuid));
    }

    @Test
    void abbrechenAbfrageVonInErfassung() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.abbrechenAbfrage(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);

        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.abbrechenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.abbrechenAbfrage(uuid));
    }

    @Test
    void abbrechenAbfrageVonInBearbeitungPlan() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.abbrechenAbfrage(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.abbrechenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.abbrechenAbfrage(uuid));
    }

    @Test
    void abbrechenAbfrageVonInBearbeitungFachreferate() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.abbrechenAbfrage(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.abbrechenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.abbrechenAbfrage(uuid));
    }

    @Test
    void abbrechenAbfrageVonBedarfsmeldungErfolgt() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.abbrechenAbfrage(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ABBRUCH));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.abbrechenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.abbrechenAbfrage(uuid));
    }

    @Test
    void angabenAnpassenAbfrageVonInErfassung() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.angabenAnpassenAbfrage(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.angabenAnpassenAbfrage(uuid));
    }

    @Test
    void angabenAnpassenAbfrageVonOffen() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.angabenAnpassenAbfrage(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ANGELEGT));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.angabenAnpassenAbfrage(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.angabenAnpassenAbfrage(uuid));
    }

    @Test
    void weitereAbfragevariantenAnlegenVonInBearbeitungPlan() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));
    }

    @Test
    void weitereAbfragevariantenAnlegenVonInBearbeitungFachreferate() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));
    }

    @Test
    void weitereAbfragevariantenAnlegenVonBedarfsmeldungErfolgt() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));
    }

    @Test
    void weitereAbfragevariantenAnlegenErledigtVonErledigt() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.weitereAbfragevariantenAnlegen(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.weitereAbfragevariantenAnlegen(uuid));
    }

    @Test
    void keineZusaetzlicheAbfragevarianteVonOffen() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineZusaetzlicheAbfragevariante(uuid));
    }

    @Test
    void zusaetzlicheAbfragevarianteAnlegenVonOffen() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_ERFASSUNG));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(uuid));
    }
    @Test
    void speichernDerVariantenVonInErfassung() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.speichernDerVarianten(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_PLAN));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernDerVarianten(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernDerVarianten(uuid));
    }

    @Test
    void keineBearbeitungNoetigVonInBearbeitungPlan() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.keineBearbeitungNoetig(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineBearbeitungNoetig(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.keineBearbeitungNoetig(uuid));
    }

    @Test
    void verschickenDerStellungnahmeVonInBearbeitungPlan() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.verschickenDerStellungnahme(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.verschickenDerStellungnahme(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.verschickenDerStellungnahme(uuid));
    }

    @Test
    void bedarfsmeldungErfolgtVonInBearbeitungFachreferate() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.bedarfsmeldungErfolgt(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.bedarfsmeldungErfolgt(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.bedarfsmeldungErfolgt(uuid));
    }

    @Test
    void speichernVonSozialinfrastrukturVersorgungVonBedarfsmeldungErfolgt() throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(uuid);
        abfrage.setAbfrage(new AbfrageModel());
        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.BEDARFSMELDUNG_ERFOLGT);

        Mockito.when(abfrageService.getInfrastrukturabfrageById(uuid)).thenReturn(abfrage);

        ArgumentCaptor<InfrastrukturabfrageModel> captor = ArgumentCaptor.forClass(InfrastrukturabfrageModel.class);

        abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid);

        Mockito.verify(abfrageService).updateInfrastrukturabfrage(captor.capture());

        InfrastrukturabfrageModel saved = captor.getValue();

        Mockito.verify(abfrageService, Mockito.times(2)).getInfrastrukturabfrageById(uuid);
        Mockito.verify(abfrageService, Mockito.times(1)).updateInfrastrukturabfrage(abfrage);
        assertThat(saved.getAbfrage().getStatusAbfrage(), is(StatusAbfrage.ERLEDIGT));


        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_ERFASSUNG);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_PLAN);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ERLEDIGT);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));

        abfrage.getAbfrage().setStatusAbfrage(StatusAbfrage.ABBRUCH);
        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(uuid));
    }
}
