package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapperImpl;
import de.muenchen.isi.domain.mapper.AbfragevarianteDomainMapper;
import de.muenchen.isi.domain.mapper.AbfragevarianteDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapper;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapperImpl;
import de.muenchen.isi.domain.mapper.DokumentDomainMapper;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.AbfragevarianteSachbearbeitungModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.BedarfsmeldungFachabteilungenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfragevarianteAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.InfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate.AbfragevarianteInBearbeitungFachreferateModel;
import de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate.InfrastrukturabfrageInBearbeitungFachreferateModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.AbfragevarianteInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteSachbearbeitung;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.BedarfsmeldungFachreferate;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration(
        classes = {
                AbfrageDomainMapperImpl.class,
                AbfragevarianteDomainMapperImpl.class,
                BauabschnittDomainMapperImpl.class,
                DokumentDomainMapperImpl.class,
        }
)
class AbfrageServiceTest {

    @Autowired
    AbfrageDomainMapper abfrageDomainMapper;

    @Autowired
    AbfragevarianteDomainMapper abfragevarianteDomainMapper;

    @Autowired
    BauabschnittDomainMapper bauabschnittDomainMapper;

    @Autowired
    DokumentDomainMapper dokumentDomainMapper;

    @Mock
    AuthenticationUtils authenticationUtils;

    private AbfrageService abfrageService;

    @Mock
    private InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    @Mock
    private DokumentService dokumentService;

    @BeforeEach
    public void beforeEach() {
        this.abfrageService =
                new AbfrageService(
                        this.abfrageDomainMapper,
                        this.infrastrukturabfrageRepository,
                        this.dokumentService,
                        this.authenticationUtils
                );
        Mockito.reset(this.infrastrukturabfrageRepository, this.dokumentService);
    }

