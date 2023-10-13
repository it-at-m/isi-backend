package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapperImpl;
import de.muenchen.isi.domain.mapper.AbfragevarianteBauleitplanverfahrenDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapperImpl;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.model.abfrageAngelegt.BauleitplanverfahrenAngelegtModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.security.AuthenticationUtils;
import java.lang.reflect.Field;
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
}
