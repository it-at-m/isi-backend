package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.BauvorhabenDomainMapper;
import de.muenchen.isi.domain.mapper.BauvorhabenDomainMapperImpl;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.repository.AbfragevarianteRepository;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BauvorhabenServiceTest {

    private final BauvorhabenDomainMapper bauvorhabenDomainMapper = new BauvorhabenDomainMapperImpl(
        new DokumentDomainMapperImpl()
    );

    private BauvorhabenService bauvorhabenService;

    @Mock
    private BauvorhabenRepository bauvorhabenRepository;

    @Mock
    private InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    @Mock
    private InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    @Mock
    private AbfragevarianteRepository abfragevarianteRepository;

    @Mock
    private AbfrageService abfrageService;

    @Mock
    private DokumentService dokumentService;

    @BeforeEach
    public void beforeEach() {
        this.bauvorhabenService =
            new BauvorhabenService(
                this.bauvorhabenDomainMapper,
                this.bauvorhabenRepository,
                this.infrastrukturabfrageRepository,
                this.infrastruktureinrichtungRepository,
                this.abfragevarianteRepository,
                this.abfrageService,
                this.dokumentService
            );

        Mockito.reset(
            this.bauvorhabenRepository,
            this.infrastrukturabfrageRepository,
            this.infrastruktureinrichtungRepository,
            this.dokumentService
        );
    }

    @Test
    void getBauvorhabenTest() {
        final Bauvorhaben entity1 = new Bauvorhaben();
        entity1.setId(UUID.randomUUID());
        final Bauvorhaben entity2 = new Bauvorhaben();
        entity2.setId(UUID.randomUUID());

        Mockito
            .when(this.bauvorhabenRepository.findAllByOrderByGrundstuecksgroesseDesc())
            .thenReturn(Stream.of(entity1, entity2));

        final List<BauvorhabenModel> result = this.bauvorhabenService.getBauvorhaben();

        final BauvorhabenModel model1 = new BauvorhabenModel();
        model1.setId(entity1.getId());
        final BauvorhabenModel model2 = new BauvorhabenModel();
        model2.setId(entity2.getId());

        assertThat(result, is(List.of(model1, model2)));
    }

    @Test
    void getBauvorhabenByIdTest() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.bauvorhabenRepository.findById(id)).thenReturn(Optional.of(new Bauvorhaben()));
        final BauvorhabenModel result = this.bauvorhabenService.getBauvorhabenById(id);
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.bauvorhabenRepository);

        Mockito.when(this.bauvorhabenRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.bauvorhabenService.getBauvorhabenById(id));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveBauvorhabenTest() throws UniqueViolationException, OptimisticLockingException {
        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(null);

        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(bauvorhaben.getId());

        final Bauvorhaben saveResult = new Bauvorhaben();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.bauvorhabenRepository.saveAndFlush(bauvorhabenEntity)).thenReturn(saveResult);

        final BauvorhabenModel result = this.bauvorhabenService.saveBauvorhaben(bauvorhaben);

        final BauvorhabenModel expected = new BauvorhabenModel();
        expected.setId(saveResult.getId());

        assertThat(result, is(expected));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).saveAndFlush(bauvorhabenEntity);
    }

    @Test
    void saveBauvorhabenUniqueViolationTest() throws UniqueViolationException {
        final String nameVorhaben = "Test Bauvorhaben";
        final BauvorhabenModel bauvorhabenModel = new BauvorhabenModel();
        bauvorhabenModel.setId(UUID.randomUUID());
        bauvorhabenModel.setNameVorhaben(nameVorhaben);

        final BauvorhabenModel bauvorhabenModel2 = new BauvorhabenModel();
        bauvorhabenModel2.setId(UUID.randomUUID());
        bauvorhabenModel2.setNameVorhaben(nameVorhaben);

        final Bauvorhaben entity = new Bauvorhaben();
        entity.setId(bauvorhabenModel.getId());
        entity.setNameVorhaben(bauvorhabenModel.getNameVorhaben());

        Mockito
            .when(this.bauvorhabenRepository.findByNameVorhabenIgnoreCase(entity.getNameVorhaben()))
            .thenReturn(Optional.of(entity));
        Mockito.when(this.bauvorhabenRepository.saveAndFlush(entity)).thenReturn(entity);

        Assertions.assertThrows(
            UniqueViolationException.class,
            () -> this.bauvorhabenService.saveBauvorhaben(bauvorhabenModel2)
        );

        Mockito
            .verify(this.bauvorhabenRepository, Mockito.times(1))
            .findByNameVorhabenIgnoreCase(entity.getNameVorhaben());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).save(entity);
    }

    @Test
    void updateBauvorhabenTest()
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        final BauvorhabenModel bauvorhabenModel = new BauvorhabenModel();
        bauvorhabenModel.setId(UUID.randomUUID());
        bauvorhabenModel.setNameVorhaben("BauvorhabenTest");

        final Bauvorhaben entity = this.bauvorhabenDomainMapper.model2Entity(bauvorhabenModel);

        Mockito.when(this.bauvorhabenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.bauvorhabenRepository.saveAndFlush(entity)).thenReturn(entity);
        Mockito
            .when(this.bauvorhabenRepository.findByNameVorhabenIgnoreCase("BauvorhabenTest"))
            .thenReturn(Optional.empty());

        final BauvorhabenModel result = this.bauvorhabenService.updateBauvorhaben(bauvorhabenModel);

        final BauvorhabenModel expected = new BauvorhabenModel();
        expected.setId(bauvorhabenModel.getId());

        assertThat(result, is(bauvorhabenModel));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).saveAndFlush(entity);
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findByNameVorhabenIgnoreCase("BauvorhabenTest");
        Mockito
            .verify(this.dokumentService, Mockito.times(1))
            .deleteDokumenteFromOriginalDokumentenListWhichAreMissingInParameterAdaptedDokumentenListe(
                Mockito.isNull(),
                Mockito.isNull()
            );
    }

    @Test
    void deleteBauvorhaben() throws EntityNotFoundException, EntityIsReferencedException {
        final UUID id = UUID.randomUUID();

        final Bauvorhaben entity = new Bauvorhaben();
        entity.setId(id);

        Mockito.when(this.bauvorhabenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito
            .when(this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(entity.getId()))
            .thenReturn(Stream.empty());

        this.bauvorhabenService.deleteBauvorhaben(id);

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteBauvorhabenOfAbfrageException() {
        final UUID id = UUID.randomUUID();

        final Bauvorhaben entity = new Bauvorhaben();
        entity.setId(id);

        final Infrastrukturabfrage infrastrukturabfrage = new Infrastrukturabfrage();
        final Abfrage abfrage1 = new Abfrage();
        abfrage1.setNameAbfrage("test1");
        infrastrukturabfrage.setAbfrage(abfrage1);

        Mockito.when(this.bauvorhabenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito
            .when(this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(entity.getId()))
            .thenReturn(Stream.of(infrastrukturabfrage));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.bauvorhabenService.deleteBauvorhaben(id));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void deleteBauvorhabenOfInfrastruktureinrichtungException() {
        final UUID id = UUID.randomUUID();

        final Bauvorhaben entity = new Bauvorhaben();
        entity.setId(id);

        final Kinderkrippe kinderkrippe = new Kinderkrippe();
        kinderkrippe.setNameEinrichtung("Kinderkrippe");

        Mockito.when(this.bauvorhabenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito
            .when(this.infrastruktureinrichtungRepository.findAllByBauvorhabenId(entity.getId()))
            .thenReturn(Stream.of(kinderkrippe));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.bauvorhabenService.deleteBauvorhaben(id));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void assignBauvorhabenToAbfrageTest() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(id);

        final var abfrage = new AbfrageAngelegtModel();
        AbfrageAngelegtModel returnedAbfrage;

        // Wenn 'bauvorhabenId' null ist, soll nichts passieren.

        returnedAbfrage = this.bauvorhabenService.assignBauvorhabenToAbfrage(null, abfrage);
        assertThat(returnedAbfrage, sameInstance(abfrage));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(bauvorhaben.getId());

        // Wenn kein Bauvorhaben mit der ID 'bauvorhabenId' existiert, soll eine 'BauvorhabenNotFoundException' geworfen werden.

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.bauvorhabenService.assignBauvorhabenToAbfrage(id, abfrage)
        );
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(bauvorhaben.getId());

        // Normalfall

        Mockito.when(this.bauvorhabenRepository.findById(bauvorhaben.getId())).thenReturn(Optional.of(bauvorhaben));
        returnedAbfrage = this.bauvorhabenService.assignBauvorhabenToAbfrage(id, abfrage);
        assertThat(returnedAbfrage, sameInstance(abfrage));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(2)).findById(bauvorhaben.getId());
        assertThat(returnedAbfrage.getBauvorhaben(), is(this.bauvorhabenService.getBauvorhabenById(id)));
    }

    @Test
    void assignBauvorhabenToInfrastruktureinrichtungTest() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(id);

        final var infrastruktureinrichtung = new KinderkrippeModel();
        InfrastruktureinrichtungModel returnedInfrastruktureinrichtung;

        // Wenn 'bauvorhabenId' null ist, soll nichts passieren.

        returnedInfrastruktureinrichtung =
            this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(null, infrastruktureinrichtung);
        assertThat(returnedInfrastruktureinrichtung, sameInstance(infrastruktureinrichtung));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(bauvorhaben.getId());

        // Wenn kein Bauvorhaben mit der ID 'bauvorhabenId' existiert, soll eine 'BauvorhabenNotFoundException' geworfen werden.

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(id, infrastruktureinrichtung)
        );
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(bauvorhaben.getId());

        // Normalfall

        Mockito.when(this.bauvorhabenRepository.findById(bauvorhaben.getId())).thenReturn(Optional.of(bauvorhaben));
        returnedInfrastruktureinrichtung =
            this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(id, infrastruktureinrichtung);
        assertThat(returnedInfrastruktureinrichtung, sameInstance(infrastruktureinrichtung));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(2)).findById(bauvorhaben.getId());
        assertThat(
            returnedInfrastruktureinrichtung.getBauvorhaben(),
            is(this.bauvorhabenService.getBauvorhabenById(id))
        );
    }

    @Test
    void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben() throws EntityIsReferencedException {
        final Infrastrukturabfrage infrastrukturabfrage = new Infrastrukturabfrage();
        final Abfrage abfrage1 = new Abfrage();
        abfrage1.setNameAbfrage("test1");
        infrastrukturabfrage.setAbfrage(abfrage1);

        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(UUID.randomUUID());

        Mockito
            .when(this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(bauvorhaben.getId()))
            .thenReturn(Stream.of());
        this.bauvorhabenService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(bauvorhaben);
        Mockito.reset(this.infrastrukturabfrageRepository);

        Mockito
            .when(this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(bauvorhaben.getId()))
            .thenReturn(Stream.of(infrastrukturabfrage));
        Assertions.assertThrows(
            EntityIsReferencedException.class,
            () ->
                this.bauvorhabenService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(bauvorhaben)
        );
        Mockito.reset(this.infrastrukturabfrageRepository);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben()
        throws EntityIsReferencedException {
        final Kinderkrippe kinderkrippe = new Kinderkrippe();
        kinderkrippe.setNameEinrichtung("Kinderkrippe");

        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(UUID.randomUUID());

        Mockito
            .when(this.infrastruktureinrichtungRepository.findAllByBauvorhabenId(bauvorhaben.getId()))
            .thenReturn(Stream.of(kinderkrippe));
        Assertions.assertThrows(
            EntityIsReferencedException.class,
            () ->
                this.bauvorhabenService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(
                        bauvorhaben
                    )
        );
    }
}
