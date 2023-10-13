package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
import de.muenchen.isi.domain.mapper.AbfragevarianteBauleitplanverfahrenDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapperImpl;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.BedarfsmeldungFachreferateModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteBauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.BedarfsmeldungFachreferate;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AbfrageServiceTest {

    private AbfrageService abfrageService;

    @Mock
    private AbfrageRepository abfrageRepository;

    private AbfrageDomainMapper abfrageDomainMapper;

    @Mock
    private BauvorhabenService bauvorhabenService;

    @Mock
    private DokumentService dokumentService;

    @Mock
    private AuthenticationUtils authenticationUtils;

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        final var abfragevarianteDomainMapper = new AbfragevarianteBauleitplanverfahrenDomainMapperImpl(
            new BauabschnittDomainMapperImpl()
        );
        this.abfrageDomainMapper =
            new AbfrageDomainMapperImpl(abfragevarianteDomainMapper, new DokumentDomainMapperImpl());
        Field field = abfrageDomainMapper
            .getClass()
            .getSuperclass()
            .getDeclaredField("abfragevarianteBauleitplanverfahrenDomainMapper");
        field.setAccessible(true);
        field.set(abfrageDomainMapper, abfragevarianteDomainMapper);
        this.abfrageService =
            new AbfrageService(
                this.abfrageRepository,
                this.abfrageDomainMapper,
                this.bauvorhabenService,
                this.dokumentService,
                this.authenticationUtils
            );
        Mockito.reset(this.abfrageRepository, this.bauvorhabenService, this.dokumentService, this.authenticationUtils);
    }

    @Test
    void getById() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.abfrageRepository.findById(id)).thenReturn(Optional.of(new Bauleitplanverfahren()));
        final AbfrageModel result = this.abfrageService.getById(id);
        final var expected = new BauleitplanverfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        assertThat(result, is((expected)));
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.abfrageRepository);

        Mockito.when(this.abfrageRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.abfrageService.getById(id));
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveAbfrageWithId() throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        final UUID uuid = UUID.randomUUID();
        final String sub = "1234";
        final BauleitplanverfahrenModel abfrage = new BauleitplanverfahrenModel();
        abfrage.setId(uuid);
        abfrage.setName("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final Abfrage abfrageEntity = this.abfrageDomainMapper.model2Entity(abfrage);

        final Abfrage saveResult = new Bauleitplanverfahren();
        saveResult.setId(uuid);
        saveResult.setSub(sub);
        saveResult.setName("hallo");
        saveResult.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);
        Mockito.when(this.abfrageRepository.saveAndFlush(abfrageEntity)).thenReturn(saveResult);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final AbfrageModel result = this.abfrageService.save(abfrage);

        final BauleitplanverfahrenModel expected = new BauleitplanverfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        expected.setId(saveResult.getId());
        expected.setSub(sub);
        expected.setStatusAbfrage(abfrage.getStatusAbfrage());
        expected.setName(abfrage.getName());

        assertThat(result, is(expected));
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).saveAndFlush(abfrageEntity);
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findByNameIgnoreCase("hallo");
        Mockito.verify(this.bauvorhabenService, Mockito.times(0)).getBauvorhabenById(UUID.randomUUID());
    }

    @Test
    void saveAbfrageWithoutId() throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException {
        final UUID uuid = UUID.randomUUID();
        final String sub = "1234";
        final BauleitplanverfahrenModel abfrage = new BauleitplanverfahrenModel();
        abfrage.setId(null);
        abfrage.setSub(null);
        abfrage.setName("hallo");
        abfrage.setStatusAbfrage(StatusAbfrage.OFFEN);

        final Bauleitplanverfahren abfrageEntity = new Bauleitplanverfahren();
        abfrageEntity.setId(null);
        abfrageEntity.setSub(sub);
        abfrageEntity.setName("hallo");
        abfrageEntity.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        final Bauleitplanverfahren saveResult = new Bauleitplanverfahren();
        saveResult.setId(uuid);
        saveResult.setSub(sub);
        saveResult.setName("hallo");
        saveResult.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Mockito.when(this.abfrageRepository.saveAndFlush(abfrageEntity)).thenReturn(saveResult);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final AbfrageModel result = this.abfrageService.save(abfrage);

        final BauleitplanverfahrenModel expected = new BauleitplanverfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        expected.setId(saveResult.getId());
        expected.setSub(saveResult.getSub());
        expected.setStatusAbfrage(abfrage.getStatusAbfrage());
        expected.setName(abfrage.getName());

        assertThat(result, is(expected));
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).saveAndFlush(abfrageEntity);
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findByNameIgnoreCase("hallo");
        Mockito.verify(this.bauvorhabenService, Mockito.times(0)).getBauvorhabenById(UUID.randomUUID());
    }

    @Test
    void saveUniqueViolationTest() throws EntityNotFoundException {
        final BauleitplanverfahrenModel abfrage = new BauleitplanverfahrenModel();
        abfrage.setId(UUID.randomUUID());
        abfrage.setName("hallo");

        final BauleitplanverfahrenModel abfrage2 = new BauleitplanverfahrenModel();
        abfrage2.setId(UUID.randomUUID());
        abfrage2.setName("hallo");

        final Abfrage abfrageEntity = this.abfrageDomainMapper.model2Entity(abfrage);

        Mockito.when(this.abfrageRepository.saveAndFlush(abfrageEntity)).thenReturn(abfrageEntity);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.of(abfrageEntity));

        Assertions.assertThrows(UniqueViolationException.class, () -> this.abfrageService.save(abfrage2));

        Mockito.verify(this.abfrageRepository, Mockito.times(0)).saveAndFlush(abfrageEntity);
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findByNameIgnoreCase("hallo");
        Mockito.verify(this.bauvorhabenService, Mockito.times(0)).getBauvorhabenById(UUID.randomUUID());
    }

    @Test
    void patchAngelegt()
        throws EntityNotFoundException, UniqueViolationException, FileHandlingFailedException, FileHandlingWithS3FailedException, OptimisticLockingException, AbfrageStatusNotAllowedException {
        final UUID abfrageId = UUID.randomUUID();

        final BauleitplanverfahrenAngelegtModel requestModel = new BauleitplanverfahrenAngelegtModel();
        requestModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        requestModel.setName("hallo");

        final AbfragevarianteBauleitplanverfahrenAngelegtModel abfragevarianteRequestModel =
            new AbfragevarianteBauleitplanverfahrenAngelegtModel();
        abfragevarianteRequestModel.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteRequestModel.setName("Abfragevariante");
        requestModel.setAbfragevarianten(List.of(abfragevarianteRequestModel));

        final BauleitplanverfahrenModel model = new BauleitplanverfahrenModel();
        model.setId(abfrageId);
        model.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        final BauleitplanverfahrenModel abfrageModelMapped =
            this.abfrageDomainMapper.request2Model(requestModel, model);
        final Abfrage entity = this.abfrageDomainMapper.model2Entity(abfrageModelMapped);

        Mockito.when(this.abfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.abfrageRepository.saveAndFlush(entity)).thenReturn(entity);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final AbfrageModel result = this.abfrageService.patchAngelegt(requestModel, entity.getId());

        assertThat(result, is(model));

        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).saveAndFlush(entity);
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findByNameIgnoreCase("hallo");
        Mockito
            .verify(this.dokumentService, Mockito.times(1))
            .deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
                Mockito.isNull(),
                Mockito.isNull()
            );
    }

    @Test
    void patchAngelegtArtAbfrageNotSupported()
        throws UniqueViolationException, FileHandlingFailedException, FileHandlingWithS3FailedException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException {
        final UUID abfrageId = UUID.randomUUID();

        final BauleitplanverfahrenAngelegtModel requestModel = new BauleitplanverfahrenAngelegtModel();
        requestModel.setName("hallo");

        final AbfragevarianteBauleitplanverfahrenAngelegtModel abfragevarianteRequestModel =
            new AbfragevarianteBauleitplanverfahrenAngelegtModel();
        abfragevarianteRequestModel.setName("Abfragevariante");
        requestModel.setAbfragevarianten(List.of(abfragevarianteRequestModel));

        final BauleitplanverfahrenModel model = new BauleitplanverfahrenModel();
        model.setId(abfrageId);
        model.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        final BauleitplanverfahrenModel abfrageModelMapped =
            this.abfrageDomainMapper.request2Model(requestModel, model);
        final Abfrage entity = this.abfrageDomainMapper.model2Entity(abfrageModelMapped);

        Mockito.when(this.abfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.abfrageRepository.saveAndFlush(entity)).thenReturn(entity);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        try {
            this.abfrageService.patchAngelegt(requestModel, entity.getId());
        } catch (final EntityNotFoundException exception) {
            assertThat(exception.getMessage(), is("Die Art der Abfrage wird nicht unterstützt."));
        }
    }

    @Test
    void patchInBearbeitungSachbearbeitung()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final var requestModel = new BauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        requestModel.setVersion(0L);
        requestModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        final var abfragevarianteSachbearbeitung =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteSachbearbeitung.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung.setName("Abfragevariante 1");
        abfragevarianteSachbearbeitung.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevarianteSachbearbeitung.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2017);
        abfragevarianteSachbearbeitung.setAnmerkung("Test Anmerkung");
        requestModel.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitung));

        final var entityInDb = new Bauleitplanverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityInDb.setName("hallo");

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final var entityToSave = new Bauleitplanverfahren();
        entityToSave.setAbfragevarianten(List.of());
        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityToSave.setName("hallo");
        final var abfragevariante1ToSave = new AbfragevarianteBauleitplanverfahren();
        abfragevariante1ToSave.setAbfragevariantenNr(1);
        abfragevariante1ToSave.setName("Abfragevariante 1");
        abfragevariante1ToSave.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1ToSave.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariante1ToSave.setAnmerkung("Test Anmerkung");
        entityToSave.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1ToSave));

        final var entitySaved = new Bauleitplanverfahren();
        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entitySaved.setName("hallo");
        final var abfragevariante1Saved = new AbfragevarianteBauleitplanverfahren();
        abfragevariante1Saved.setId(UUID.randomUUID());
        abfragevariante1Saved.setAbfragevariantenNr(1);
        abfragevariante1Saved.setName("Abfragevariante 1");
        abfragevariante1Saved.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1Saved.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariante1Saved.setAnmerkung("Test Anmerkung");
        entitySaved.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1Saved));

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final var result = this.abfrageService.patchInBearbeitungSachbearbeitung(requestModel, uuid);

        final var expected = new BauleitplanverfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        expected.setId(uuid);
        expected.setVersion(1L);
        expected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        expected.setName("hallo");
        final var abfragevariante1Expected = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante1Expected.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevariante1Expected.setId(abfragevariante1Saved.getId());
        abfragevariante1Expected.setAbfragevariantenNr(1);
        abfragevariante1Expected.setName("Abfragevariante 1");
        abfragevariante1Expected.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1Expected.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariante1Expected.setAnmerkung("Test Anmerkung");
        expected.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1Expected));

        assertThat(result, is(expected));
    }

    @Test
    void patchInBearbeitungSachbearbeitungAbfrageNotSupported()
        throws UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();

        final var requestModel = new BauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        requestModel.setVersion(0L);
        requestModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        final var abfragevarianteSachbearbeitung =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung.setName("Abfragevariante 1");
        requestModel.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitung));

        final var entityInDb = new Bauleitplanverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityInDb.setName("hallo");

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final var entityToSave = new Bauleitplanverfahren();
        entityToSave.setAbfragevarianten(List.of());
        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityToSave.setName("hallo");
        final var abfragevariante1ToSave = new AbfragevarianteBauleitplanverfahren();
        abfragevariante1ToSave.setAbfragevariantenNr(1);
        abfragevariante1ToSave.setName("Abfragevariante 1");
        entityToSave.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1ToSave));

        final var entitySaved = new Bauleitplanverfahren();
        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entitySaved.setName("hallo");
        final var abfragevariante1Saved = new AbfragevarianteBauleitplanverfahren();
        abfragevariante1Saved.setId(UUID.randomUUID());
        abfragevariante1Saved.setAbfragevariantenNr(1);
        abfragevariante1Saved.setName("Abfragevariante 1");
        entitySaved.setAbfragevariantenSachbearbeitung(List.of(abfragevariante1Saved));

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        try {
            this.abfrageService.patchInBearbeitungSachbearbeitung(requestModel, uuid);
        } catch (final EntityNotFoundException exception) {
            assertThat(exception.getMessage(), is("Die Art der Abfrage wird nicht unterstützt."));
        }
    }

    @Test
    void patchInBearbeitungFachreferat()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();
        final var uuidAbfragevariante = UUID.randomUUID();
        final var uuidAbfragevarianteSachbearbeitung = UUID.randomUUID();

        final var requestModel = new BauleitplanverfahrenInBearbeitungFachreferatModel();
        requestModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        requestModel.setVersion(0L);

        final var abfragevarianteRequestModel = new AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel();
        abfragevarianteRequestModel.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteRequestModel.setId(uuidAbfragevariante);
        abfragevarianteRequestModel.setVersion(0L);
        final var abfragevarianteBedarfsmeldung = new BedarfsmeldungFachreferateModel();
        abfragevarianteBedarfsmeldung.setAnzahlEinrichtungen(5);
        abfragevarianteBedarfsmeldung.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevarianteBedarfsmeldung.setAnzahlKinderkrippengruppen(4);
        abfragevarianteBedarfsmeldung.setAnzahlKindergartengruppen(3);
        abfragevarianteBedarfsmeldung.setAnzahlHortgruppen(2);
        abfragevarianteBedarfsmeldung.setAnzahlGrundschulzuege(1);
        abfragevarianteRequestModel.setBedarfsmeldungFachreferate(List.of(abfragevarianteBedarfsmeldung));

        final var abfragevarianteSachbearbeitungRequestModel =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel();
        abfragevarianteSachbearbeitungRequestModel.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteSachbearbeitungRequestModel.setId(uuidAbfragevarianteSachbearbeitung);
        abfragevarianteSachbearbeitungRequestModel.setVersion(0L);
        final var abfragevarianteSachbearbeitungBedarfsmeldung = new BedarfsmeldungFachreferateModel();
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlEinrichtungen(1);
        abfragevarianteSachbearbeitungBedarfsmeldung.setInfrastruktureinrichtungTyp(
            InfrastruktureinrichtungTyp.GRUNDSCHULE
        );
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlKinderkrippengruppen(2);
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlKindergartengruppen(3);
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlHortgruppen(4);
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlGrundschulzuege(5);
        abfragevarianteSachbearbeitungRequestModel.setBedarfsmeldungFachreferate(
            List.of(abfragevarianteSachbearbeitungBedarfsmeldung)
        );

        requestModel.setAbfragevarianten(List.of(abfragevarianteRequestModel));
        requestModel.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitungRequestModel));

        final var entityInDb = new Bauleitplanverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setName("hallo");
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var entityInDbAbfragevariante = new AbfragevarianteBauleitplanverfahren();
        entityInDbAbfragevariante.setId(uuidAbfragevariante);

        final var entityInDbAbfragevarianteSachbearbeitung = new AbfragevarianteBauleitplanverfahren();
        entityInDbAbfragevarianteSachbearbeitung.setId(uuidAbfragevarianteSachbearbeitung);

        entityInDb.setAbfragevarianten(List.of(entityInDbAbfragevariante));
        entityInDb.setAbfragevariantenSachbearbeitung(List.of(entityInDbAbfragevarianteSachbearbeitung));

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final var entityToSave = new Bauleitplanverfahren();

        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setName("hallo");
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var abfragevarianteToSaveSave = new AbfragevarianteBauleitplanverfahren();
        abfragevarianteToSaveSave.setId(uuidAbfragevariante);
        abfragevarianteToSaveSave.setVersion(0L);
        final var abfragevarianteBedarfsmeldungToSave = new BedarfsmeldungFachreferate();
        abfragevarianteBedarfsmeldungToSave.setAnzahlEinrichtungen(5);
        abfragevarianteBedarfsmeldungToSave.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevarianteBedarfsmeldungToSave.setAnzahlKinderkrippengruppen(4);
        abfragevarianteBedarfsmeldungToSave.setAnzahlKindergartengruppen(3);
        abfragevarianteBedarfsmeldungToSave.setAnzahlHortgruppen(2);
        abfragevarianteBedarfsmeldungToSave.setAnzahlGrundschulzuege(1);
        abfragevarianteToSaveSave.setBedarfsmeldungFachreferate((List.of(abfragevarianteBedarfsmeldungToSave)));

        final var abfragevarianteSachbearbeitungToSave = new AbfragevarianteBauleitplanverfahren();
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
        abfragevarianteSachbearbeitungToSave.setBedarfsmeldungFachreferate(
            List.of(abfragevarianteSachbearbeitungBedarfsmeldungToSave)
        );

        entityToSave.setAbfragevarianten(List.of(abfragevarianteToSaveSave));
        entityToSave.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitungToSave));

        final var entitySaved = new Bauleitplanverfahren();

        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        entitySaved.setName("hallo");

        final var abfragevarianteSaved = new AbfragevarianteBauleitplanverfahren();
        abfragevarianteSaved.setId(uuidAbfragevariante);
        abfragevarianteSaved.setVersion(1L);
        final var abfragevarianteBedarfsmeldungSaved = new BedarfsmeldungFachreferate();
        abfragevarianteBedarfsmeldungSaved.setAnzahlEinrichtungen(5);
        abfragevarianteBedarfsmeldungSaved.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevarianteBedarfsmeldungSaved.setAnzahlKinderkrippengruppen(4);
        abfragevarianteBedarfsmeldungSaved.setAnzahlKindergartengruppen(3);
        abfragevarianteBedarfsmeldungSaved.setAnzahlHortgruppen(2);
        abfragevarianteBedarfsmeldungSaved.setAnzahlGrundschulzuege(1);
        abfragevarianteSaved.setBedarfsmeldungFachreferate(List.of(abfragevarianteBedarfsmeldungSaved));

        final var abfragevarianteSachbearbeitungSaved = new AbfragevarianteBauleitplanverfahren();
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
        abfragevarianteSachbearbeitungSaved.setBedarfsmeldungFachreferate(
            List.of(abfragevarianteSachbearbeitungBedarfsmeldungSaved)
        );

        entitySaved.setAbfragevarianten(List.of(abfragevarianteSaved));
        entitySaved.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitungSaved));

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final var result = this.abfrageService.patchInBearbeitungFachreferat(requestModel, uuid);

        final var expected = new BauleitplanverfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        expected.setId(uuid);
        expected.setVersion(1L);
        expected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        expected.setName("hallo");

        final var abfragevarianteExpected = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteExpected.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteExpected.setId(abfragevarianteSaved.getId());
        abfragevarianteExpected.setVersion(1L);
        final var abfragevarianteBedarfsmeldungExpected = new BedarfsmeldungFachreferateModel();
        abfragevarianteBedarfsmeldungExpected.setAnzahlEinrichtungen(5);
        abfragevarianteBedarfsmeldungExpected.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevarianteBedarfsmeldungExpected.setAnzahlKinderkrippengruppen(4);
        abfragevarianteBedarfsmeldungExpected.setAnzahlKindergartengruppen(3);
        abfragevarianteBedarfsmeldungExpected.setAnzahlHortgruppen(2);
        abfragevarianteBedarfsmeldungExpected.setAnzahlGrundschulzuege(1);
        abfragevarianteExpected.setBedarfsmeldungFachreferate(List.of(abfragevarianteBedarfsmeldungExpected));

        final var abfragevarianteSachbearbeitungExpected = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteSachbearbeitungExpected.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteSachbearbeitungExpected.setId(abfragevarianteSachbearbeitungSaved.getId());
        abfragevarianteSachbearbeitungExpected.setVersion(1L);
        final var abfragevarianteSachbearbeitungBedarfsmeldungExpected = new BedarfsmeldungFachreferateModel();
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setAnzahlEinrichtungen(1);
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setInfrastruktureinrichtungTyp(
            InfrastruktureinrichtungTyp.GRUNDSCHULE
        );
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setAnzahlKinderkrippengruppen(2);
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setAnzahlKindergartengruppen(3);
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setAnzahlHortgruppen(4);
        abfragevarianteSachbearbeitungBedarfsmeldungExpected.setAnzahlGrundschulzuege(5);
        abfragevarianteSachbearbeitungExpected.setBedarfsmeldungFachreferate(
            List.of(abfragevarianteSachbearbeitungBedarfsmeldungExpected)
        );

        expected.setAbfragevarianten(List.of(abfragevarianteExpected));
        expected.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitungExpected));

        assertThat(result, is(expected));
    }

    @Test
    void patchInBearbeitungFachreferatAbfrageNotSupported()
        throws UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException {
        final var uuid = UUID.randomUUID();
        final var uuidAbfragevariante = UUID.randomUUID();
        final var uuidAbfragevarianteSachbearbeitung = UUID.randomUUID();

        final var requestModel = new BauleitplanverfahrenInBearbeitungFachreferatModel();
        requestModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        requestModel.setVersion(0L);

        final var abfragevarianteRequestModel = new AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel();
        abfragevarianteRequestModel.setId(uuidAbfragevariante);
        abfragevarianteRequestModel.setVersion(0L);
        final var abfragevarianteBedarfsmeldung = new BedarfsmeldungFachreferateModel();
        abfragevarianteBedarfsmeldung.setAnzahlEinrichtungen(5);
        abfragevarianteBedarfsmeldung.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevarianteBedarfsmeldung.setAnzahlKinderkrippengruppen(4);
        abfragevarianteBedarfsmeldung.setAnzahlKindergartengruppen(3);
        abfragevarianteBedarfsmeldung.setAnzahlHortgruppen(2);
        abfragevarianteBedarfsmeldung.setAnzahlGrundschulzuege(1);
        abfragevarianteRequestModel.setBedarfsmeldungFachreferate(List.of(abfragevarianteBedarfsmeldung));

        final var abfragevarianteSachbearbeitungRequestModel =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel();
        abfragevarianteSachbearbeitungRequestModel.setId(uuidAbfragevarianteSachbearbeitung);
        abfragevarianteSachbearbeitungRequestModel.setVersion(0L);
        final var abfragevarianteSachbearbeitungBedarfsmeldung = new BedarfsmeldungFachreferateModel();
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlEinrichtungen(1);
        abfragevarianteSachbearbeitungBedarfsmeldung.setInfrastruktureinrichtungTyp(
            InfrastruktureinrichtungTyp.GRUNDSCHULE
        );
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlKinderkrippengruppen(2);
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlKindergartengruppen(3);
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlHortgruppen(4);
        abfragevarianteSachbearbeitungBedarfsmeldung.setAnzahlGrundschulzuege(5);
        abfragevarianteSachbearbeitungRequestModel.setBedarfsmeldungFachreferate(
            List.of(abfragevarianteSachbearbeitungBedarfsmeldung)
        );

        requestModel.setAbfragevarianten(List.of(abfragevarianteRequestModel));
        requestModel.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitungRequestModel));

        final var entityInDb = new Bauleitplanverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setName("hallo");
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var entityInDbAbfragevariante = new AbfragevarianteBauleitplanverfahren();
        entityInDbAbfragevariante.setId(uuidAbfragevariante);

        final var entityInDbAbfragevarianteSachbearbeitung = new AbfragevarianteBauleitplanverfahren();
        entityInDbAbfragevarianteSachbearbeitung.setId(uuidAbfragevarianteSachbearbeitung);

        entityInDb.setAbfragevarianten(List.of(entityInDbAbfragevariante));
        entityInDb.setAbfragevariantenSachbearbeitung(List.of(entityInDbAbfragevarianteSachbearbeitung));

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final var entityToSave = new Bauleitplanverfahren();

        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setName("hallo");
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var abfragevarianteToSaveSave = new AbfragevarianteBauleitplanverfahren();
        abfragevarianteToSaveSave.setId(uuidAbfragevariante);
        abfragevarianteToSaveSave.setVersion(0L);
        final var abfragevarianteBedarfsmeldungToSave = new BedarfsmeldungFachreferate();
        abfragevarianteBedarfsmeldungToSave.setAnzahlEinrichtungen(5);
        abfragevarianteBedarfsmeldungToSave.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevarianteBedarfsmeldungToSave.setAnzahlKinderkrippengruppen(4);
        abfragevarianteBedarfsmeldungToSave.setAnzahlKindergartengruppen(3);
        abfragevarianteBedarfsmeldungToSave.setAnzahlHortgruppen(2);
        abfragevarianteBedarfsmeldungToSave.setAnzahlGrundschulzuege(1);
        abfragevarianteToSaveSave.setBedarfsmeldungFachreferate((List.of(abfragevarianteBedarfsmeldungToSave)));

        final var abfragevarianteSachbearbeitungToSave = new AbfragevarianteBauleitplanverfahren();
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
        abfragevarianteSachbearbeitungToSave.setBedarfsmeldungFachreferate(
            List.of(abfragevarianteSachbearbeitungBedarfsmeldungToSave)
        );

        entityToSave.setAbfragevarianten(List.of(abfragevarianteToSaveSave));
        entityToSave.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitungToSave));

        final var entitySaved = new Bauleitplanverfahren();

        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        entitySaved.setName("hallo");

        final var abfragevarianteSaved = new AbfragevarianteBauleitplanverfahren();
        abfragevarianteSaved.setId(uuidAbfragevariante);
        abfragevarianteSaved.setVersion(1L);
        final var abfragevarianteBedarfsmeldungSaved = new BedarfsmeldungFachreferate();
        abfragevarianteBedarfsmeldungSaved.setAnzahlEinrichtungen(5);
        abfragevarianteBedarfsmeldungSaved.setInfrastruktureinrichtungTyp(InfrastruktureinrichtungTyp.KINDERKRIPPE);
        abfragevarianteBedarfsmeldungSaved.setAnzahlKinderkrippengruppen(4);
        abfragevarianteBedarfsmeldungSaved.setAnzahlKindergartengruppen(3);
        abfragevarianteBedarfsmeldungSaved.setAnzahlHortgruppen(2);
        abfragevarianteBedarfsmeldungSaved.setAnzahlGrundschulzuege(1);
        abfragevarianteSaved.setBedarfsmeldungFachreferate(List.of(abfragevarianteBedarfsmeldungSaved));

        final var abfragevarianteSachbearbeitungSaved = new AbfragevarianteBauleitplanverfahren();
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
        abfragevarianteSachbearbeitungSaved.setBedarfsmeldungFachreferate(
            List.of(abfragevarianteSachbearbeitungBedarfsmeldungSaved)
        );

        entitySaved.setAbfragevarianten(List.of(abfragevarianteSaved));
        entitySaved.setAbfragevariantenSachbearbeitung(List.of(abfragevarianteSachbearbeitungSaved));

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        try {
            this.abfrageService.patchInBearbeitungFachreferat(requestModel, uuid);
        } catch (final EntityNotFoundException exception) {
            assertThat(exception.getMessage(), is("Die Art der Abfrage wird nicht unterstützt."));
        }
    }

    @Test
    void deleteByIdAbfrageerstellung()
        throws UserRoleNotAllowedException, EntityIsReferencedException, EntityNotFoundException, AbfrageStatusNotAllowedException {
        final UUID id = UUID.randomUUID();

        String[] roles = { "abfrageerstellung" };
        String sub = "1234";

        final Bauleitplanverfahren entity = new Bauleitplanverfahren();
        entity.setId(id);
        entity.setSub(sub);
        entity.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        Mockito.when(this.abfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        this.abfrageService.deleteById(id);

        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteByIdAdmin()
        throws UserRoleNotAllowedException, EntityIsReferencedException, EntityNotFoundException, AbfrageStatusNotAllowedException {
        final UUID id = UUID.randomUUID();

        String[] roles = { "admin" };
        String sub = "1234";

        final Bauleitplanverfahren entity = new Bauleitplanverfahren();
        entity.setId(id);
        entity.setSub(sub);
        entity.setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.when(this.abfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        this.abfrageService.deleteById(id);

        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteByIdAdminNotSameSub()
        throws UserRoleNotAllowedException, EntityIsReferencedException, EntityNotFoundException, AbfrageStatusNotAllowedException {
        final UUID id = UUID.randomUUID();

        String[] roles = { "admin" };
        String sub = "1234";
        String userSub = "6789";

        final Bauleitplanverfahren entity = new Bauleitplanverfahren();
        entity.setId(id);
        entity.setSub(sub);
        entity.setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.when(this.abfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));

        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(userSub);

        this.abfrageService.deleteById(id);

        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.abfrageRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteByIdUserNotAllowedException() {
        final UUID id = UUID.randomUUID();
        String[] roles = { "abfrageerstellung" };
        String sub = "1234";
        final Bauleitplanverfahren entity = new Bauleitplanverfahren();
        entity.setId(id);
        entity.setSub("321");
        entity.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        Mockito.when(this.abfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.deleteById(id));

        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.abfrageRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void deleteByIdException() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();
        String[] roles = { "abfrageerstellung" };
        String sub = "1234";
        final Bauleitplanverfahren entity = new Bauleitplanverfahren();
        entity.setId(id);
        entity.setSub(sub);
        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(UUID.randomUUID());
        entity.setBauvorhaben(bauvorhaben);
        entity.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        final var bauvorhabenModel = new BauvorhabenModel();
        bauvorhabenModel.setId(bauvorhaben.getId());

        Mockito.when(this.abfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.bauvorhabenService.getBauvorhabenById(bauvorhaben.getId())).thenReturn(bauvorhabenModel);
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.abfrageService.deleteById(id));

        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.bauvorhabenService, Mockito.times(1)).getBauvorhabenById(bauvorhabenModel.getId());
        Mockito.verify(this.abfrageRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void deleteByIdStatusException() {
        final UUID id = UUID.randomUUID();
        String[] roles = { "abfrageerstellung" };
        String sub = "1234";
        final Bauleitplanverfahren entity = new Bauleitplanverfahren();
        entity.setId(id);
        entity.setSub(sub);
        entity.setStatusAbfrage(StatusAbfrage.OFFEN);

        Mockito.when(this.abfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Assertions.assertThrows(AbfrageStatusNotAllowedException.class, () -> this.abfrageService.deleteById(id));

        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.abfrageRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void deleteByIdNutzerException() {
        final UUID id = UUID.randomUUID();
        String[] roles = { "nutzer" };
        String sub = "1234";
        final Bauleitplanverfahren entity = new Bauleitplanverfahren();
        entity.setId(id);
        entity.setSub(sub);
        entity.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        Mockito.when(this.abfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Assertions.assertThrows(UserRoleNotAllowedException.class, () -> this.abfrageService.deleteById(id));

        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.abfrageRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenDeleteAbfrage()
        throws UserRoleNotAllowedException, AbfrageStatusNotAllowedException {
        final UUID id = UUID.randomUUID();
        String[] roles = { "abfrageerstellung", "admin" };
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        String sub = "1234";
        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);
        final var model = new BauleitplanverfahrenModel();
        model.setId(id);
        model.setSub(sub);
        model.setStatusAbfrage(StatusAbfrage.OFFEN);
        this.abfrageService.throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenDeleteAbfrage(model);

        roles = new String[] { "fachreferat" };
        model.setId(id);
        model.setSub(sub);
        model.setStatusAbfrage(StatusAbfrage.OFFEN);
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        try {
            this.abfrageService.throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenDeleteAbfrage(model);
        } catch (final UserRoleNotAllowedException exception) {
            assertThat(exception.getMessage(), is("Keine Berechtigung zum Löschen der Abfrage."));
        }

        roles = new String[] { "abfrageerstellung" };
        model.setId(id);
        model.setSub("321");
        model.setStatusAbfrage(StatusAbfrage.OFFEN);
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        try {
            this.abfrageService.throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenDeleteAbfrage(model);
        } catch (final UserRoleNotAllowedException exception) {
            assertThat(
                exception.getMessage(),
                is("Keine Berechtigung zum Löschen der Abfrage, da diese durch einen anderen Nutzer angelegt wurde.")
            );
        }

        roles = new String[] { "abfrageerstellung" };
        model.setId(id);
        model.setSub(sub);
        model.setStatusAbfrage(StatusAbfrage.OFFEN);
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        try {
            this.abfrageService.throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenDeleteAbfrage(model);
        } catch (final AbfrageStatusNotAllowedException exception) {
            assertThat(exception.getMessage(), is("Die Abfrage kann nur im Status 'angelegt' gelöscht werden."));
        }

        roles = new String[] { "abfrageerstellung" };
        model.setId(id);
        model.setSub(sub);
        model.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        this.abfrageService.throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenDeleteAbfrage(model);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben()
        throws EntityIsReferencedException, EntityNotFoundException {
        this.abfrageService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(
                new BauleitplanverfahrenModel()
            );

        final BauleitplanverfahrenModel abfrage = new BauleitplanverfahrenModel();
        abfrage.setBauvorhaben(UUID.randomUUID());

        final var bauvorhabenModel = new BauvorhabenModel();
        bauvorhabenModel.setId(abfrage.getBauvorhaben());

        Mockito.when(this.bauvorhabenService.getBauvorhabenById(bauvorhabenModel.getId())).thenReturn(bauvorhabenModel);
        Assertions.assertThrows(
            EntityIsReferencedException.class,
            () -> this.abfrageService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(abfrage)
        );
    }

    @Test
    void throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid() throws AbfrageStatusNotAllowedException {
        final var model = new BauleitplanverfahrenModel();
        model.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        this.abfrageService.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                model,
                StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE
            );

        Assertions.assertThrows(
            AbfrageStatusNotAllowedException.class,
            () ->
                this.abfrageService.throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                        model,
                        StatusAbfrage.ANGELEGT
                    )
        );
    }
}