    @Test
    void getInfrastrukturabfrageById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito
                .when(this.infrastrukturabfrageRepository.findById(id))
                .thenReturn(Optional.of(new Infrastrukturabfrage()));
        final InfrastrukturabfrageModel result = this.abfrageService.getInfrastrukturabfrageById(id);
        assertThat(result, is((new InfrastrukturabfrageModel())));
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.infrastrukturabfrageRepository);

        Mockito.when(this.infrastrukturabfrageRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> this.abfrageService.getInfrastrukturabfrageById(id)
        );
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveInfrastrukturabfrage() throws UniqueViolationException, OptimisticLockingException {
        final UUID uuid = UUID.randomUUID();
        final String sub = "1234";
        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(uuid);
        final AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setNameAbfrage("hallo");
        abfrageModel.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        final Infrastrukturabfrage abfrageEntity = this.abfrageDomainMapper.model2entity(infrastrukturabfrageModel);

        final Infrastrukturabfrage saveResult = new Infrastrukturabfrage();
        saveResult.setId(uuid);
        saveResult.setSub(sub);
        final Abfrage abfrage = new Abfrage();
        abfrage.setNameAbfrage("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        saveResult.setAbfrage(abfrage);

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);
        Mockito.when(this.infrastrukturabfrageRepository.saveAndFlush(abfrageEntity)).thenReturn(saveResult);
        Mockito
                .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
                .thenReturn(Optional.empty());

        final InfrastrukturabfrageModel result =
                this.abfrageService.saveInfrastrukturabfrage(infrastrukturabfrageModel);

        final InfrastrukturabfrageModel expected = new InfrastrukturabfrageModel();
        expected.setId(saveResult.getId());
        expected.setSub(sub);
        final AbfrageModel expectedAbfrage = new AbfrageModel();
        expectedAbfrage.setStatusAbfrage(abfrage.getStatusAbfrage());
        expectedAbfrage.setNameAbfrage(abfrage.getNameAbfrage());
        expected.setAbfrage(expectedAbfrage);

        assertThat(result, is(expected));
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).saveAndFlush(abfrageEntity);
        Mockito
                .verify(this.infrastrukturabfrageRepository, Mockito.times(1))
                .findByAbfrage_NameAbfrageIgnoreCase("hallo");
    }

    @Test
    void saveInfrastrukturabfrageWithAngelegtStatus() throws UniqueViolationException, OptimisticLockingException {
        final UUID uuid = UUID.randomUUID();
        final String sub = "1234";
        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(null);
        infrastrukturabfrageModel.setSub(null);
        final AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setNameAbfrage("hallo");
        abfrageModel.setStatusAbfrage(StatusAbfrage.OFFEN);
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        // Mockito vergleicht die Objekte auf Feldebene weshalb das Objekt genauso sein muss wie wenn es von der Methode aufgerufen wird.
        final Infrastrukturabfrage infrastrukturabfrageEntity = new Infrastrukturabfrage();
        infrastrukturabfrageEntity.setId(null);
        infrastrukturabfrageEntity.setSub(sub);
        final Abfrage abfrageEntity = new Abfrage();
        abfrageEntity.setNameAbfrage("hallo");
        abfrageEntity.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        infrastrukturabfrageEntity.setAbfrage(abfrageEntity);

        final Infrastrukturabfrage saveResult = new Infrastrukturabfrage();
        saveResult.setId(uuid);
        saveResult.setSub(sub);
        final Abfrage abfrage = new Abfrage();
        abfrage.setNameAbfrage("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        saveResult.setAbfrage(abfrage);

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Mockito
                .when(this.infrastrukturabfrageRepository.saveAndFlush(infrastrukturabfrageEntity))
                .thenReturn(saveResult);
        Mockito
                .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
                .thenReturn(Optional.empty());

        final InfrastrukturabfrageModel result =
                this.abfrageService.saveInfrastrukturabfrage(infrastrukturabfrageModel);

        final InfrastrukturabfrageModel expected = new InfrastrukturabfrageModel();
        expected.setId(saveResult.getId());
        expected.setSub(saveResult.getSub());
        final AbfrageModel expectedAbfrage = new AbfrageModel();
        expectedAbfrage.setStatusAbfrage(abfrage.getStatusAbfrage());
        expectedAbfrage.setNameAbfrage(abfrage.getNameAbfrage());
        expected.setAbfrage(expectedAbfrage);

        assertThat(result, is(expected));
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).saveAndFlush(infrastrukturabfrageEntity);
        Mockito
                .verify(this.infrastrukturabfrageRepository, Mockito.times(1))
                .findByAbfrage_NameAbfrageIgnoreCase("hallo");
    }

    @Test
    void saveInfrastrukturabfrageUniqueViolationTest() {
        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(UUID.randomUUID());
        final AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setNameAbfrage("hallo");
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        final InfrastrukturabfrageModel infrastrukturabfrageModel2 = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel2.setId(UUID.randomUUID());
        infrastrukturabfrageModel2.setAbfrage(abfrageModel);

        final Infrastrukturabfrage abfrageEntity = this.abfrageDomainMapper.model2entity(infrastrukturabfrageModel);

        Mockito.when(this.infrastrukturabfrageRepository.saveAndFlush(abfrageEntity)).thenReturn(abfrageEntity);
        Mockito
                .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
                .thenReturn(Optional.of(abfrageEntity));

        Assertions.assertThrows(
                UniqueViolationException.class,
                () -> this.abfrageService.saveInfrastrukturabfrage(infrastrukturabfrageModel2)
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(0)).saveAndFlush(abfrageEntity);
        Mockito
                .verify(this.infrastrukturabfrageRepository, Mockito.times(1))
                .findByAbfrage_NameAbfrageIgnoreCase("hallo");
    }

    @Test
    void patchAbfrageAngelegt()
            throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final UUID abfrageId = UUID.randomUUID();

        final InfrastrukturabfrageAngelegtModel infrastrukturabfrageRequestModel =
                new InfrastrukturabfrageAngelegtModel();

        final AbfrageAngelegtModel abfrageRequestModel = new AbfrageAngelegtModel();
        abfrageRequestModel.setNameAbfrage("hallo");
        infrastrukturabfrageRequestModel.setAbfrage(abfrageRequestModel);

        final AbfragevarianteAngelegtModel abfragevarianteAngelegtModel = new AbfragevarianteAngelegtModel();
        abfragevarianteAngelegtModel.setAbfragevariantenName("Abfragevariante");
        infrastrukturabfrageRequestModel.setAbfragevarianten(List.of(abfragevarianteAngelegtModel));

        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(abfrageId);

        final AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        final InfrastrukturabfrageModel infrastrukturabfrageModelMapped =
                this.abfrageDomainMapper.request2Model(infrastrukturabfrageRequestModel, infrastrukturabfrageModel);
        final Infrastrukturabfrage entity = this.abfrageDomainMapper.model2entity(infrastrukturabfrageModelMapped);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.infrastrukturabfrageRepository.saveAndFlush(entity)).thenReturn(entity);
        Mockito
                .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
                .thenReturn(Optional.empty());

        final InfrastrukturabfrageModel result =
                this.abfrageService.patchAbfrageAngelegt(infrastrukturabfrageRequestModel, entity.getId());

        assertThat(result, is(infrastrukturabfrageModel));

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).saveAndFlush(entity);
        Mockito
                .verify(this.infrastrukturabfrageRepository, Mockito.times(1))
                .findByAbfrage_NameAbfrageIgnoreCase("hallo");
        Mockito
                .verify(this.dokumentService, Mockito.times(1))
                .deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
                        Mockito.isNull(),
                        Mockito.isNull()
                );
    }

    @Test
    void patchAbfrageInBearbeitungSachbearbeitung()
            throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final var infrastrukturabfrageRequestModel = new InfrastrukturabfrageInBearbeitungSachbearbeitungModel();
        infrastrukturabfrageRequestModel.setVersion(0L);

        final var abfragevarianteSachbearbeitung = new AbfragevarianteInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung.setAbfragevariantenName("Abfragevariante 1");

        infrastrukturabfrageRequestModel.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitung));

        final var entityInDb = new Infrastrukturabfrage();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        final var abfrage = new Abfrage();
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage.setNameAbfrage("hallo");
        entityInDb.setAbfrage(abfrage);

        Mockito
                .when(this.infrastrukturabfrageRepository.findById(entityInDb.getId()))
                .thenReturn(Optional.of(entityInDb));

        final var entityToSave = new Infrastrukturabfrage();
        entityToSave.setAbfragevarianten(List.of());
        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        final var abfrageToSave = new Abfrage();
        abfrageToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrageToSave.setNameAbfrage("hallo");
        entityToSave.setAbfrage(abfrageToSave);
        final var abfragevariante1ToSave = new Abfragevariante();
        abfragevariante1ToSave.setAbfragevariantenNr(1);
        abfragevariante1ToSave.setAbfragevariantenName("Abfragevariante 1");
        entityToSave.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1ToSave));

        final var entitySaved = new Infrastrukturabfrage();
        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        final var abfrageSaved = new Abfrage();
        abfrageSaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrageSaved.setNameAbfrage("hallo");
        entitySaved.setAbfrage(abfrageSaved);
        final var abfragevariante1Saved = new Abfragevariante();
        abfragevariante1Saved.setId(UUID.randomUUID());
        abfragevariante1Saved.setAbfragevariantenNr(1);
        abfragevariante1Saved.setAbfragevariantenName("Abfragevariante 1");
        entitySaved.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1Saved));

        Mockito.when(this.infrastrukturabfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito
                .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
                .thenReturn(Optional.empty());

        final var result =
                this.abfrageService.patchAbfrageInBearbeitungSachbearbeitung(infrastrukturabfrageRequestModel, uuid);

        final var entityExpected = new InfrastrukturabfrageModel();
        entityExpected.setId(uuid);
        entityExpected.setVersion(1L);
        final var abfrageExpected = new AbfrageModel();
        abfrageExpected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrageExpected.setNameAbfrage("hallo");
        entityExpected.setAbfrage(abfrageExpected);
        final var abfragevariante1Expected = new AbfragevarianteModel();
        abfragevariante1Expected.setId(abfragevariante1Saved.getId());
        abfragevariante1Expected.setAbfragevariantenNr(1);
        abfragevariante1Expected.setAbfragevariantenName("Abfragevariante 1");
        entityExpected.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1Expected));

        assertThat(result, is(entityExpected));
    }

    @Test
    void patchAbfrageInBearbeitungFachreferate()
            throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();
        final var uuidAbfragevariante = UUID.randomUUID();
        final var uuidAbfragevarianteSachbearbeitung = UUID.randomUUID();

        final var infrastrukturabfrageRequestModel = new InfrastrukturabfrageInBearbeitungFachreferateModel();
        infrastrukturabfrageRequestModel.setVersion(0L);

        final var abfragevariante = new AbfragevarianteInBearbeitungFachreferateModel();
        abfragevariante.setId(uuidAbfragevariante);
        abfragevariante.setVersion(0L);
        final var abfragevarianteBedarfsmeldung = new BedarfsmeldungFachabteilungenModel();
        abfragevarianteBedarfsmeldung.setAnzahlEinrichtungen(5);
        abfragevarianteBedarfsmeldung.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevarianteBedarfsmeldung.setAnzahlKinderkrippengruppen(4);
        abfragevarianteBedarfsmeldung.setAnzahlKindergartengruppen(3);
        abfragevarianteBedarfsmeldung.setAnzahlHortgruppen(2);
        abfragevarianteBedarfsmeldung.setAnzahlGrundschulzuege(1);
        abfragevariante.setBedarfsmeldungFachreferate(List.of(abfragevarianteBedarfsmeldung));

        final var abfragevarianteSachbearbeitung = new AbfragevarianteInBearbeitungFachreferateModel();
        abfragevarianteSachbearbeitung.setId(uuidAbfragevarianteSachbearbeitung);
        abfragevarianteSachbearbeitung.setVersion(0L);
        final var abfragevarianteSachbearbeitungBedarfsmeldung = new BedarfsmeldungFachabteilungenModel();
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlEinrichtungen(1);
        abfragevarianteSachbearbeitungBedarfsmeldung.setInfrastruktureinrichtungTyp(
                InfrastruktureinrichtungTyp.GRUNDSCHULE
        );
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlKinderkrippengruppen(2);
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlKindergartengruppen(3);
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlHortgruppen(4);
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlGrundschulzuege(5);
        abfragevarianteSachbearbeitung.setBedarfsmeldungFachreferate(
                List.of(abfragevarianteSachbearbeitungBedarfsmeldung)
        );

        infrastrukturabfrageRequestModel.setAbfragevarianten(List.of(abfragevariante));
        infrastrukturabfrageRequestModel.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitung));

        final var entityInDb = new Infrastrukturabfrage();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        final var abfrage = new Abfrage();
        abfrage.setNameAbfrage("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        entityInDb.setAbfrage(abfrage);

        final var entityInDbAbfragevariante = new Abfragevariante();
        entityInDbAbfragevariante.setId(uuidAbfragevariante);

        final var entityInDbAbfragevarianteSachbearbeitung = new Abfragevariante();
        entityInDbAbfragevarianteSachbearbeitung.setId(uuidAbfragevarianteSachbearbeitung);

        entityInDb.setAbfragevarianten(List.of(entityInDbAbfragevariante));
        entityInDb.setAbfragevariantenSachbearbeitung(List.of(entityInDbAbfragevarianteSachbearbeitung));

        Mockito
                .when(this.infrastrukturabfrageRepository.findById(entityInDb.getId()))
                .thenReturn(Optional.of(entityInDb));

        final var entityToSave = new Infrastrukturabfrage();

        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        final var abfrageToSave = new Abfrage();
        abfrageToSave.setNameAbfrage("hallo");
        abfrageToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        entityToSave.setAbfrage(abfrageToSave);

        final var abfragevarianteToSaveSave = new Abfragevariante();
        abfragevarianteToSaveSave.setId(uuidAbfragevariante);
        abfragevarianteToSaveSave.setVersion(0L);
        final var abfragevarianteBedarfsmeldungToSave = new BedarfsmeldungFachreferate();
        abfragevarianteBedarfsmeldungToSave.setAnzahlEinrichtungen(5);
        abfragevarianteBedarfsmeldungToSave.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevarianteBedarfsmeldungToSave.setAnzahlKinderkrippengruppen(4);
        abfragevarianteBedarfsmeldungToSave.setAnzahlKindergartengruppen(3);
        abfragevarianteBedarfsmeldungToSave.setAnzahlHortgruppen(2);
        abfragevarianteBedarfsmeldungToSave.setAnzahlGrundschulzuege(1);
        abfragevarianteToSaveSave.setAbfragevarianteSachbearbeitung(new AbfragevarianteSachbearbeitung());
        abfragevarianteToSaveSave
                .getAbfragevarianteSachbearbeitung()
                .setBedarfsmeldungFachreferate(List.of(abfragevarianteBedarfsmeldungToSave));

        final var abfragevarianteSachbearbeitungToSave = new Abfragevariante();
        abfragevarianteSachbearbeitungToSave.setId(uuidAbfragevarianteSachbearbeitung);
        abfragevarianteSachbearbeitungToSave.setVersion(0L);
        final var abfragevarianteSachbearbeitungBedarfsmeldungToSave = new BedarfsmeldungFachreferate();
        abfragevarianteSachbearbeitungBedarfsmeldungToSave.setAnzahlEinrichtungen(1);
        abfragevarianteSachbearbeitungBedarfsmeldungToSave.setInfrastruktureinrichtungTyp(
                InfrastruktureinrichtungTyp.GRUNDSCHULE
        );
        abfragevarianteSachbearbeitungBedarfsmeldungToSave.setAnzahlKinderkrippengruppen(2);
        abfragevarianteSachbearbeitungBedarfsmeldungToSave.setAnzahlKindergartengruppen(3);
        abfragevarianteSachbearbeitungBedarfsmeldungToSave.setAnzahlHortgruppen(4);
        abfragevarianteSachbearbeitungBedarfsmeldungToSave.setAnzahlGrundschulzuege(5);
        abfragevarianteSachbearbeitungToSave.setAbfragevarianteSachbearbeitung(new AbfragevarianteSachbearbeitung());
        abfragevarianteSachbearbeitungToSave
                .getAbfragevarianteSachbearbeitung()
                .setBedarfsmeldungFachreferate(List.of(abfragevarianteSachbearbeitungBedarfsmeldungToSave));

        entityToSave.setAbfragevarianten(List.of(abfragevarianteToSaveSave));
        entityToSave.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitungToSave));

        final var entitySaved = new Infrastrukturabfrage();

        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        final var abfrageSaved = new Abfrage();
        abfrageSaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        abfrageSaved.setNameAbfrage("hallo");
        entitySaved.setAbfrage(abfrageSaved);

        final var abfragevarianteSaved = new Abfragevariante();
        abfragevarianteSaved.setId(uuidAbfragevariante);
        abfragevarianteSaved.setVersion(1L);
        final var abfragevarianteBedarfsmeldungSaved = new BedarfsmeldungFachreferate();
        abfragevarianteBedarfsmeldungSaved.setAnzahlEinrichtungen(5);
        abfragevarianteBedarfsmeldungSaved.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevarianteBedarfsmeldungSaved.setAnzahlKinderkrippengruppen(4);
        abfragevarianteBedarfsmeldungSaved.setAnzahlKindergartengruppen(3);
        abfragevarianteBedarfsmeldungSaved.setAnzahlHortgruppen(2);
        abfragevarianteBedarfsmeldungSaved.setAnzahlGrundschulzuege(1);
        abfragevarianteSaved.setAbfragevarianteSachbearbeitung(new AbfragevarianteSachbearbeitung());
        abfragevarianteSaved
                .getAbfragevarianteSachbearbeitung()
                .setBedarfsmeldungFachreferate(List.of(abfragevarianteBedarfsmeldungSaved));

        final var abfragevarianteSachbearbeitungSaved = new Abfragevariante();
        abfragevarianteSachbearbeitungSaved.setId(uuidAbfragevarianteSachbearbeitung);
        abfragevarianteSachbearbeitungSaved.setVersion(1L);
        final var abfragevarianteSachbearbeitungBedarfsmeldungSaved = new BedarfsmeldungFachreferate();
        abfragevarianteSachbearbeitungBedarfsmeldungSaved.setAnzahlEinrichtungen(1);
        abfragevarianteSachbearbeitungBedarfsmeldungSaved.setInfrastruktureinrichtungTyp(
                InfrastruktureinrichtungTyp.GRUNDSCHULE
        );
        abfragevarianteSachbearbeitungBedarfsmeldungSaved.setAnzahlKinderkrippengruppen(2);
        abfragevarianteSachbearbeitungBedarfsmeldungSaved.setAnzahlKindergartengruppen(3);
        abfragevarianteSachbearbeitungBedarfsmeldungSaved.setAnzahlHortgruppen(4);
        abfragevarianteSachbearbeitungBedarfsmeldungSaved.setAnzahlGrundschulzuege(5);
        abfragevarianteSachbearbeitungSaved.setAbfragevarianteSachbearbeitung(new AbfragevarianteSachbearbeitung());
        abfragevarianteSachbearbeitungSaved
                .getAbfragevarianteSachbearbeitung()
                .setBedarfsmeldungFachreferate(List.of(abfragevarianteSachbearbeitungBedarfsmeldungSaved));

        entitySaved.setAbfragevarianten(List.of(abfragevarianteSaved));
        entitySaved.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitungSaved));

        Mockito.when(this.infrastrukturabfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito
                .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
                .thenReturn(Optional.empty());

        final var result =
                this.abfrageService.patchAbfrageInBearbeitungFachreferate(infrastrukturabfrageRequestModel, uuid);

        final var entityExpected = new InfrastrukturabfrageModel();
        entityExpected.setId(uuid);
        entityExpected.setVersion(1L);
        final var abfrageExpected = new AbfrageModel();
        abfrageExpected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        abfrageExpected.setNameAbfrage("hallo");
        entityExpected.setAbfrage(abfrageExpected);

        final var abfragevarianteExpected = new AbfragevarianteModel();
        abfragevarianteExpected.setId(abfragevarianteSaved.getId());
        abfragevarianteExpected.setVersion(1L);
        final var abfragevarianteBedarfsmeldungExpected = new BedarfsmeldungFachabteilungenModel();
        abfragevarianteBedarfsmeldungExpected.setAnzahlEinrichtungen(5);
        abfragevarianteBedarfsmeldungExpected.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevarianteBedarfsmeldungExpected.setAnzahlKinderkrippengruppen(4);
        abfragevarianteBedarfsmeldungExpected.setAnzahlKindergartengruppen(3);
        abfragevarianteBedarfsmeldungExpected.setAnzahlHortgruppen(2);
        abfragevarianteBedarfsmeldungExpected.setAnzahlGrundschulzuege(1);
        abfragevarianteExpected.setAbfragevarianteSachbearbeitung(new AbfragevarianteSachbearbeitungModel());
        abfragevarianteExpected
                .getAbfragevarianteSachbearbeitung()
                .setBedarfsmeldungFachreferate(List.of(abfragevarianteBedarfsmeldungExpected));

        final var abfragevarianteSachbearbeitungExpected = new AbfragevarianteModel();
        abfragevarianteSachbearbeitungExpected.setId(abfragevarianteSachbearbeitungSaved.getId());
        abfragevarianteSachbearbeitungExpected.setVersion(1L);
        final var abfragevarianteSachbearbeitungBedarfsmeldungExpected = new BedarfsmeldungFachabteilungenModel();
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setAnzahlEinrichtungen(1);
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setInfrastruktureinrichtungTyp(
                InfrastruktureinrichtungTyp.GRUNDSCHULE
        );
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setAnzahlKinderkrippengruppen(2);
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setAnzahlKindergartengruppen(3);
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setAnzahlHortgruppen(4);
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setAnzahlGrundschulzuege(5);
        abfragevarianteSachbearbeitungExpected.setAbfragevarianteSachbearbeitung(
                new AbfragevarianteSachbearbeitungModel()
        );
        abfragevarianteSachbearbeitungExpected
                .getAbfragevarianteSachbearbeitung()
                .setBedarfsmeldungFachreferate(List.of(abfragevarianteSachbearbeitungBedarfsmeldungExpected));

        entityExpected.setAbfragevarianten(List.of(abfragevarianteExpected));
        entityExpected.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitungExpected));

        assertThat(result, is(entityExpected));
    }

    @Test
    void throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid()
            throws FileHandlingFailedException, FileHandlingWithS3FailedException {
        final UUID abfrageId = UUID.randomUUID();
        final InfrastrukturabfrageAngelegtModel infrastrukturabfrageRequestModel =
                new InfrastrukturabfrageAngelegtModel();
        final AbfrageAngelegtModel abfrageRequestModel = new AbfrageAngelegtModel();
        abfrageRequestModel.setNameAbfrage("test");
        infrastrukturabfrageRequestModel.setAbfrage(abfrageRequestModel);

        final AbfragevarianteAngelegtModel abfragevarianteAngelegtModel = new AbfragevarianteAngelegtModel();
        abfragevarianteAngelegtModel.setAbfragevariantenName("Abfragevariante");
        infrastrukturabfrageRequestModel.setAbfragevarianten(List.of(abfragevarianteAngelegtModel));

        InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(abfrageId);
        AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setStatusAbfrage(StatusAbfrage.OFFEN);
        infrastrukturabfrageModel.setAbfrage(abfrageModel);
        infrastrukturabfrageModel =
                this.abfrageDomainMapper.request2Model(infrastrukturabfrageRequestModel, infrastrukturabfrageModel);

        final Infrastrukturabfrage entity = this.abfrageDomainMapper.model2entity(infrastrukturabfrageModel);
        entity.setId(UUID.randomUUID());
        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Assertions.assertThrows(
                AbfrageStatusNotAllowedException.class,
                () -> this.abfrageService.patchAbfrageAngelegt(infrastrukturabfrageRequestModel, entity.getId())
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(0)).saveAndFlush(entity);
        Mockito
                .verify(this.infrastrukturabfrageRepository, Mockito.times(0))
                .findByAbfrage_NameAbfrageIgnoreCase("test");
        Mockito
                .verify(this.dokumentService, Mockito.times(0))
                .deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
                        Mockito.isNull(),
                        Mockito.isNull()
                );
    }

    @Test
    void deleteInfrastrukturabfrageAbfrageerstellung()
            throws EntityNotFoundException, EntityIsReferencedException, UserRoleNotAllowedException, AbfrageStatusNotAllowedException {
        final UUID id = UUID.randomUUID();

        String[] roles = {"abfrageerstellung"};
        String sub = "1234";

        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(id);
        entity.setSub(sub);
        final Abfrage abfrage = new Abfrage();
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        entity.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        this.abfrageService.deleteInfrasturkturabfrageById(id);

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteInfrastrukturabfrageAdmin()
            throws EntityNotFoundException, EntityIsReferencedException, UserRoleNotAllowedException, AbfrageStatusNotAllowedException {
        final UUID id = UUID.randomUUID();

        String[] roles = {"admin"};
        String sub = "1234";

        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(id);
        entity.setSub(sub);
        final Abfrage abfrage = new Abfrage();
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        entity.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        this.abfrageService.deleteInfrasturkturabfrageById(id);

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteInfrastrukturabfrageAdminNotSameSub()
            throws EntityNotFoundException, EntityIsReferencedException, UserRoleNotAllowedException, AbfrageStatusNotAllowedException {
        final UUID id = UUID.randomUUID();

        String[] roles = {"admin"};
        String sub = "1234";
        String userSub = "6789";

        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(id);
        entity.setSub(sub);
        final Abfrage abfrage = new Abfrage();
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        entity.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(userSub);

        this.abfrageService.deleteInfrasturkturabfrageById(id);

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteInfrastrukturabfrageUserNotAllowedException() {
        final UUID id = UUID.randomUUID();
        String[] roles = {"abfrageerstellung"};
        String sub = "1234";
        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(id);
        entity.setSub("123");
        final Abfrage abfrage = new Abfrage();
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        entity.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Assertions.assertThrows(
                UserRoleNotAllowedException.class,
                () -> this.abfrageService.deleteInfrasturkturabfrageById(id)
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void deleteInfrastrukturabfrageException() {
        final UUID id = UUID.randomUUID();
        String[] roles = {"abfrageerstellung"};
        String sub = "1234";
        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(id);
        entity.setSub(sub);
        final Abfrage abfrage = new Abfrage();
        abfrage.setBauvorhaben(new Bauvorhaben());
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        entity.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Assertions.assertThrows(
                EntityIsReferencedException.class,
                () -> this.abfrageService.deleteInfrasturkturabfrageById(id)
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void deleteInfrastrukturabfrageStatusException() {
        final UUID id = UUID.randomUUID();
        String[] roles = {"abfrageerstellung"};
        String sub = "1234";
        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(id);
        entity.setSub(sub);
        final Abfrage abfrage = new Abfrage();
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);
        entity.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Assertions.assertThrows(
                AbfrageStatusNotAllowedException.class,
                () -> this.abfrageService.deleteInfrasturkturabfrageById(id)
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void deleteInfrastrukturabfrageNutzerException() {
        final UUID id = UUID.randomUUID();
        String[] roles = {"nutzer"};
        String sub = "1234";
        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(id);
        entity.setSub(sub);
        final Abfrage abfrage = new Abfrage();
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        entity.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Assertions.assertThrows(
                UserRoleNotAllowedException.class,
                () -> this.abfrageService.deleteInfrasturkturabfrageById(id)
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben() throws EntityIsReferencedException {
        this.abfrageService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(new AbfrageModel());

        final AbfrageModel abfrage = new AbfrageModel();
        abfrage.setBauvorhaben(new BauvorhabenModel());
        Assertions.assertThrows(
                EntityIsReferencedException.class,
                () -> this.abfrageService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(abfrage)
        );
    }
}
