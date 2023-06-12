package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.BauvorhabenNotReferencedException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
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
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungAbfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungAbfragevarianteAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageerstellungInfrastrukturabfrageAngelegtModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.SachbearbeitungAbfragevarianteInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung.SachbearbeitungInfrastrukturabfrageInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Abfragevariante;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
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

    private AbfrageService abfrageService;

    @Mock
    private InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    @Mock
    private DokumentService dokumentService;

    @BeforeEach
    public void beforeEach() {
        this.abfrageService =
            new AbfrageService(this.abfrageDomainMapper, this.infrastrukturabfrageRepository, this.dokumentService);
        Mockito.reset(this.infrastrukturabfrageRepository, this.dokumentService);
    }

    @Test
    void getInfrastrukturabfragen() {
        final Infrastrukturabfrage entity1 = new Infrastrukturabfrage();
        entity1.setId(UUID.randomUUID());
        final Infrastrukturabfrage entity2 = new Infrastrukturabfrage();
        entity2.setId(UUID.randomUUID());

        Mockito
            .when(this.infrastrukturabfrageRepository.findAllByOrderByAbfrageFristStellungnahmeDesc())
            .thenReturn(Stream.of(entity1, entity2));

        final List<InfrastrukturabfrageModel> result = this.abfrageService.getInfrastrukturabfragen();

        final InfrastrukturabfrageModel model1 = new InfrastrukturabfrageModel();
        model1.setId(entity1.getId());
        final InfrastrukturabfrageModel model2 = new InfrastrukturabfrageModel();
        model2.setId(entity2.getId());

        assertThat(result, is(List.of(model1, model2)));
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
        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(uuid);
        final AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setNameAbfrage("hallo");
        abfrageModel.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        final Infrastrukturabfrage abfrageEntity = this.abfrageDomainMapper.model2entity(infrastrukturabfrageModel);

        final Infrastrukturabfrage saveResult = new Infrastrukturabfrage();
        saveResult.setId(uuid);
        final Abfrage abfrage = new Abfrage();
        abfrage.setNameAbfrage("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        saveResult.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.saveAndFlush(abfrageEntity)).thenReturn(saveResult);
        Mockito
            .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
            .thenReturn(Optional.empty());

        final InfrastrukturabfrageModel result =
            this.abfrageService.saveInfrastrukturabfrage(infrastrukturabfrageModel);

        final InfrastrukturabfrageModel expected = new InfrastrukturabfrageModel();
        expected.setId(saveResult.getId());
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
        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(null);
        final AbfrageModel abfrageModel = new AbfrageModel();
        abfrageModel.setNameAbfrage("hallo");
        abfrageModel.setStatusAbfrage(StatusAbfrage.OFFEN);
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        // Mockito vergleicht die Objekte auf Feldebene weshalb das Objekt genauso sein muss wie wenn es von der Methode aufgerufen wird.
        final Infrastrukturabfrage infrastrukturabfrageEntity = new Infrastrukturabfrage();
        infrastrukturabfrageEntity.setId(null);
        final Abfrage abfrageEntity = new Abfrage();
        abfrageEntity.setNameAbfrage("hallo");
        abfrageEntity.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        infrastrukturabfrageEntity.setAbfrage(abfrageEntity);

        final Infrastrukturabfrage saveResult = new Infrastrukturabfrage();
        saveResult.setId(uuid);
        final Abfrage abfrage = new Abfrage();
        abfrage.setNameAbfrage("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        saveResult.setAbfrage(abfrage);

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

        final AbfrageerstellungInfrastrukturabfrageAngelegtModel infrastrukturabfrageRequestModel =
            new AbfrageerstellungInfrastrukturabfrageAngelegtModel();

        final AbfrageerstellungAbfrageAngelegtModel abfrageRequestModel = new AbfrageerstellungAbfrageAngelegtModel();
        abfrageRequestModel.setNameAbfrage("hallo");
        infrastrukturabfrageRequestModel.setAbfrage(abfrageRequestModel);

        final AbfrageerstellungAbfragevarianteAngelegtModel abfragevarianteAngelegtModel =
            new AbfrageerstellungAbfragevarianteAngelegtModel();
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

        final var infrastrukturabfrageRequestModel =
            new SachbearbeitungInfrastrukturabfrageInBearbeitungSachbearbeitungModel();
        infrastrukturabfrageRequestModel.setVersion(0L);

        final var abfragevarianteSachbearbeitung =
            new SachbearbeitungAbfragevarianteInBearbeitungSachbearbeitungModel();
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
        entityToSave.setId(uuid);
        entityInDb.setVersion(0L);
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

        final var entityExpected = new Infrastrukturabfrage();
        entityExpected.setId(uuid);
        entityExpected.setVersion(1L);
        final var abfrageExpected = new Abfrage();
        abfrageExpected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrageExpected.setNameAbfrage("hallo");
        entityExpected.setAbfrage(abfrageExpected);
        final var abfragevariante1Expected = new Abfragevariante();
        abfragevariante1Expected.setId(UUID.randomUUID());
        abfragevariante1Expected.setAbfragevariantenNr(1);
        abfragevariante1Expected.setAbfragevariantenName("Abfragevariante 1");
        entityExpected.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1Expected));

        assertThat(result, is(entityExpected));
    }

    @Test
    void changeAbfragevarianteRelevant()
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, FileHandlingFailedException, FileHandlingWithS3FailedException, BauvorhabenNotReferencedException {
        final UUID abfrageId = UUID.randomUUID();
        final UUID abfragevarianteId = UUID.randomUUID();
        final UUID bauvorhabenId = UUID.randomUUID();

        BauvorhabenModel bauvorhabenModel = new BauvorhabenModel();
        bauvorhabenModel.setId(bauvorhabenId);

        final InfrastrukturabfrageModel infrastrukturabfrage = new InfrastrukturabfrageModel();
        infrastrukturabfrage.setId(abfrageId);
        final AbfrageModel abfrage = new AbfrageModel();
        abfrage.setNameAbfrage("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage.setBauvorhaben(bauvorhabenModel);
        infrastrukturabfrage.setAbfrage(abfrage);

        final AbfragevarianteModel abfragevariante = new AbfragevarianteModel();
        abfragevariante.setRelevant(false);
        abfragevariante.setId(abfragevarianteId);
        infrastrukturabfrage.setAbfragevarianten(List.of(abfragevariante));

        Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(bauvorhabenId);

        final Infrastrukturabfrage infrastrukturabfrageEntity = new Infrastrukturabfrage();
        infrastrukturabfrageEntity.setId(abfrageId);

        final Abfrage abfrageEntity = new Abfrage();
        abfrageEntity.setNameAbfrage("hallo");
        abfrageEntity.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrageEntity.setBauvorhaben(bauvorhabenEntity);
        infrastrukturabfrageEntity.setAbfrage(abfrageEntity);

        final Abfragevariante abfragevarianteEntity = new Abfragevariante();
        abfragevarianteEntity.setRelevant(true);
        abfragevarianteEntity.setId(abfragevarianteId);
        infrastrukturabfrageEntity.setAbfragevarianten(List.of(abfragevarianteEntity));

        Infrastrukturabfrage entity = this.abfrageDomainMapper.model2entity(infrastrukturabfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito
            .when(this.infrastrukturabfrageRepository.saveAndFlush(infrastrukturabfrageEntity))
            .thenReturn(infrastrukturabfrageEntity);
        Mockito
            .when(this.infrastrukturabfrageRepository.findByAbfrage_NameAbfrageIgnoreCase("hallo"))
            .thenReturn(Optional.empty());

        final InfrastrukturabfrageModel result =
            this.abfrageService.changeAbfragevarianteRelevant(abfrageId, abfragevarianteId);

        assertThat(result.getAbfragevarianten().get(0).isRelevant(), is(true));

        Mockito
            .verify(this.infrastrukturabfrageRepository, Mockito.times(1))
            .findById(infrastrukturabfrageEntity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).saveAndFlush(infrastrukturabfrageEntity);
        Mockito
            .verify(this.infrastrukturabfrageRepository, Mockito.times(1))
            .findByAbfrage_NameAbfrageIgnoreCase("hallo");
    }

    @Test
    void changeAbfragevarianteRelevantUniqueViolationTest()
        throws UniqueViolationException, OptimisticLockingException {
        final UUID abfrageId_1 = UUID.randomUUID();
        final UUID abfrageId_2 = UUID.randomUUID();

        BauvorhabenModel bauvorhabenModel = new BauvorhabenModel();
        bauvorhabenModel.setId(UUID.randomUUID());

        final InfrastrukturabfrageModel infrastrukturabfrage_1 = new InfrastrukturabfrageModel();
        infrastrukturabfrage_1.setId(abfrageId_1);
        final AbfrageModel abfrage_1 = new AbfrageModel();
        abfrage_1.setNameAbfrage("hallo");
        abfrage_1.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage_1.setBauvorhaben(bauvorhabenModel);
        infrastrukturabfrage_1.setAbfrage(abfrage_1);

        final AbfragevarianteModel abfragevariante_1 = new AbfragevarianteModel();
        abfragevariante_1.setRelevant(true);
        abfragevariante_1.setId(UUID.randomUUID());

        infrastrukturabfrage_1.setAbfragevarianten(List.of(abfragevariante_1));

        final InfrastrukturabfrageModel infrastrukturabfrage_2 = new InfrastrukturabfrageModel();
        infrastrukturabfrage_2.setId(abfrageId_2);
        final AbfrageModel abfrage_2 = new AbfrageModel();
        abfrage_2.setNameAbfrage("hallo");
        abfrage_2.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrage_2.setBauvorhaben(bauvorhabenModel);
        infrastrukturabfrage_2.setAbfrage(abfrage_2);

        final AbfragevarianteModel abfragevariante_2 = new AbfragevarianteModel();
        abfragevariante_2.setRelevant(false);
        abfragevariante_2.setId(UUID.randomUUID());

        infrastrukturabfrage_2.setAbfragevarianten(List.of(abfragevariante_2));

        Infrastrukturabfrage entity_1 = this.abfrageDomainMapper.model2entity(infrastrukturabfrage_1);

        Infrastrukturabfrage entity_2 = this.abfrageDomainMapper.model2entity(infrastrukturabfrage_2);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity_1.getId())).thenReturn(Optional.of(entity_1));
        Mockito.when(this.infrastrukturabfrageRepository.saveAndFlush(entity_1)).thenReturn(entity_1);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity_2.getId())).thenReturn(Optional.of(entity_2));

        Mockito
            .when(this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(bauvorhabenModel.getId()))
            .thenReturn(Stream.of(entity_1, entity_2));

        this.abfrageService.saveInfrastrukturabfrage(infrastrukturabfrage_1);

        Assertions.assertThrows(
            UniqueViolationException.class,
            () -> this.abfrageService.changeAbfragevarianteRelevant(abfrageId_2, abfragevariante_2.getId())
        );

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).saveAndFlush(entity_1);
    }

    @Test
    void changeAbfragevarianteRelevantBauvorhabenNotReferenced() {
        final UUID abfrageId = UUID.randomUUID();

        final InfrastrukturabfrageModel infrastrukturabfrage = new InfrastrukturabfrageModel();
        infrastrukturabfrage.setId(abfrageId);
        final AbfrageModel abfrage = new AbfrageModel();
        abfrage.setNameAbfrage("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        infrastrukturabfrage.setAbfrage(abfrage);

        final AbfragevarianteModel abfragevariante = new AbfragevarianteModel();
        abfragevariante.setRelevant(false);
        abfragevariante.setId(UUID.randomUUID());

        infrastrukturabfrage.setAbfragevarianten(List.of(abfragevariante));

        Infrastrukturabfrage entity = this.abfrageDomainMapper.model2entity(infrastrukturabfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Assertions.assertThrows(
            BauvorhabenNotReferencedException.class,
            () -> this.abfrageService.changeAbfragevarianteRelevant(abfrageId, abfragevariante.getId())
        );
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
    }

    @Test
    void throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid()
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final UUID abfrageId = UUID.randomUUID();
        final AbfrageerstellungInfrastrukturabfrageAngelegtModel infrastrukturabfrageRequestModel =
            new AbfrageerstellungInfrastrukturabfrageAngelegtModel();
        final AbfrageerstellungAbfrageAngelegtModel abfrageRequestModel = new AbfrageerstellungAbfrageAngelegtModel();
        abfrageRequestModel.setNameAbfrage("test");
        infrastrukturabfrageRequestModel.setAbfrage(abfrageRequestModel);

        final AbfrageerstellungAbfragevarianteAngelegtModel abfragevarianteAngelegtModel =
            new AbfrageerstellungAbfragevarianteAngelegtModel();
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
    void deleteInfrastrukturabfrage() throws EntityNotFoundException, EntityIsReferencedException {
        final UUID id = UUID.randomUUID();

        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(id);
        final Abfrage abfrage = new Abfrage();
        entity.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        this.abfrageService.deleteInfrasturkturabfrageById(id);

        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.infrastrukturabfrageRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteInfrastrukturabfrageException() {
        final UUID id = UUID.randomUUID();

        final Infrastrukturabfrage entity = new Infrastrukturabfrage();
        entity.setId(id);
        final Abfrage abfrage = new Abfrage();
        abfrage.setBauvorhaben(new Bauvorhaben());
        entity.setAbfrage(abfrage);

        Mockito.when(this.infrastrukturabfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Assertions.assertThrows(
            EntityIsReferencedException.class,
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
