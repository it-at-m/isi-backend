package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.BauvorhabenDomainMapper;
import de.muenchen.isi.domain.mapper.BauvorhabenDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.HausFuerKinder;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.GsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Grundschule;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Mittelschule;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.GrundschuleRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.GsNachmittagBetreuungRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.HausFuerKinderRepository;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.KindergartenRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.KinderkrippeRepository;
import de.muenchen.isi.infrastructure.repository.infrastruktureinrichtung.MittelschuleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BauvorhabenServiceTest {

    private final BauvorhabenDomainMapper bauvorhabenDomainMapper = new BauvorhabenDomainMapperImpl();

    private BauvorhabenService bauvorhabenService;
    @Mock
    private BauvorhabenRepository bauvorhabenRepository;

    @Mock
    private InfrastrukturabfrageRepository infrastrukturabfrageRepository;

    @Mock
    private KinderkrippeRepository kinderkrippeRepository;

    @Mock
    private KindergartenRepository kindergartenRepository;

    @Mock
    private HausFuerKinderRepository hausFuerKinderRepository;

    @Mock
    private GsNachmittagBetreuungRepository gsNachmittagBetreuungRepository;

    @Mock
    private GrundschuleRepository grundschuleRepository;

    @Mock
    private MittelschuleRepository mittelschuleRepository;

    @BeforeEach
    public void beforeEach() {

        this.bauvorhabenService = new BauvorhabenService(
                this.bauvorhabenDomainMapper,
                this.bauvorhabenRepository,
                this.infrastrukturabfrageRepository,
                this.kinderkrippeRepository,
                this.kindergartenRepository,
                this.hausFuerKinderRepository,
                this.gsNachmittagBetreuungRepository,
                this.grundschuleRepository,
                this.mittelschuleRepository
        );

        Mockito.reset(
                this.bauvorhabenRepository,
                this.infrastrukturabfrageRepository,
                this.kinderkrippeRepository,
                this.kindergartenRepository,
                this.hausFuerKinderRepository,
                this.gsNachmittagBetreuungRepository,
                this.grundschuleRepository,
                this.mittelschuleRepository
        );
    }

    @Test
    void getBauvorhabenTest() {
        final Bauvorhaben entity1 = new Bauvorhaben();
        entity1.setId(UUID.randomUUID());
        final Bauvorhaben entity2 = new Bauvorhaben();
        entity2.setId(UUID.randomUUID());

        Mockito.when(this.bauvorhabenRepository.findAllByOrderByGrundstuecksgroesseDesc()).thenReturn(Stream.of(entity1, entity2));

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
        Mockito.reset(
                this.bauvorhabenRepository
        );

        Mockito.when(this.bauvorhabenRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> this.bauvorhabenService.getBauvorhabenById(id));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(id);

    }

    @Test
    void saveBauvorhabenTest() throws UniqueViolationException {
        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(null);

        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(bauvorhaben.getId());

        final Bauvorhaben saveResult = new Bauvorhaben();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.bauvorhabenRepository.save(bauvorhabenEntity)).thenReturn(saveResult);

        final BauvorhabenModel result = this.bauvorhabenService.saveBauvorhaben(bauvorhaben);

        final BauvorhabenModel expected = new BauvorhabenModel();
        expected.setId(saveResult.getId());

        assertThat(result, is(expected));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).save(bauvorhabenEntity);
    }

    @Test
    void saveBauvorhabenUniqueViolationTest() throws UniqueViolationException {
        final String nameVorhaben = "Test Bauvorhaben";
        final BauvorhabenModel bauvorhabenModel = new BauvorhabenModel();
        bauvorhabenModel.setId(UUID.randomUUID());
        bauvorhabenModel.setNameVorhaben(nameVorhaben);

        final Bauvorhaben entity = new Bauvorhaben();
        entity.setId(bauvorhabenModel.getId());
        entity.setNameVorhaben(bauvorhabenModel.getNameVorhaben());

        Mockito.when(this.bauvorhabenRepository.findByNameVorhabenIgnoreCase(entity.getNameVorhaben())).thenReturn(Optional.of(entity));
        Mockito.when(this.bauvorhabenRepository.save(entity)).thenReturn(entity);

        Assertions.assertThrows(UniqueViolationException.class, () -> this.bauvorhabenService.saveBauvorhaben(bauvorhabenModel));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findByNameVorhabenIgnoreCase(entity.getNameVorhaben());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).save(entity);
    }

    @Test
    void updateBauvorhabenTest() throws EntityNotFoundException, UniqueViolationException {
        final BauvorhabenModel bauvorhabenModel = new BauvorhabenModel();
        bauvorhabenModel.setId(UUID.randomUUID());

        final Bauvorhaben entity = new Bauvorhaben();
        entity.setId(bauvorhabenModel.getId());

        Mockito.when(this.bauvorhabenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.bauvorhabenRepository.save(entity)).thenReturn(entity);

        final BauvorhabenModel result = this.bauvorhabenService.updateBauvorhaben(bauvorhabenModel);

        final BauvorhabenModel expected = new BauvorhabenModel();
        expected.setId(bauvorhabenModel.getId());

        assertThat(
                result,
                is(bauvorhabenModel)
        );

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).save(entity);
    }

    @Test
    void deleteBauvorhaben() throws EntityNotFoundException, EntityIsReferencedException {
        final UUID id = UUID.randomUUID();

        final Bauvorhaben entity = new Bauvorhaben();
        entity.setId(id);

        Mockito.when(this.bauvorhabenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(entity.getId())).thenReturn(Stream.empty());

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
        Mockito.when(this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(entity.getId())).thenReturn(Stream.of(infrastrukturabfrage));

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
        final Infrastruktureinrichtung infrastruktureinrichtungKinderkrippe = new Infrastruktureinrichtung();
        infrastruktureinrichtungKinderkrippe.setNameEinrichtung("Kinderkrippe");
        kinderkrippe.setInfrastruktureinrichtung(infrastruktureinrichtungKinderkrippe);

        final Kindergarten kindergarten = new Kindergarten();
        final Infrastruktureinrichtung infrastruktureinrichtungKindergarten = new Infrastruktureinrichtung();
        infrastruktureinrichtungKindergarten.setNameEinrichtung("Kindergarten");
        kindergarten.setInfrastruktureinrichtung(infrastruktureinrichtungKindergarten);

        final HausFuerKinder hausFuerKinder = new HausFuerKinder();
        final Infrastruktureinrichtung infrastruktureinrichtungHausFuerKinder = new Infrastruktureinrichtung();
        infrastruktureinrichtungHausFuerKinder.setNameEinrichtung("Haus für Kinder");
        hausFuerKinder.setInfrastruktureinrichtung(infrastruktureinrichtungHausFuerKinder);

        final GsNachmittagBetreuung gsNachmittagBetreuung = new GsNachmittagBetreuung();
        final Infrastruktureinrichtung infrastruktureinrichtungGsNachmittagBetreuung = new Infrastruktureinrichtung();
        infrastruktureinrichtungGsNachmittagBetreuung.setNameEinrichtung("Nachmittagsbetreuung für Grundschulkinder");
        gsNachmittagBetreuung.setInfrastruktureinrichtung(infrastruktureinrichtungGsNachmittagBetreuung);

        final Grundschule grundschule = new Grundschule();
        final Infrastruktureinrichtung infrastruktureinrichtungGrundschule = new Infrastruktureinrichtung();
        infrastruktureinrichtungGrundschule.setNameEinrichtung("Grundschule");
        grundschule.setInfrastruktureinrichtung(infrastruktureinrichtungGrundschule);

        final Mittelschule mittelschule = new Mittelschule();
        final Infrastruktureinrichtung infrastruktureinrichtungMittelschule = new Infrastruktureinrichtung();
        infrastruktureinrichtungMittelschule.setNameEinrichtung("Mittelschule");
        mittelschule.setInfrastruktureinrichtung(infrastruktureinrichtungMittelschule);

        Mockito.when(this.bauvorhabenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.kinderkrippeRepository.findAllByInfrastruktureinrichtungBauvorhabenId(entity.getId())).thenReturn(Stream.of(kinderkrippe));
        Mockito.when(this.kindergartenRepository.findAllByInfrastruktureinrichtungBauvorhabenId(entity.getId())).thenReturn(Stream.of(kindergarten));
        Mockito.when(this.hausFuerKinderRepository.findAllByInfrastruktureinrichtungBauvorhabenId(entity.getId())).thenReturn(Stream.of(hausFuerKinder));
        Mockito.when(this.gsNachmittagBetreuungRepository.findAllByInfrastruktureinrichtungBauvorhabenId(entity.getId())).thenReturn(Stream.of(gsNachmittagBetreuung));
        Mockito.when(this.grundschuleRepository.findAllByInfrastruktureinrichtungBauvorhabenId(entity.getId())).thenReturn(Stream.of(grundschule));
        Mockito.when(this.mittelschuleRepository.findAllByInfrastruktureinrichtungBauvorhabenId(entity.getId())).thenReturn(Stream.of(mittelschule));

        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.bauvorhabenService.deleteBauvorhaben(id));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void assignBauvorhabenToAbfrageTest() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        final var bauvorhaben = new Bauvorhaben();
        bauvorhaben.setId(id);

        final var abfrage = new AbfrageModel();
        AbfrageModel returnedAbfrage;

        // Wenn 'bauvorhabenId' null ist, soll nichts passieren.

        returnedAbfrage = this.bauvorhabenService.assignBauvorhabenToAbfrage(null, abfrage);
        assertThat(returnedAbfrage, sameInstance(abfrage));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(bauvorhaben.getId());

        // Wenn kein Bauvorhaben mit der ID 'bauvorhabenId' existiert, soll eine 'BauvorhabenNotFoundException' geworfen werden.

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.bauvorhabenService.assignBauvorhabenToAbfrage(id, abfrage));
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

        final var infrastruktureinrichtung = new InfrastruktureinrichtungModel();
        InfrastruktureinrichtungModel returnedInfrastruktureinrichtung;

        // Wenn 'bauvorhabenId' null ist, soll nichts passieren.

        returnedInfrastruktureinrichtung = this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(null, infrastruktureinrichtung);
        assertThat(returnedInfrastruktureinrichtung, sameInstance(infrastruktureinrichtung));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).findById(bauvorhaben.getId());

        // Wenn kein Bauvorhaben mit der ID 'bauvorhabenId' existiert, soll eine 'BauvorhabenNotFoundException' geworfen werden.

        Assertions.assertThrows(EntityNotFoundException.class, () -> this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(id, infrastruktureinrichtung));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(bauvorhaben.getId());

        // Normalfall

        Mockito.when(this.bauvorhabenRepository.findById(bauvorhaben.getId())).thenReturn(Optional.of(bauvorhaben));
        returnedInfrastruktureinrichtung = this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(id, infrastruktureinrichtung);
        assertThat(returnedInfrastruktureinrichtung, sameInstance(infrastruktureinrichtung));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(2)).findById(bauvorhaben.getId());
        assertThat(returnedInfrastruktureinrichtung.getBauvorhaben(), is(this.bauvorhabenService.getBauvorhabenById(id)));
    }

    @Test
    void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben() throws EntityIsReferencedException {
        final Infrastrukturabfrage infrastrukturabfrage = new Infrastrukturabfrage();
        final Abfrage abfrage1 = new Abfrage();
        abfrage1.setNameAbfrage("test1");
        infrastrukturabfrage.setAbfrage(abfrage1);

        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(UUID.randomUUID());

        Mockito.when(this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(bauvorhaben.getId())).thenReturn(Stream.of());
        this.bauvorhabenService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(bauvorhaben);
        Mockito.reset(this.infrastrukturabfrageRepository);

        Mockito.when(this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(bauvorhaben.getId())).thenReturn(Stream.of(infrastrukturabfrage));
        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.bauvorhabenService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(bauvorhaben));
        Mockito.reset(this.infrastrukturabfrageRepository);

    }

    @Test
    void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben() throws EntityIsReferencedException {
        final Kinderkrippe kinderkrippe = new Kinderkrippe();
        final Infrastruktureinrichtung infrastruktureinrichtungKinderkrippe = new Infrastruktureinrichtung();
        infrastruktureinrichtungKinderkrippe.setNameEinrichtung("Kinderkrippe");
        kinderkrippe.setInfrastruktureinrichtung(infrastruktureinrichtungKinderkrippe);

        final Kindergarten kindergarten = new Kindergarten();
        final Infrastruktureinrichtung infrastruktureinrichtungKindergarten = new Infrastruktureinrichtung();
        infrastruktureinrichtungKindergarten.setNameEinrichtung("Kindergarten");
        kindergarten.setInfrastruktureinrichtung(infrastruktureinrichtungKindergarten);

        final HausFuerKinder hausFuerKinder = new HausFuerKinder();
        final Infrastruktureinrichtung infrastruktureinrichtungHausFuerKinder = new Infrastruktureinrichtung();
        infrastruktureinrichtungHausFuerKinder.setNameEinrichtung("Haus für Kinder");
        hausFuerKinder.setInfrastruktureinrichtung(infrastruktureinrichtungHausFuerKinder);

        final GsNachmittagBetreuung gsNachmittagBetreuung = new GsNachmittagBetreuung();
        final Infrastruktureinrichtung infrastruktureinrichtungGsNachmittagBetreuung = new Infrastruktureinrichtung();
        infrastruktureinrichtungGsNachmittagBetreuung.setNameEinrichtung("Nachmittagsbetreuung für Grundschulkinder");
        gsNachmittagBetreuung.setInfrastruktureinrichtung(infrastruktureinrichtungGsNachmittagBetreuung);

        final Grundschule grundschule = new Grundschule();
        final Infrastruktureinrichtung infrastruktureinrichtungGrundschule = new Infrastruktureinrichtung();
        infrastruktureinrichtungGrundschule.setNameEinrichtung("Grundschule");
        grundschule.setInfrastruktureinrichtung(infrastruktureinrichtungGrundschule);

        final Mittelschule mittelschule = new Mittelschule();
        final Infrastruktureinrichtung infrastruktureinrichtungMittelschule = new Infrastruktureinrichtung();
        infrastruktureinrichtungMittelschule.setNameEinrichtung("Mittelschule");
        mittelschule.setInfrastruktureinrichtung(infrastruktureinrichtungMittelschule);

        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(UUID.randomUUID());

        Mockito.when(this.kinderkrippeRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())).thenReturn(Stream.of(kinderkrippe));
        Mockito.when(this.kindergartenRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())).thenReturn(Stream.of(kindergarten));
        Mockito.when(this.hausFuerKinderRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())).thenReturn(Stream.of(hausFuerKinder));
        Mockito.when(this.gsNachmittagBetreuungRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())).thenReturn(Stream.of(gsNachmittagBetreuung));
        Mockito.when(this.grundschuleRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())).thenReturn(Stream.of(grundschule));
        Mockito.when(this.mittelschuleRepository.findAllByInfrastruktureinrichtungBauvorhabenId(bauvorhaben.getId())).thenReturn(Stream.of(mittelschule));
        Assertions.assertThrows(EntityIsReferencedException.class, () -> this.bauvorhabenService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(bauvorhaben));
        Mockito.reset(
                this.kinderkrippeRepository,
                this.kindergartenRepository,
                this.hausFuerKinderRepository,
                this.gsNachmittagBetreuungRepository,
                this.grundschuleRepository,
                this.mittelschuleRepository
        );
    }

}

