package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapperImpl;
import de.muenchen.isi.domain.mapper.AbfragevarianteDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapperImpl;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteBaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BedarfsmeldungFachreferateModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteWeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BaugenehmigungsverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.WeiteresVerfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.AbfragevarianteWeiteresVerfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BaugenehmigungsverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.BauleitplanverfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat.WeiteresVerfahrenInBearbeitungFachreferatModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.BauleitplanverfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung.WeiteresVerfahrenInBearbeitungSachbearbeitungModel;
import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.service.calculation.CalculationService;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteBaugenehmigungsverfahren;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteBauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteWeiteresVerfahren;
import de.muenchen.isi.infrastructure.entity.Baugenehmigungsverfahren;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.BedarfsmeldungFachreferate;
import de.muenchen.isi.infrastructure.entity.WeiteresVerfahren;
import de.muenchen.isi.infrastructure.entity.common.Stadtbezirk;
import de.muenchen.isi.infrastructure.entity.common.Verortung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.infrastructure.repository.AbfragevarianteBaugenehmigungsverfahrenRepository;
import de.muenchen.isi.infrastructure.repository.AbfragevarianteBauleitplanverfahrenRepository;
import de.muenchen.isi.infrastructure.repository.AbfragevarianteWeiteresVerfahrenRepository;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    @Mock
    private AbfragevarianteBauleitplanverfahrenRepository abfragevarianteBauleitplanverfahrenRepository;

    @Mock
    private AbfragevarianteBaugenehmigungsverfahrenRepository abfragevarianteBaugenehmigungsverfahrenRepository;

    @Mock
    private AbfragevarianteWeiteresVerfahrenRepository abfragevarianteWeiteresVerfahrenRepository;

    private AbfrageDomainMapper abfrageDomainMapper;

    @Mock
    private BauvorhabenRepository bauvorhabenRepository;

    @Mock
    private DokumentService dokumentService;

    @Mock
    private AuthenticationUtils authenticationUtils;

    @Mock
    private CalculationService calculationService;

    @BeforeEach
    public void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        final var abfragevarianteDomainMapper = new AbfragevarianteDomainMapperImpl(new BauabschnittDomainMapperImpl());
        this.abfrageDomainMapper =
            new AbfrageDomainMapperImpl(abfragevarianteDomainMapper, new DokumentDomainMapperImpl());
        Field field = abfrageDomainMapper.getClass().getSuperclass().getDeclaredField("abfragevarianteDomainMapper");
        field.setAccessible(true);
        field.set(abfrageDomainMapper, abfragevarianteDomainMapper);
        this.abfrageService =
            new AbfrageService(
                this.abfrageRepository,
                this.abfrageDomainMapper,
                this.bauvorhabenRepository,
                this.dokumentService,
                this.authenticationUtils,
                this.abfragevarianteBauleitplanverfahrenRepository,
                this.abfragevarianteBaugenehmigungsverfahrenRepository,
                this.abfragevarianteWeiteresVerfahrenRepository,
                this.calculationService
            );
        Mockito.reset(
            this.abfrageRepository,
            this.bauvorhabenRepository,
            this.dokumentService,
            this.authenticationUtils,
            this.abfragevarianteBauleitplanverfahrenRepository,
            this.abfragevarianteBaugenehmigungsverfahrenRepository,
            this.abfragevarianteWeiteresVerfahrenRepository,
            this.calculationService
        );
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
    void saveAbfrageWithId()
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, CalculationException {
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
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(UUID.randomUUID());
        Mockito
            .verify(this.calculationService, Mockito.times(1))
            .calculateAndAppendLangfristigerPlanungsursaechlicherBedarfToEachAbfragevarianteOfAbfrage(abfrage);
    }

    @Test
    void saveAbfrageWithoutId()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, CalculationException {
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
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(UUID.randomUUID());
        Mockito
            .verify(this.calculationService, Mockito.times(1))
            .calculateAndAppendLangfristigerPlanungsursaechlicherBedarfToEachAbfragevarianteOfAbfrage(abfrage);
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
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(UUID.randomUUID());
    }

    @Test
    void patchAngelegtBauleitplanverfahren()
        throws EntityNotFoundException, UniqueViolationException, FileHandlingFailedException, FileHandlingWithS3FailedException, OptimisticLockingException, AbfrageStatusNotAllowedException, CalculationException {
        final UUID abfrageId = UUID.randomUUID();

        final BauleitplanverfahrenAngelegtModel requestModel = new BauleitplanverfahrenAngelegtModel();
        requestModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        requestModel.setName("hallo");

        final AbfragevarianteBauleitplanverfahrenAngelegtModel abfragevarianteRequestModel =
            new AbfragevarianteBauleitplanverfahrenAngelegtModel();
        abfragevarianteRequestModel.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteRequestModel.setName("Abfragevariante");
        requestModel.setAbfragevariantenBauleitplanverfahren(List.of(abfragevarianteRequestModel));

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
    void patchAngelegtBaugenehmigungsverfahren()
        throws EntityNotFoundException, UniqueViolationException, FileHandlingFailedException, FileHandlingWithS3FailedException, OptimisticLockingException, AbfrageStatusNotAllowedException, CalculationException {
        final UUID abfrageId = UUID.randomUUID();

        final BaugenehmigungsverfahrenAngelegtModel requestModel = new BaugenehmigungsverfahrenAngelegtModel();
        requestModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        requestModel.setName("hallo");

        final AbfragevarianteBaugenehmigungsverfahrenAngelegtModel abfragevarianteRequestModel =
            new AbfragevarianteBaugenehmigungsverfahrenAngelegtModel();
        abfragevarianteRequestModel.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevarianteRequestModel.setName("Abfragevariante");
        requestModel.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevarianteRequestModel));

        final BaugenehmigungsverfahrenModel model = new BaugenehmigungsverfahrenModel();
        model.setId(abfrageId);
        model.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        final BaugenehmigungsverfahrenModel abfrageModelMapped =
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
    void patchAngelegtWeiteresVerfahren()
        throws EntityNotFoundException, UniqueViolationException, FileHandlingFailedException, FileHandlingWithS3FailedException, OptimisticLockingException, AbfrageStatusNotAllowedException, CalculationException {
        final UUID abfrageId = UUID.randomUUID();

        final WeiteresVerfahrenAngelegtModel requestModel = new WeiteresVerfahrenAngelegtModel();
        requestModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        requestModel.setName("hallo");

        final AbfragevarianteWeiteresVerfahrenAngelegtModel abfragevarianteRequestModel =
            new AbfragevarianteWeiteresVerfahrenAngelegtModel();
        abfragevarianteRequestModel.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevarianteRequestModel.setName("Abfragevariante");
        requestModel.setAbfragevariantenWeiteresVerfahren(List.of(abfragevarianteRequestModel));

        final WeiteresVerfahrenModel model = new WeiteresVerfahrenModel();
        model.setId(abfrageId);
        model.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        final WeiteresVerfahrenModel abfrageModelMapped = this.abfrageDomainMapper.request2Model(requestModel, model);
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
    void patchAngelegtArtAbfrageNotSupportedBauleitplanverfahren()
        throws UniqueViolationException, FileHandlingFailedException, FileHandlingWithS3FailedException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException {
        final UUID abfrageId = UUID.randomUUID();

        final BauleitplanverfahrenAngelegtModel requestModel = new BauleitplanverfahrenAngelegtModel();
        requestModel.setName("hallo");

        final AbfragevarianteBauleitplanverfahrenAngelegtModel abfragevarianteRequestModel =
            new AbfragevarianteBauleitplanverfahrenAngelegtModel();
        abfragevarianteRequestModel.setName("Abfragevariante");
        requestModel.setAbfragevariantenBauleitplanverfahren(List.of(abfragevarianteRequestModel));

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
    void patchAngelegtArtAbfrageNotSupportedBaugenehmigungsverfahren()
        throws UniqueViolationException, FileHandlingFailedException, FileHandlingWithS3FailedException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException {
        final UUID abfrageId = UUID.randomUUID();

        final BaugenehmigungsverfahrenAngelegtModel requestModel = new BaugenehmigungsverfahrenAngelegtModel();
        requestModel.setName("hallo");

        final AbfragevarianteBaugenehmigungsverfahrenAngelegtModel abfragevarianteRequestModel =
            new AbfragevarianteBaugenehmigungsverfahrenAngelegtModel();
        abfragevarianteRequestModel.setName("Abfragevariante");
        requestModel.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevarianteRequestModel));

        final BaugenehmigungsverfahrenModel model = new BaugenehmigungsverfahrenModel();
        model.setId(abfrageId);
        model.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        final BaugenehmigungsverfahrenModel abfrageModelMapped =
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
    void patchAngelegtArtAbfrageNotSupportedWeiteresVerfahren()
        throws UniqueViolationException, FileHandlingFailedException, FileHandlingWithS3FailedException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException {
        final UUID abfrageId = UUID.randomUUID();

        final var requestModel = new WeiteresVerfahrenAngelegtModel();
        requestModel.setName("hallo");

        final AbfragevarianteWeiteresVerfahrenAngelegtModel abfragevarianteRequestModel =
            new AbfragevarianteWeiteresVerfahrenAngelegtModel();
        abfragevarianteRequestModel.setName("Abfragevariante");
        requestModel.setAbfragevariantenWeiteresVerfahren(List.of(abfragevarianteRequestModel));

        final WeiteresVerfahrenModel model = new WeiteresVerfahrenModel();
        model.setId(abfrageId);
        model.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        final WeiteresVerfahrenModel abfrageModelMapped = this.abfrageDomainMapper.request2Model(requestModel, model);
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
    void patchInBearbeitungSachbearbeitungBauleitplanverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException {
        final var uuid = UUID.randomUUID();

        final StadtbezirkModel abfrage_sb_08 = new StadtbezirkModel();
        abfrage_sb_08.setNummer("08");
        abfrage_sb_08.setName("Stadtbezirk 8");

        final StadtbezirkModel abfraqe_sb_20 = new StadtbezirkModel();
        abfraqe_sb_20.setNummer("20");
        abfraqe_sb_20.setName("Stadtbezirk 20");

        final VerortungModel abfrageVerortung = new VerortungModel();
        abfrageVerortung.setStadtbezirke(Stream.of(abfraqe_sb_20, abfrage_sb_08).collect(Collectors.toSet()));

        final var requestModel = new BauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        requestModel.setVersion(0L);
        requestModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        requestModel.setVerortung(abfrageVerortung);
        final var abfragevarianteSachbearbeitung =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteSachbearbeitung.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung.setName("Abfragevariante 1");
        abfragevarianteSachbearbeitung.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevarianteSachbearbeitung.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2017);
        abfragevarianteSachbearbeitung.setAnmerkung("Test Anmerkung");
        requestModel.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevarianteSachbearbeitung));

        final var entityInDb = new Bauleitplanverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityInDb.setName("hallo");

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final Stadtbezirk abfrageEntity_sb_08 = new Stadtbezirk();
        abfrageEntity_sb_08.setNummer("08");
        abfrageEntity_sb_08.setName("Stadtbezirk 8");

        final Stadtbezirk abfraqgeEntity_sb_20 = new Stadtbezirk();
        abfraqgeEntity_sb_20.setNummer("20");
        abfraqgeEntity_sb_20.setName("Stadtbezirk 20");

        final Verortung abfrageEntityVerortung = new Verortung();
        abfrageEntityVerortung.setStadtbezirke(
            Stream.of(abfraqgeEntity_sb_20, abfrageEntity_sb_08).collect(Collectors.toSet())
        );

        final var entityToSave = new Bauleitplanverfahren();
        entityToSave.setAbfragevariantenBauleitplanverfahren(List.of());
        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityToSave.setName("hallo");
        entityToSave.setVerortung(abfrageEntityVerortung);
        final var abfragevariante1ToSave = new AbfragevarianteBauleitplanverfahren();
        abfragevariante1ToSave.setAbfragevariantenNr(1);
        abfragevariante1ToSave.setName("Abfragevariante 1");
        abfragevariante1ToSave.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1ToSave.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariante1ToSave.setAnmerkung("Test Anmerkung");
        entityToSave.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante1ToSave));

        final var entitySaved = new Bauleitplanverfahren();
        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entitySaved.setName("hallo");
        entitySaved.setVerortung(abfrageEntityVerortung);
        final var abfragevariante1Saved = new AbfragevarianteBauleitplanverfahren();
        abfragevariante1Saved.setId(UUID.randomUUID());
        abfragevariante1Saved.setAbfragevariantenNr(1);
        abfragevariante1Saved.setName("Abfragevariante 1");
        abfragevariante1Saved.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1Saved.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariante1Saved.setAnmerkung("Test Anmerkung");
        entitySaved.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante1Saved));

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final var result = this.abfrageService.patchInBearbeitungSachbearbeitung(requestModel, uuid);

        final var expected = new BauleitplanverfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        expected.setId(uuid);
        expected.setVersion(1L);
        expected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        expected.setName("hallo");
        expected.setVerortung(abfrageVerortung);
        final var abfragevariante1Expected = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante1Expected.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevariante1Expected.setId(abfragevariante1Saved.getId());
        abfragevariante1Expected.setAbfragevariantenNr(1);
        abfragevariante1Expected.setName("Abfragevariante 1");
        abfragevariante1Expected.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1Expected.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2017);
        abfragevariante1Expected.setAnmerkung("Test Anmerkung");
        expected.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante1Expected));

        assertThat(result, is(expected));
    }

    @Test
    void patchInBearbeitungSachbearbeitungBaugenehmigungsverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException {
        final var uuid = UUID.randomUUID();

        final StadtbezirkModel abfrage_sb_08 = new StadtbezirkModel();
        abfrage_sb_08.setNummer("08");
        abfrage_sb_08.setName("Stadtbezirk 8");

        final StadtbezirkModel abfraqe_sb_20 = new StadtbezirkModel();
        abfraqe_sb_20.setNummer("20");
        abfraqe_sb_20.setName("Stadtbezirk 20");

        final VerortungModel abfrageVerortung = new VerortungModel();
        abfrageVerortung.setStadtbezirke(Stream.of(abfraqe_sb_20, abfrage_sb_08).collect(Collectors.toSet()));

        final var requestModel = new BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        requestModel.setVersion(0L);
        requestModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        requestModel.setVerortung(abfrageVerortung);
        final var abfragevarianteSachbearbeitung =
            new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevarianteSachbearbeitung.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung.setName("Abfragevariante 1");
        abfragevarianteSachbearbeitung.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevarianteSachbearbeitung.setAnmerkung("Test Anmerkung");
        requestModel.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitung)
        );

        final var entityInDb = new Baugenehmigungsverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityInDb.setName("hallo");

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final Stadtbezirk abfrageEntity_sb_08 = new Stadtbezirk();
        abfrageEntity_sb_08.setNummer("08");
        abfrageEntity_sb_08.setName("Stadtbezirk 8");

        final Stadtbezirk abfraqgeEntity_sb_20 = new Stadtbezirk();
        abfraqgeEntity_sb_20.setNummer("20");
        abfraqgeEntity_sb_20.setName("Stadtbezirk 20");

        final Verortung abfrageEntityVerortung = new Verortung();
        abfrageEntityVerortung.setStadtbezirke(
            Stream.of(abfraqgeEntity_sb_20, abfrageEntity_sb_08).collect(Collectors.toSet())
        );

        final var entityToSave = new Baugenehmigungsverfahren();
        entityToSave.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityToSave.setName("hallo");
        entityToSave.setVerortung(abfrageEntityVerortung);
        final var abfragevariante1ToSave = new AbfragevarianteBaugenehmigungsverfahren();
        abfragevariante1ToSave.setAbfragevariantenNr(1);
        abfragevariante1ToSave.setName("Abfragevariante 1");
        abfragevariante1ToSave.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1ToSave.setAnmerkung("Test Anmerkung");
        entityToSave.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of(abfragevariante1ToSave));

        final var entitySaved = new Baugenehmigungsverfahren();
        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entitySaved.setName("hallo");
        entitySaved.setVerortung(abfrageEntityVerortung);
        final var abfragevariante1Saved = new AbfragevarianteBaugenehmigungsverfahren();
        abfragevariante1Saved.setId(UUID.randomUUID());
        abfragevariante1Saved.setAbfragevariantenNr(1);
        abfragevariante1Saved.setName("Abfragevariante 1");
        abfragevariante1Saved.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1Saved.setAnmerkung("Test Anmerkung");
        entitySaved.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of(abfragevariante1Saved));

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final var result = this.abfrageService.patchInBearbeitungSachbearbeitung(requestModel, uuid);

        final var expected = new BaugenehmigungsverfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        expected.setId(uuid);
        expected.setVersion(1L);
        expected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        expected.setName("hallo");
        expected.setVerortung(abfrageVerortung);
        final var abfragevariante1Expected = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante1Expected.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevariante1Expected.setId(abfragevariante1Saved.getId());
        abfragevariante1Expected.setAbfragevariantenNr(1);
        abfragevariante1Expected.setName("Abfragevariante 1");
        abfragevariante1Expected.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1Expected.setAnmerkung("Test Anmerkung");
        expected.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of(abfragevariante1Expected));

        assertThat(result, is(expected));
    }

    @Test
    void patchInBearbeitungSachbearbeitungWeiteresVerfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException {
        final var uuid = UUID.randomUUID();

        final StadtbezirkModel abfrage_sb_08 = new StadtbezirkModel();
        abfrage_sb_08.setNummer("08");
        abfrage_sb_08.setName("Stadtbezirk 8");

        final StadtbezirkModel abfraqe_sb_20 = new StadtbezirkModel();
        abfraqe_sb_20.setNummer("20");
        abfraqe_sb_20.setName("Stadtbezirk 20");

        final VerortungModel abfrageVerortung = new VerortungModel();
        abfrageVerortung.setStadtbezirke(Stream.of(abfraqe_sb_20, abfrage_sb_08).collect(Collectors.toSet()));

        final var requestModel = new WeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        requestModel.setVersion(0L);
        requestModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        requestModel.setVerortung(abfrageVerortung);
        final var abfragevarianteSachbearbeitung =
            new AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevarianteSachbearbeitung.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung.setName("Abfragevariante 1");
        abfragevarianteSachbearbeitung.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevarianteSachbearbeitung.setAnmerkung("Test Anmerkung");
        requestModel.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevarianteSachbearbeitung));

        final var entityInDb = new WeiteresVerfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityInDb.setName("hallo");

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final Stadtbezirk abfrageEntity_sb_08 = new Stadtbezirk();
        abfrageEntity_sb_08.setNummer("08");
        abfrageEntity_sb_08.setName("Stadtbezirk 8");

        final Stadtbezirk abfraqgeEntity_sb_20 = new Stadtbezirk();
        abfraqgeEntity_sb_20.setNummer("20");
        abfraqgeEntity_sb_20.setName("Stadtbezirk 20");

        final Verortung abfrageEntityVerortung = new Verortung();
        abfrageEntityVerortung.setStadtbezirke(
            Stream.of(abfraqgeEntity_sb_20, abfrageEntity_sb_08).collect(Collectors.toSet())
        );

        final var entityToSave = new WeiteresVerfahren();
        entityToSave.setAbfragevariantenWeiteresVerfahren(List.of());
        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityToSave.setName("hallo");
        entityToSave.setVerortung(abfrageEntityVerortung);
        final var abfragevariante1ToSave = new AbfragevarianteWeiteresVerfahren();
        abfragevariante1ToSave.setAbfragevariantenNr(1);
        abfragevariante1ToSave.setName("Abfragevariante 1");
        abfragevariante1ToSave.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1ToSave.setAnmerkung("Test Anmerkung");
        entityToSave.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante1ToSave));

        final var entitySaved = new WeiteresVerfahren();
        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entitySaved.setName("hallo");
        entitySaved.setVerortung(abfrageEntityVerortung);
        final var abfragevariante1Saved = new AbfragevarianteWeiteresVerfahren();
        abfragevariante1Saved.setId(UUID.randomUUID());
        abfragevariante1Saved.setAbfragevariantenNr(1);
        abfragevariante1Saved.setName("Abfragevariante 1");
        abfragevariante1Saved.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1Saved.setAnmerkung("Test Anmerkung");
        entitySaved.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante1Saved));

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final var result = this.abfrageService.patchInBearbeitungSachbearbeitung(requestModel, uuid);

        final var expected = new WeiteresVerfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        expected.setId(uuid);
        expected.setVersion(1L);
        expected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        expected.setName("hallo");
        expected.setVerortung(abfrageVerortung);
        final var abfragevariante1Expected = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante1Expected.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevariante1Expected.setId(abfragevariante1Saved.getId());
        abfragevariante1Expected.setAbfragevariantenNr(1);
        abfragevariante1Expected.setName("Abfragevariante 1");
        abfragevariante1Expected.setGfWohnenPlanungsursaechlich(BigDecimal.TEN);
        abfragevariante1Expected.setAnmerkung("Test Anmerkung");
        expected.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante1Expected));

        assertThat(result, is(expected));
    }

    @Test
    void patchInBearbeitungSachbearbeitungAbfrageNotSupportedBauleitplanverfahren()
        throws UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, CalculationException {
        final var uuid = UUID.randomUUID();

        final var requestModel = new BauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        requestModel.setVersion(0L);
        requestModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        final var abfragevarianteSachbearbeitung =
            new AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung.setName("Abfragevariante 1");
        requestModel.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevarianteSachbearbeitung));

        final var entityInDb = new Bauleitplanverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityInDb.setName("hallo");

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final var entityToSave = new Bauleitplanverfahren();
        entityToSave.setAbfragevariantenBauleitplanverfahren(List.of());
        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityToSave.setName("hallo");
        final var abfragevariante1ToSave = new AbfragevarianteBauleitplanverfahren();
        abfragevariante1ToSave.setAbfragevariantenNr(1);
        abfragevariante1ToSave.setName("Abfragevariante 1");
        entityToSave.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante1ToSave));

        final var entitySaved = new Bauleitplanverfahren();
        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entitySaved.setName("hallo");
        final var abfragevariante1Saved = new AbfragevarianteBauleitplanverfahren();
        abfragevariante1Saved.setId(UUID.randomUUID());
        abfragevariante1Saved.setAbfragevariantenNr(1);
        abfragevariante1Saved.setName("Abfragevariante 1");
        entitySaved.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante1Saved));

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        try {
            this.abfrageService.patchInBearbeitungSachbearbeitung(requestModel, uuid);
        } catch (final EntityNotFoundException exception) {
            assertThat(exception.getMessage(), is("Die Art der Abfrage wird nicht unterstützt."));
        }
    }

    @Test
    void patchInBearbeitungSachbearbeitungAbfrageNotSupportedBaugenehmigungsverfahren()
        throws UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, CalculationException {
        final var uuid = UUID.randomUUID();

        final var requestModel = new BaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        requestModel.setVersion(0L);
        requestModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        final var abfragevarianteSachbearbeitung =
            new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung.setName("Abfragevariante 1");
        requestModel.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitung)
        );

        final var entityInDb = new Baugenehmigungsverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityInDb.setName("hallo");

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final var entityToSave = new Baugenehmigungsverfahren();
        entityToSave.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityToSave.setName("hallo");
        final var abfragevariante1ToSave = new AbfragevarianteBaugenehmigungsverfahren();
        abfragevariante1ToSave.setAbfragevariantenNr(1);
        abfragevariante1ToSave.setName("Abfragevariante 1");
        entityToSave.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of(abfragevariante1ToSave));

        final var entitySaved = new Baugenehmigungsverfahren();
        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entitySaved.setName("hallo");
        final var abfragevariante1Saved = new AbfragevarianteBaugenehmigungsverfahren();
        abfragevariante1Saved.setId(UUID.randomUUID());
        abfragevariante1Saved.setAbfragevariantenNr(1);
        abfragevariante1Saved.setName("Abfragevariante 1");
        entitySaved.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of(abfragevariante1Saved));

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        try {
            this.abfrageService.patchInBearbeitungSachbearbeitung(requestModel, uuid);
        } catch (final EntityNotFoundException exception) {
            assertThat(exception.getMessage(), is("Die Art der Abfrage wird nicht unterstützt."));
        }
    }

    @Test
    void patchInBearbeitungSachbearbeitungAbfrageNotSupportedWeiteresVerfahren()
        throws UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, CalculationException {
        final var uuid = UUID.randomUUID();

        final var requestModel = new WeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        requestModel.setVersion(0L);
        requestModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        final var abfragevarianteSachbearbeitung =
            new AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel();
        abfragevarianteSachbearbeitung.setAbfragevariantenNr(1);
        abfragevarianteSachbearbeitung.setName("Abfragevariante 1");
        requestModel.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevarianteSachbearbeitung));

        final var entityInDb = new WeiteresVerfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityInDb.setName("hallo");

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final var entityToSave = new WeiteresVerfahren();
        entityToSave.setAbfragevariantenWeiteresVerfahren(List.of());
        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entityToSave.setName("hallo");
        final var abfragevariante1ToSave = new AbfragevarianteWeiteresVerfahren();
        abfragevariante1ToSave.setAbfragevariantenNr(1);
        abfragevariante1ToSave.setName("Abfragevariante 1");
        entityToSave.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante1ToSave));

        final var entitySaved = new WeiteresVerfahren();
        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        entitySaved.setName("hallo");
        final var abfragevariante1Saved = new AbfragevarianteWeiteresVerfahren();
        abfragevariante1Saved.setId(UUID.randomUUID());
        abfragevariante1Saved.setAbfragevariantenNr(1);
        abfragevariante1Saved.setName("Abfragevariante 1");
        entitySaved.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante1Saved));

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        try {
            this.abfrageService.patchInBearbeitungSachbearbeitung(requestModel, uuid);
        } catch (final EntityNotFoundException exception) {
            assertThat(exception.getMessage(), is("Die Art der Abfrage wird nicht unterstützt."));
        }
    }

    @Test
    void patchInBearbeitungFachreferatBauleitplanverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException {
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

        requestModel.setAbfragevariantenBauleitplanverfahren(List.of(abfragevarianteRequestModel));
        requestModel.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(abfragevarianteSachbearbeitungRequestModel)
        );

        final var entityInDb = new Bauleitplanverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setName("hallo");
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var entityInDbAbfragevariante = new AbfragevarianteBauleitplanverfahren();
        entityInDbAbfragevariante.setId(uuidAbfragevariante);

        final var entityInDbAbfragevarianteSachbearbeitung = new AbfragevarianteBauleitplanverfahren();
        entityInDbAbfragevarianteSachbearbeitung.setId(uuidAbfragevarianteSachbearbeitung);

        entityInDb.setAbfragevariantenBauleitplanverfahren(List.of(entityInDbAbfragevariante));
        entityInDb.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(entityInDbAbfragevarianteSachbearbeitung)
        );

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

        entityToSave.setAbfragevariantenBauleitplanverfahren(List.of(abfragevarianteToSaveSave));
        entityToSave.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(abfragevarianteSachbearbeitungToSave)
        );

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

        entitySaved.setAbfragevariantenBauleitplanverfahren(List.of(abfragevarianteSaved));
        entitySaved.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(abfragevarianteSachbearbeitungSaved)
        );

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

        expected.setAbfragevariantenBauleitplanverfahren(List.of(abfragevarianteExpected));
        expected.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(abfragevarianteSachbearbeitungExpected)
        );

        assertThat(result, is(expected));
    }

    @Test
    void patchInBearbeitungFachreferatBaugenehmigungsverfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException {
        final var uuid = UUID.randomUUID();
        final var uuidAbfragevariante = UUID.randomUUID();
        final var uuidAbfragevarianteSachbearbeitung = UUID.randomUUID();

        final var requestModel = new BaugenehmigungsverfahrenInBearbeitungFachreferatModel();
        requestModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        requestModel.setVersion(0L);

        final var abfragevarianteRequestModel =
            new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel();
        abfragevarianteRequestModel.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
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
            new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel();
        abfragevarianteSachbearbeitungRequestModel.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
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

        requestModel.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevarianteRequestModel));
        requestModel.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitungRequestModel)
        );

        final var entityInDb = new Baugenehmigungsverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setName("hallo");
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var entityInDbAbfragevariante = new AbfragevarianteBaugenehmigungsverfahren();
        entityInDbAbfragevariante.setId(uuidAbfragevariante);

        final var entityInDbAbfragevarianteSachbearbeitung = new AbfragevarianteBaugenehmigungsverfahren();
        entityInDbAbfragevarianteSachbearbeitung.setId(uuidAbfragevarianteSachbearbeitung);

        entityInDb.setAbfragevariantenBaugenehmigungsverfahren(List.of(entityInDbAbfragevariante));
        entityInDb.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(entityInDbAbfragevarianteSachbearbeitung)
        );

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final var entityToSave = new Baugenehmigungsverfahren();

        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setName("hallo");
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var abfragevarianteToSaveSave = new AbfragevarianteBaugenehmigungsverfahren();
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

        final var abfragevarianteSachbearbeitungToSave = new AbfragevarianteBaugenehmigungsverfahren();
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

        entityToSave.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevarianteToSaveSave));
        entityToSave.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitungToSave)
        );

        final var entitySaved = new Baugenehmigungsverfahren();

        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        entitySaved.setName("hallo");

        final var abfragevarianteSaved = new AbfragevarianteBaugenehmigungsverfahren();
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

        final var abfragevarianteSachbearbeitungSaved = new AbfragevarianteBaugenehmigungsverfahren();
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

        entitySaved.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevarianteSaved));
        entitySaved.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitungSaved)
        );

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final var result = this.abfrageService.patchInBearbeitungFachreferat(requestModel, uuid);

        final var expected = new BaugenehmigungsverfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        expected.setId(uuid);
        expected.setVersion(1L);
        expected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        expected.setName("hallo");

        final var abfragevarianteExpected = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevarianteExpected.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
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

        final var abfragevarianteSachbearbeitungExpected = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevarianteSachbearbeitungExpected.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
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

        expected.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevarianteExpected));
        expected.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitungExpected)
        );

        assertThat(result, is(expected));
    }

    @Test
    void patchInBearbeitungFachreferatWeiteresVerfahren()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException {
        final var uuid = UUID.randomUUID();
        final var uuidAbfragevariante = UUID.randomUUID();
        final var uuidAbfragevarianteSachbearbeitung = UUID.randomUUID();

        final var requestModel = new WeiteresVerfahrenInBearbeitungFachreferatModel();
        requestModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        requestModel.setVersion(0L);

        final var abfragevarianteRequestModel = new AbfragevarianteWeiteresVerfahrenInBearbeitungFachreferatModel();
        abfragevarianteRequestModel.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
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
            new AbfragevarianteWeiteresVerfahrenInBearbeitungFachreferatModel();
        abfragevarianteSachbearbeitungRequestModel.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
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

        requestModel.setAbfragevariantenWeiteresVerfahren(List.of(abfragevarianteRequestModel));
        requestModel.setAbfragevariantenSachbearbeitungWeiteresVerfahren(
            List.of(abfragevarianteSachbearbeitungRequestModel)
        );

        final var entityInDb = new WeiteresVerfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setName("hallo");
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var entityInDbAbfragevariante = new AbfragevarianteWeiteresVerfahren();
        entityInDbAbfragevariante.setId(uuidAbfragevariante);

        final var entityInDbAbfragevarianteSachbearbeitung = new AbfragevarianteWeiteresVerfahren();
        entityInDbAbfragevarianteSachbearbeitung.setId(uuidAbfragevarianteSachbearbeitung);

        entityInDb.setAbfragevariantenWeiteresVerfahren(List.of(entityInDbAbfragevariante));
        entityInDb.setAbfragevariantenSachbearbeitungWeiteresVerfahren(
            List.of(entityInDbAbfragevarianteSachbearbeitung)
        );

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final var entityToSave = new WeiteresVerfahren();

        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setName("hallo");
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var abfragevarianteToSaveSave = new AbfragevarianteWeiteresVerfahren();
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

        final var abfragevarianteSachbearbeitungToSave = new AbfragevarianteWeiteresVerfahren();
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

        entityToSave.setAbfragevariantenWeiteresVerfahren(List.of(abfragevarianteToSaveSave));
        entityToSave.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevarianteSachbearbeitungToSave));

        final var entitySaved = new WeiteresVerfahren();

        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        entitySaved.setName("hallo");

        final var abfragevarianteSaved = new AbfragevarianteWeiteresVerfahren();
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

        final var abfragevarianteSachbearbeitungSaved = new AbfragevarianteWeiteresVerfahren();
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

        entitySaved.setAbfragevariantenWeiteresVerfahren(List.of(abfragevarianteSaved));
        entitySaved.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevarianteSachbearbeitungSaved));

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        final var result = this.abfrageService.patchInBearbeitungFachreferat(requestModel, uuid);

        final var expected = new WeiteresVerfahrenModel();
        expected.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        expected.setId(uuid);
        expected.setVersion(1L);
        expected.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        expected.setName("hallo");

        final var abfragevarianteExpected = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteExpected.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
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

        final var abfragevarianteSachbearbeitungExpected = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteSachbearbeitungExpected.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
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

        expected.setAbfragevariantenWeiteresVerfahren(List.of(abfragevarianteExpected));
        expected.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevarianteSachbearbeitungExpected));

        assertThat(result, is(expected));
    }

    @Test
    void patchInBearbeitungFachreferatAbfrageNotSupportedBauleitplanverfahren()
        throws UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, CalculationException {
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

        requestModel.setAbfragevariantenBauleitplanverfahren(List.of(abfragevarianteRequestModel));
        requestModel.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(abfragevarianteSachbearbeitungRequestModel)
        );

        final var entityInDb = new Bauleitplanverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setName("hallo");
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var entityInDbAbfragevariante = new AbfragevarianteBauleitplanverfahren();
        entityInDbAbfragevariante.setId(uuidAbfragevariante);

        final var entityInDbAbfragevarianteSachbearbeitung = new AbfragevarianteBauleitplanverfahren();
        entityInDbAbfragevarianteSachbearbeitung.setId(uuidAbfragevarianteSachbearbeitung);

        entityInDb.setAbfragevariantenBauleitplanverfahren(List.of(entityInDbAbfragevariante));
        entityInDb.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(entityInDbAbfragevarianteSachbearbeitung)
        );

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

        entityToSave.setAbfragevariantenBauleitplanverfahren(List.of(abfragevarianteToSaveSave));
        entityToSave.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(abfragevarianteSachbearbeitungToSave)
        );

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

        entitySaved.setAbfragevariantenBauleitplanverfahren(List.of(abfragevarianteSaved));
        entitySaved.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(abfragevarianteSachbearbeitungSaved)
        );

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        try {
            this.abfrageService.patchInBearbeitungFachreferat(requestModel, uuid);
        } catch (final EntityNotFoundException exception) {
            assertThat(exception.getMessage(), is("Die Art der Abfrage wird nicht unterstützt."));
        }
    }

    @Test
    void patchInBearbeitungFachreferatAbfrageNotSupportedBaugenehmigungsverfahren()
        throws UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, CalculationException {
        final var uuid = UUID.randomUUID();
        final var uuidAbfragevariante = UUID.randomUUID();
        final var uuidAbfragevarianteSachbearbeitung = UUID.randomUUID();

        final var requestModel = new BaugenehmigungsverfahrenInBearbeitungFachreferatModel();
        requestModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        requestModel.setVersion(0L);

        final var abfragevarianteRequestModel =
            new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel();
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
            new AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel();
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

        requestModel.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevarianteRequestModel));
        requestModel.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitungRequestModel)
        );

        final var entityInDb = new Baugenehmigungsverfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setName("hallo");
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var entityInDbAbfragevariante = new AbfragevarianteBaugenehmigungsverfahren();
        entityInDbAbfragevariante.setId(uuidAbfragevariante);

        final var entityInDbAbfragevarianteSachbearbeitung = new AbfragevarianteBaugenehmigungsverfahren();
        entityInDbAbfragevarianteSachbearbeitung.setId(uuidAbfragevarianteSachbearbeitung);

        entityInDb.setAbfragevariantenBaugenehmigungsverfahren(List.of(entityInDbAbfragevariante));
        entityInDb.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(entityInDbAbfragevarianteSachbearbeitung)
        );

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final var entityToSave = new Baugenehmigungsverfahren();

        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setName("hallo");
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var abfragevarianteToSaveSave = new AbfragevarianteBaugenehmigungsverfahren();
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

        final var abfragevarianteSachbearbeitungToSave = new AbfragevarianteBaugenehmigungsverfahren();
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

        entityToSave.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevarianteToSaveSave));
        entityToSave.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitungToSave)
        );

        final var entitySaved = new Baugenehmigungsverfahren();

        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        entitySaved.setName("hallo");

        final var abfragevarianteSaved = new AbfragevarianteBaugenehmigungsverfahren();
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

        final var abfragevarianteSachbearbeitungSaved = new AbfragevarianteBaugenehmigungsverfahren();
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

        entitySaved.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevarianteSaved));
        entitySaved.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(abfragevarianteSachbearbeitungSaved)
        );

        Mockito.when(this.abfrageRepository.saveAndFlush(entityToSave)).thenReturn(entitySaved);
        Mockito.when(this.abfrageRepository.findByNameIgnoreCase("hallo")).thenReturn(Optional.empty());

        try {
            this.abfrageService.patchInBearbeitungFachreferat(requestModel, uuid);
        } catch (final EntityNotFoundException exception) {
            assertThat(exception.getMessage(), is("Die Art der Abfrage wird nicht unterstützt."));
        }
    }

    @Test
    void patchInBearbeitungFachreferatAbfrageNotSupportedWeiteresVerfahren()
        throws UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, CalculationException {
        final var uuid = UUID.randomUUID();
        final var uuidAbfragevariante = UUID.randomUUID();
        final var uuidAbfragevarianteSachbearbeitung = UUID.randomUUID();

        final var requestModel = new WeiteresVerfahrenInBearbeitungFachreferatModel();
        requestModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        requestModel.setVersion(0L);

        final var abfragevarianteRequestModel = new AbfragevarianteWeiteresVerfahrenInBearbeitungFachreferatModel();
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
            new AbfragevarianteWeiteresVerfahrenInBearbeitungFachreferatModel();
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

        requestModel.setAbfragevariantenWeiteresVerfahren(List.of(abfragevarianteRequestModel));
        requestModel.setAbfragevariantenSachbearbeitungWeiteresVerfahren(
            List.of(abfragevarianteSachbearbeitungRequestModel)
        );

        final var entityInDb = new WeiteresVerfahren();
        entityInDb.setId(uuid);
        entityInDb.setVersion(0L);
        entityInDb.setName("hallo");
        entityInDb.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var entityInDbAbfragevariante = new AbfragevarianteWeiteresVerfahren();
        entityInDbAbfragevariante.setId(uuidAbfragevariante);

        final var entityInDbAbfragevarianteSachbearbeitung = new AbfragevarianteWeiteresVerfahren();
        entityInDbAbfragevarianteSachbearbeitung.setId(uuidAbfragevarianteSachbearbeitung);

        entityInDb.setAbfragevariantenWeiteresVerfahren(List.of(entityInDbAbfragevariante));
        entityInDb.setAbfragevariantenSachbearbeitungWeiteresVerfahren(
            List.of(entityInDbAbfragevarianteSachbearbeitung)
        );

        Mockito.when(this.abfrageRepository.findById(entityInDb.getId())).thenReturn(Optional.of(entityInDb));

        final var entityToSave = new WeiteresVerfahren();

        entityToSave.setId(uuid);
        entityToSave.setVersion(0L);
        entityToSave.setName("hallo");
        entityToSave.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);

        final var abfragevarianteToSaveSave = new AbfragevarianteWeiteresVerfahren();
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

        final var abfragevarianteSachbearbeitungToSave = new AbfragevarianteWeiteresVerfahren();
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

        entityToSave.setAbfragevariantenWeiteresVerfahren(List.of(abfragevarianteToSaveSave));
        entityToSave.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevarianteSachbearbeitungToSave));

        final var entitySaved = new WeiteresVerfahren();

        entitySaved.setId(uuid);
        entitySaved.setVersion(1L);
        entitySaved.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_FACHREFERATE);
        entitySaved.setName("hallo");

        final var abfragevarianteSaved = new AbfragevarianteWeiteresVerfahren();
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

        final var abfragevarianteSachbearbeitungSaved = new AbfragevarianteWeiteresVerfahren();
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

        entitySaved.setAbfragevariantenWeiteresVerfahren(List.of(abfragevarianteSaved));
        entitySaved.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevarianteSachbearbeitungSaved));

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

        final var bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(bauvorhaben.getId());

        Mockito.when(this.abfrageRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito
            .when(this.bauvorhabenRepository.findById(bauvorhaben.getId()))
            .thenReturn(Optional.of(bauvorhabenEntity));
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        Mockito.when(this.authenticationUtils.getUserSub()).thenReturn(sub);

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.abfrageService.deleteById(id));

        Mockito.verify(this.abfrageRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(bauvorhabenEntity.getId());
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
    void throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenNotTheCorrectUserWithTheCorrectRole()
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
        this.abfrageService.throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenNotTheCorrectUserWithTheCorrectRole(
                model
            );

        roles = new String[] { "fachreferat" };
        model.setId(id);
        model.setSub(sub);
        model.setStatusAbfrage(StatusAbfrage.OFFEN);
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        try {
            this.abfrageService.throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenNotTheCorrectUserWithTheCorrectRole(
                    model
                );
        } catch (final UserRoleNotAllowedException exception) {
            assertThat(exception.getMessage(), is("Keine Berechtigung zum Löschen der Abfrage."));
        }

        roles = new String[] { "abfrageerstellung" };
        model.setId(id);
        model.setSub("321");
        model.setStatusAbfrage(StatusAbfrage.OFFEN);
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        try {
            this.abfrageService.throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenNotTheCorrectUserWithTheCorrectRole(
                    model
                );
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
            this.abfrageService.throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenNotTheCorrectUserWithTheCorrectRole(
                    model
                );
        } catch (final AbfrageStatusNotAllowedException exception) {
            assertThat(exception.getMessage(), is("Die Abfrage kann nur im Status 'angelegt' gelöscht werden."));
        }

        roles = new String[] { "abfrageerstellung" };
        model.setId(id);
        model.setSub(sub);
        model.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        Mockito.when(this.authenticationUtils.getUserRoles()).thenReturn(List.of(roles));
        this.abfrageService.throwUserRoleNotAllowedOrAbfrageStatusNotAllowedExceptionWhenNotTheCorrectUserWithTheCorrectRole(
                model
            );
    }

    @Test
    void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben()
        throws EntityIsReferencedException, EntityNotFoundException {
        this.abfrageService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(
                new BauleitplanverfahrenModel()
            );

        final BauleitplanverfahrenModel abfrage = new BauleitplanverfahrenModel();
        abfrage.setBauvorhaben(UUID.randomUUID());

        final var bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(abfrage.getBauvorhaben());

        Mockito
            .when(this.bauvorhabenRepository.findById(bauvorhabenEntity.getId()))
            .thenReturn(Optional.of(bauvorhabenEntity));
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
