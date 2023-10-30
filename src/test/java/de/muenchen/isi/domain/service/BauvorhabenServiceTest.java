package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.BauvorhabenNotReferencedException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.BauvorhabenDomainMapper;
import de.muenchen.isi.domain.mapper.BauvorhabenDomainMapperImpl;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.mapper.SearchDomainMapper;
import de.muenchen.isi.domain.mapper.SearchDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.enums.SearchResultType;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.Abfrage;
import de.muenchen.isi.infrastructure.entity.AbfragevarianteBauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.GlobalCounter;
import de.muenchen.isi.infrastructure.entity.common.Stadtbezirk;
import de.muenchen.isi.infrastructure.entity.common.Verortung;
import de.muenchen.isi.infrastructure.entity.enums.CounterType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kindergarten;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Kinderkrippe;
import de.muenchen.isi.infrastructure.repository.AbfrageRepository;
import de.muenchen.isi.infrastructure.repository.AbfragevarianteRepository;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import de.muenchen.isi.infrastructure.repository.common.GlobalCounterRepository;
import de.muenchen.isi.infrastructure.repository.common.KommentarRepository;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
public class BauvorhabenServiceTest {

    private final BauvorhabenDomainMapper bauvorhabenDomainMapper = new BauvorhabenDomainMapperImpl(
        new DokumentDomainMapperImpl()
    );

    private final SearchDomainMapper searchDomainMapper = new SearchDomainMapperImpl();

    private BauvorhabenService bauvorhabenService;

    @Mock
    private AbfrageService abfrageService;

    @Mock
    private BauvorhabenRepository bauvorhabenRepository;

    @Mock
    private AbfrageRepository abfrageRepository;

    @Mock
    private InfrastruktureinrichtungRepository infrastruktureinrichtungRepository;

    @Mock
    private AbfragevarianteRepository abfragevarianteRepository;

    @Mock
    private GlobalCounterRepository globalCounterRepository;

    @Mock
    private DokumentService dokumentService;

    @Mock
    private KommentarRepository kommentarRepository;

    @BeforeEach
    public void beforeEach() throws IllegalAccessException, NoSuchFieldException {
        Field field = bauvorhabenDomainMapper.getClass().getSuperclass().getDeclaredField("abfragevarianteRepository");
        field.setAccessible(true);
        field.set(bauvorhabenDomainMapper, abfragevarianteRepository);
        this.bauvorhabenService =
            new BauvorhabenService(
                this.bauvorhabenDomainMapper,
                this.searchDomainMapper,
                this.bauvorhabenRepository,
                this.abfrageRepository,
                this.abfragevarianteRepository,
                this.infrastruktureinrichtungRepository,
                this.globalCounterRepository,
                this.abfrageService,
                this.dokumentService,
                this.kommentarRepository
            );

        Mockito.reset(
            this.bauvorhabenRepository,
            this.abfrageRepository,
            this.infrastruktureinrichtungRepository,
            this.abfragevarianteRepository,
            this.globalCounterRepository,
            this.dokumentService,
            this.abfrageService,
            this.kommentarRepository
        );
    }

    @Test
    void getReferencedAbfragenElements() {
        var bauvorhabenId = UUID.randomUUID();
        Bauvorhaben bauvorhaben = new Bauvorhaben();
        bauvorhaben.setBauvorhabenNummer("12345");
        bauvorhaben.setGrundstuecksgroesse(BigDecimal.valueOf(1));
        bauvorhaben.setNameVorhaben("Name");
        bauvorhaben.setWesentlicheRechtsgrundlage(List.of(WesentlicheRechtsgrundlage.AUSSENBEREICH));
        bauvorhaben.setSobonRelevant(UncertainBoolean.FALSE);
        bauvorhaben.setStandVerfahren(StandVerfahren.INFO_FEHLT);
        bauvorhaben.setId(bauvorhabenId);

        final Bauleitplanverfahren abfrage1 = new Bauleitplanverfahren();
        abfrage1.setId(UUID.randomUUID());
        abfrage1.setName("NameAbfrage1");
        abfrage1.setStatusAbfrage(StatusAbfrage.OFFEN);
        abfrage1.setFristBearbeitung(LocalDate.of(2022, 11, 1));
        abfrage1.setBauvorhaben(bauvorhaben);

        final Bauleitplanverfahren abfrage2 = new Bauleitplanverfahren();
        abfrage2.setId(UUID.randomUUID());
        abfrage2.setName("NameAbfrage2");
        abfrage2.setStatusAbfrage(StatusAbfrage.ANGELEGT);
        abfrage2.setFristBearbeitung(LocalDate.of(2022, 9, 1));
        abfrage2.setBauvorhaben(bauvorhaben);

        final Bauleitplanverfahren abfrage3 = new Bauleitplanverfahren();
        abfrage3.setId(UUID.randomUUID());
        abfrage3.setName("NameAbfrage3");
        abfrage3.setStatusAbfrage(StatusAbfrage.OFFEN);
        abfrage3.setFristBearbeitung(LocalDate.of(2022, 12, 1));
        abfrage3.setBauvorhaben(bauvorhaben);

        final Stream<Abfrage> listAbfrage = Stream.of(abfrage1, abfrage2, abfrage3);

        final List<AbfrageSearchResultModel> expectedAbfrageList = new ArrayList<>();

        var abfrageListElementModel1 = new AbfrageSearchResultModel();
        abfrageListElementModel1.setType(SearchResultType.ABFRAGE);
        abfrageListElementModel1.setId(abfrage1.getId());
        abfrageListElementModel1.setName(abfrage1.getName());
        abfrageListElementModel1.setStatusAbfrage(abfrage1.getStatusAbfrage());
        abfrageListElementModel1.setFristBearbeitung(abfrage1.getFristBearbeitung());
        abfrageListElementModel1.setType(SearchResultType.ABFRAGE);
        abfrageListElementModel1.setBauvorhaben(bauvorhabenId);
        expectedAbfrageList.add(abfrageListElementModel1);

        var abfrageListElementModel2 = new AbfrageSearchResultModel();
        abfrageListElementModel2.setType(SearchResultType.ABFRAGE);
        abfrageListElementModel2.setId(abfrage2.getId());
        abfrageListElementModel2.setName(abfrage2.getName());
        abfrageListElementModel2.setStatusAbfrage(abfrage2.getStatusAbfrage());
        abfrageListElementModel2.setFristBearbeitung(abfrage2.getFristBearbeitung());
        abfrageListElementModel2.setType(SearchResultType.ABFRAGE);
        abfrageListElementModel2.setBauvorhaben(bauvorhabenId);
        expectedAbfrageList.add(abfrageListElementModel2);

        var abfrageListElementModel3 = new AbfrageSearchResultModel();
        abfrageListElementModel3.setType(SearchResultType.ABFRAGE);
        abfrageListElementModel3.setId(abfrage3.getId());
        abfrageListElementModel3.setName(abfrage3.getName());
        abfrageListElementModel3.setStatusAbfrage(abfrage3.getStatusAbfrage());
        abfrageListElementModel3.setFristBearbeitung(abfrage3.getFristBearbeitung());
        abfrageListElementModel3.setType(SearchResultType.ABFRAGE);
        abfrageListElementModel3.setBauvorhaben(bauvorhabenId);
        expectedAbfrageList.add(abfrageListElementModel3);

        Mockito
            .when(this.abfrageRepository.findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(bauvorhabenId))
            .thenReturn(listAbfrage);

        List<AbfrageSearchResultModel> abfrageResult = this.bauvorhabenService.getReferencedAbfrage(bauvorhabenId);

        assertThat(expectedAbfrageList, is(abfrageResult));

        Mockito
            .verify(this.abfrageRepository, Mockito.times(1))
            .findAllByBauvorhabenIdOrderByCreatedDateTimeDesc(bauvorhabenId);
    }

    @Test
    void getReferencedInfrastruktureinrichtungElements() {
        var bauvorhabenId = UUID.randomUUID();
        Bauvorhaben bauvorhaben = new Bauvorhaben();
        bauvorhaben.setBauvorhabenNummer("12345");
        bauvorhaben.setGrundstuecksgroesse(BigDecimal.valueOf(1));
        bauvorhaben.setNameVorhaben("Name");
        bauvorhaben.setWesentlicheRechtsgrundlage(
            List.of(WesentlicheRechtsgrundlage.EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30)
        );
        bauvorhaben.setSobonRelevant(UncertainBoolean.FALSE);
        bauvorhaben.setStandVerfahren(StandVerfahren.RAHMENPLANUNG);
        bauvorhaben.setId(bauvorhabenId);

        final Kinderkrippe kinderkrippe1 = new Kinderkrippe();
        kinderkrippe1.setNameEinrichtung("A");
        kinderkrippe1.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        kinderkrippe1.setAnzahlKinderkrippeGruppen(10);
        kinderkrippe1.setAnzahlKinderkrippePlaetze(100);
        kinderkrippe1.setBauvorhaben(bauvorhaben);

        final Kinderkrippe kinderkrippe2 = new Kinderkrippe();
        kinderkrippe2.setNameEinrichtung("B");
        kinderkrippe2.setStatus(StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_NEUE_EINR);
        kinderkrippe2.setAnzahlKinderkrippeGruppen(11);
        kinderkrippe2.setAnzahlKinderkrippePlaetze(110);
        kinderkrippe2.setBauvorhaben(bauvorhaben);

        final Kindergarten kindergarten1 = new Kindergarten();
        kindergarten1.setNameEinrichtung("A");
        kindergarten1.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        kindergarten1.setAnzahlKindergartenGruppen(9);
        kindergarten1.setAnzahlKindergartenPlaetze(90);
        kindergarten1.setBauvorhaben(bauvorhaben);

        final Kindergarten kindergarten2 = new Kindergarten();
        kindergarten2.setNameEinrichtung("B");
        kindergarten2.setStatus(StatusInfrastruktureinrichtung.BESTAND);
        kindergarten2.setAnzahlKindergartenGruppen(9);
        kindergarten2.setAnzahlKindergartenPlaetze(90);
        kindergarten2.setBauvorhaben(bauvorhaben);

        final Stream<Infrastruktureinrichtung> listInfrastruktureinrichtung = Stream.of(
            kinderkrippe1,
            kinderkrippe2,
            kindergarten1,
            kindergarten2
        );

        final List<AbfrageSearchResultModel> expectedAbfrageList = new ArrayList<>();

        final List<InfrastruktureinrichtungSearchResultModel> expectedInfrastruktureinrichtungList = new ArrayList<>();

        var kinderkrippeListElementModel1 = new InfrastruktureinrichtungSearchResultModel();
        kinderkrippeListElementModel1.setType(SearchResultType.INFRASTRUKTUREINRICHTUNG);
        kinderkrippeListElementModel1.setId(kinderkrippe1.getId());
        kinderkrippeListElementModel1.setNameEinrichtung(kinderkrippe1.getNameEinrichtung());
        kinderkrippeListElementModel1.setInfrastruktureinrichtungTyp(kinderkrippe1.getInfrastruktureinrichtungTyp());
        expectedInfrastruktureinrichtungList.add(kinderkrippeListElementModel1);

        var kinderkrippeListElementModel2 = new InfrastruktureinrichtungSearchResultModel();
        kinderkrippeListElementModel2.setType(SearchResultType.INFRASTRUKTUREINRICHTUNG);
        kinderkrippeListElementModel2.setId(kinderkrippe2.getId());
        kinderkrippeListElementModel2.setNameEinrichtung(kinderkrippe2.getNameEinrichtung());
        kinderkrippeListElementModel2.setInfrastruktureinrichtungTyp(kinderkrippe2.getInfrastruktureinrichtungTyp());
        expectedInfrastruktureinrichtungList.add(kinderkrippeListElementModel2);

        var kindergartenListElementModel1 = new InfrastruktureinrichtungSearchResultModel();
        kindergartenListElementModel1.setType(SearchResultType.INFRASTRUKTUREINRICHTUNG);
        kindergartenListElementModel1.setId(kindergarten1.getId());
        kindergartenListElementModel1.setNameEinrichtung(kindergarten1.getNameEinrichtung());
        kindergartenListElementModel1.setInfrastruktureinrichtungTyp(kindergarten1.getInfrastruktureinrichtungTyp());
        expectedInfrastruktureinrichtungList.add(kindergartenListElementModel1);

        var kindergartenListElementModel2 = new InfrastruktureinrichtungSearchResultModel();
        kindergartenListElementModel2.setType(SearchResultType.INFRASTRUKTUREINRICHTUNG);
        kindergartenListElementModel2.setId(kindergarten2.getId());
        kindergartenListElementModel2.setNameEinrichtung(kindergarten2.getNameEinrichtung());
        kindergartenListElementModel2.setInfrastruktureinrichtungTyp(kindergarten2.getInfrastruktureinrichtungTyp());
        expectedInfrastruktureinrichtungList.add(kindergartenListElementModel2);

        Mockito
            .when(this.infrastruktureinrichtungRepository.findAllByBauvorhabenId(bauvorhabenId))
            .thenReturn(listInfrastruktureinrichtung);

        List<InfrastruktureinrichtungSearchResultModel> infraResult =
            this.bauvorhabenService.getReferencedInfrastruktureinrichtungen(bauvorhabenId);

        assertThat(expectedInfrastruktureinrichtungList, is(infraResult));

        Mockito.verify(this.infrastruktureinrichtungRepository, Mockito.times(1)).findAllByBauvorhabenId(bauvorhabenId);
    }

    @Test
    void getBauvorhabenByIdTest() throws EntityNotFoundException {
        final UUID id = UUID.randomUUID();

        Mockito.when(this.bauvorhabenRepository.findById(id)).thenReturn(Optional.of(new Bauvorhaben()));
        final BauvorhabenModel result = this.bauvorhabenService.getBauvorhabenById(id);
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(id);
        Mockito.reset(this.bauvorhabenRepository);

        Mockito.when(this.bauvorhabenRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> this.bauvorhabenService.getBauvorhabenById(id));
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void saveBauvorhabenTest()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, EntityIsReferencedException {
        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(null);

        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(bauvorhaben.getId());

        final Bauvorhaben saveResult = new Bauvorhaben();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.bauvorhabenRepository.saveAndFlush(bauvorhabenEntity)).thenReturn(saveResult);

        final BauvorhabenModel result = this.bauvorhabenService.saveBauvorhaben(bauvorhaben, null);

        final BauvorhabenModel expected = new BauvorhabenModel();
        expected.setId(saveResult.getId());

        assertThat(result, is(expected));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).saveAndFlush(bauvorhabenEntity);
    }

    @Test
    void saveBauvorhabenBauvorhabennummerTest()
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, EntityIsReferencedException {
        // BauvorhabenModel
        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(null);

        final StadtbezirkModel sb_08 = new StadtbezirkModel();
        sb_08.setNummer("08");
        sb_08.setName("Stadtbezirk 8");

        final StadtbezirkModel sb_20 = new StadtbezirkModel();
        sb_20.setNummer("20");
        sb_20.setName("Stadtbezirk 20");

        final VerortungModel verortung = new VerortungModel();
        verortung.setStadtbezirke(Stream.of(sb_20, sb_08).collect(Collectors.toSet()));

        bauvorhaben.setVerortung(verortung);

        // Bauvorhaben Entity
        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(bauvorhaben.getId());

        final Stadtbezirk bauvorhabenEntity_sb_08 = new Stadtbezirk();
        bauvorhabenEntity_sb_08.setNummer("08");
        bauvorhabenEntity_sb_08.setName("Stadtbezirk 8");

        final Stadtbezirk bauvorhabenEntity_sb_20 = new Stadtbezirk();
        bauvorhabenEntity_sb_20.setNummer("20");
        bauvorhabenEntity_sb_20.setName("Stadtbezirk 20");

        final Verortung bauvorhabenEntityVerortung = new Verortung();
        bauvorhabenEntityVerortung.setStadtbezirke(
            Stream.of(bauvorhabenEntity_sb_20, bauvorhabenEntity_sb_08).collect(Collectors.toSet())
        );
        bauvorhabenEntity.setVerortung(bauvorhabenEntityVerortung);
        bauvorhabenEntity.setBauvorhabenNummer("08_0001");

        // Saved Bauvorhaben
        final Bauvorhaben saveResult = new Bauvorhaben();
        saveResult.setId(UUID.randomUUID());

        final Stadtbezirk saveResult_sb_08 = new Stadtbezirk();
        saveResult_sb_08.setNummer("08");
        saveResult_sb_08.setName("Stadtbezirk 8");

        final Stadtbezirk saveResult_sb_20 = new Stadtbezirk();
        saveResult_sb_20.setNummer("20");
        saveResult_sb_20.setName("Stadtbezirk 20");

        final Verortung saveResultVerortung = new Verortung();
        saveResultVerortung.setStadtbezirke(Stream.of(saveResult_sb_20, saveResult_sb_08).collect(Collectors.toSet()));
        saveResult.setVerortung(saveResultVerortung);
        saveResult.setBauvorhabenNummer("08_0001");

        Mockito
            .when(this.globalCounterRepository.findByCounterType(CounterType.NUMMER_BAUVORHABEN))
            .thenReturn(Optional.empty());
        final GlobalCounter bauvorhabennummerEntity = new GlobalCounter();
        bauvorhabennummerEntity.setCounterType(CounterType.NUMMER_BAUVORHABEN);
        bauvorhabennummerEntity.setCounter(1);

        final GlobalCounter saveBauvorhabennummer = new GlobalCounter();
        saveBauvorhabennummer.setId(UUID.randomUUID());
        saveBauvorhabennummer.setCounterType(CounterType.NUMMER_BAUVORHABEN);
        saveBauvorhabennummer.setCounter(1);

        Mockito
            .when(this.globalCounterRepository.saveAndFlush(bauvorhabennummerEntity))
            .thenReturn(saveBauvorhabennummer);

        Mockito.when(this.bauvorhabenRepository.saveAndFlush(bauvorhabenEntity)).thenReturn(saveResult);

        final BauvorhabenModel result = this.bauvorhabenService.saveBauvorhaben(bauvorhaben, null);

        // Expected BauvorhabenModel
        final BauvorhabenModel expected = new BauvorhabenModel();
        expected.setId(saveResult.getId());

        final StadtbezirkModel expected_sb_08 = new StadtbezirkModel();
        expected_sb_08.setNummer("08");
        expected_sb_08.setName("Stadtbezirk 8");

        final StadtbezirkModel expected_sb_20 = new StadtbezirkModel();
        expected_sb_20.setNummer("20");
        expected_sb_20.setName("Stadtbezirk 20");

        final VerortungModel expectedVerortung = new VerortungModel();
        expectedVerortung.setStadtbezirke(Stream.of(expected_sb_20, expected_sb_08).collect(Collectors.toSet()));
        expected.setVerortung(expectedVerortung);
        expected.setBauvorhabenNummer("08_0001");

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
            () -> this.bauvorhabenService.saveBauvorhaben(bauvorhabenModel2, null)
        );

        Mockito
            .verify(this.bauvorhabenRepository, Mockito.times(1))
            .findByNameVorhabenIgnoreCase(entity.getNameVorhaben());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).save(entity);
    }

    @Test
    void saveBauvorhabenReferencedByAbfrage()
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, EntityIsReferencedException {
        final UUID abfrageId = UUID.randomUUID();
        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(null);

        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(bauvorhaben.getId());

        final Bauvorhaben saveResult = new Bauvorhaben();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.bauvorhabenRepository.saveAndFlush(bauvorhabenEntity)).thenReturn(saveResult);

        final BauleitplanverfahrenModel abfrage = new BauleitplanverfahrenModel();
        abfrage.setId(abfrageId);

        final BauleitplanverfahrenModel abfrageToSave = new BauleitplanverfahrenModel();
        abfrageToSave.setId(abfrageId);
        abfrageToSave.setBauvorhaben(saveResult.getId());

        final BauleitplanverfahrenModel savedAbfrage = new BauleitplanverfahrenModel();
        savedAbfrage.setId(abfrageId);
        savedAbfrage.setBauvorhaben(saveResult.getId());

        Mockito.when(this.abfrageService.getById(abfrageId)).thenReturn(abfrage);
        Mockito.when(this.abfrageService.save(abfrageToSave)).thenReturn(savedAbfrage);

        final BauvorhabenModel result = this.bauvorhabenService.saveBauvorhaben(bauvorhaben, abfrageId);

        final BauvorhabenModel expected = new BauvorhabenModel();
        expected.setId(saveResult.getId());

        assertThat(result, is(expected));

        final BauleitplanverfahrenModel expectedAbfrage = new BauleitplanverfahrenModel();
        expectedAbfrage.setId(abfrageId);
        expectedAbfrage.setBauvorhaben(result.getId());
        assertThat(savedAbfrage, is(expectedAbfrage));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).saveAndFlush(bauvorhabenEntity);
        Mockito.verify(this.abfrageService, Mockito.times(1)).getById(abfrageId);
    }

    @Test
    void throwEntityNotFoundExceptionSaveBauvorhabenReferencedByAbfrage() throws EntityNotFoundException {
        final UUID abfrageId = UUID.randomUUID();
        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(null);

        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(bauvorhaben.getId());

        final Bauvorhaben saveResult = new Bauvorhaben();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.bauvorhabenRepository.saveAndFlush(bauvorhabenEntity)).thenReturn(saveResult);

        final Bauleitplanverfahren abfrage = new Bauleitplanverfahren();
        abfrage.setId(abfrageId);

        Mockito
            .when(this.abfrageService.getById(abfrage.getId()))
            .thenThrow(new EntityNotFoundException("Abfrage nicht gefunden"));

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.bauvorhabenService.saveBauvorhaben(bauvorhaben, abfrageId)
        );

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).saveAndFlush(bauvorhabenEntity);
        Mockito.verify(this.abfrageService, Mockito.times(1)).getById(abfrageId);
    }

    @Test
    void throwEntityIsReferencedExceptionSaveBauvorhabenReferencedByAbfrage()
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, EntityIsReferencedException {
        final UUID abfrageId = UUID.randomUUID();
        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(null);

        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(bauvorhaben.getId());

        final Bauvorhaben saveResult = new Bauvorhaben();
        saveResult.setId(UUID.randomUUID());

        Mockito.when(this.bauvorhabenRepository.saveAndFlush(bauvorhabenEntity)).thenReturn(saveResult);

        final BauleitplanverfahrenModel abfrage = new BauleitplanverfahrenModel();
        abfrage.setId(abfrageId);
        final BauvorhabenModel abfrageBauvorhaben = new BauvorhabenModel();
        abfrageBauvorhaben.setId(UUID.randomUUID());
        abfrageBauvorhaben.setNameVorhaben("Name Bauvorhaben");
        abfrage.setBauvorhaben(abfrageBauvorhaben.getId());

        Mockito.when(this.abfrageService.getById(abfrageId)).thenReturn(abfrage);

        Assertions.assertThrows(
            EntityIsReferencedException.class,
            () -> this.bauvorhabenService.saveBauvorhaben(bauvorhaben, abfrageId)
        );

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).saveAndFlush(bauvorhabenEntity);
        Mockito.verify(this.abfrageService, Mockito.times(1)).getById(abfrageId);
    }

    @Test
    void updateBauvorhabenTest()
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, FileHandlingFailedException, FileHandlingWithS3FailedException, EntityIsReferencedException {
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
        Mockito.when(this.abfrageRepository.findAllByBauvorhabenId(entity.getId())).thenReturn(Stream.empty());

        this.bauvorhabenService.deleteBauvorhaben(id);

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteBauvorhabenOfAbfrageException() {
        final UUID id = UUID.randomUUID();

        final Bauvorhaben entity = new Bauvorhaben();
        entity.setId(id);

        final Bauleitplanverfahren abfrage = new Bauleitplanverfahren();
        abfrage.setName("test1");

        Mockito.when(this.bauvorhabenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito.when(this.abfrageRepository.findAllByBauvorhabenId(entity.getId())).thenReturn(Stream.of(abfrage));

        assertThrows(EntityIsReferencedException.class, () -> this.bauvorhabenService.deleteBauvorhaben(id));

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

        assertThrows(EntityIsReferencedException.class, () -> this.bauvorhabenService.deleteBauvorhaben(id));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).findById(entity.getId());
        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).deleteById(id);
    }

    @Test
    void changeRelevanteAbfragevarianteSetNewRelevanteAbfragevarianteTest()
        throws AbfrageStatusNotAllowedException, EntityNotFoundException, BauvorhabenNotReferencedException, UniqueViolationException, OptimisticLockingException, EntityIsReferencedException {
        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(UUID.randomUUID());
        final AbfragevarianteBauleitplanverfahren abfragevarianteBauleitplanverfahren =
            new AbfragevarianteBauleitplanverfahren();
        abfragevarianteBauleitplanverfahren.setId(UUID.randomUUID());

        final AbfrageModel abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setId(UUID.randomUUID());
        abfrageModel.setName("test1");
        abfrageModel.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrageModel.setBauvorhaben(bauvorhabenEntity.getId());

        Mockito
            .when(this.abfrageService.getByAbfragevarianteId(abfragevarianteBauleitplanverfahren.getId()))
            .thenReturn(abfrageModel);
        Mockito
            .when(bauvorhabenRepository.findById(bauvorhabenEntity.getId()))
            .thenReturn(Optional.of(bauvorhabenEntity));

        Mockito
            .doCallRealMethod()
            .when(abfrageService)
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );

        Mockito
            .when(abfragevarianteRepository.findById(abfragevarianteBauleitplanverfahren.getId()))
            .thenReturn(Optional.of(abfragevarianteBauleitplanverfahren));

        this.bauvorhabenService.changeRelevanteAbfragevariante(abfragevarianteBauleitplanverfahren.getId());

        final Bauvorhaben bauvorhabenToVerify = new Bauvorhaben();
        bauvorhabenToVerify.setId(bauvorhabenEntity.getId());
        bauvorhabenToVerify.setRelevanteAbfragevariante(abfragevarianteBauleitplanverfahren);

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).saveAndFlush(bauvorhabenToVerify);
        Mockito
            .verify(this.abfrageService, Mockito.times(1))
            .getByAbfragevarianteId(abfragevarianteBauleitplanverfahren.getId());
        Mockito
            .verify(this.abfrageService, Mockito.times(1))
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );
    }

    @Test
    void changeRelevanteAbfragevarianteUnsetRelevanteAbfragevarianteTest()
        throws AbfrageStatusNotAllowedException, EntityNotFoundException, BauvorhabenNotReferencedException, UniqueViolationException, OptimisticLockingException, EntityIsReferencedException {
        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(UUID.randomUUID());
        final AbfragevarianteBauleitplanverfahren abfragevarianteBauleitplanverfahren =
            new AbfragevarianteBauleitplanverfahren();
        abfragevarianteBauleitplanverfahren.setId(UUID.randomUUID());
        bauvorhabenEntity.setRelevanteAbfragevariante(abfragevarianteBauleitplanverfahren);

        final AbfrageModel abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setId(UUID.randomUUID());
        abfrageModel.setName("test1");
        abfrageModel.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrageModel.setBauvorhaben(bauvorhabenEntity.getId());

        Mockito
            .when(this.abfrageService.getByAbfragevarianteId(abfragevarianteBauleitplanverfahren.getId()))
            .thenReturn(abfrageModel);
        Mockito
            .when(bauvorhabenRepository.findById(bauvorhabenEntity.getId()))
            .thenReturn(Optional.of(bauvorhabenEntity));

        Mockito
            .doCallRealMethod()
            .when(abfrageService)
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );

        Mockito
            .when(abfragevarianteRepository.findById(abfragevarianteBauleitplanverfahren.getId()))
            .thenReturn(Optional.of(abfragevarianteBauleitplanverfahren));

        this.bauvorhabenService.changeRelevanteAbfragevariante(abfragevarianteBauleitplanverfahren.getId());

        final Bauvorhaben bauvorhabenToVerify = new Bauvorhaben();
        bauvorhabenToVerify.setId(bauvorhabenEntity.getId());
        bauvorhabenToVerify.setRelevanteAbfragevariante(null);

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).saveAndFlush(bauvorhabenToVerify);
        Mockito
            .verify(this.abfrageService, Mockito.times(1))
            .getByAbfragevarianteId(abfragevarianteBauleitplanverfahren.getId());
        Mockito
            .verify(this.abfrageService, Mockito.times(1))
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );
    }

    @Test
    void changeRelevanteAbfragevarianteUniqueViolationExceptionTest()
        throws AbfrageStatusNotAllowedException, EntityNotFoundException {
        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(UUID.randomUUID());
        final AbfragevarianteBauleitplanverfahren abfragevarianteBauleitplanverfahren =
            new AbfragevarianteBauleitplanverfahren();
        abfragevarianteBauleitplanverfahren.setId(UUID.randomUUID());

        bauvorhabenEntity.setRelevanteAbfragevariante(abfragevarianteBauleitplanverfahren);

        final AbfrageModel abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setId(UUID.randomUUID());
        abfrageModel.setName("test1");
        abfrageModel.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrageModel.setBauvorhaben(bauvorhabenEntity.getId());

        final UUID otherAbfragevariante = UUID.randomUUID();
        Mockito.when(this.abfrageService.getByAbfragevarianteId(otherAbfragevariante)).thenReturn(abfrageModel);
        Mockito
            .when(bauvorhabenRepository.findById(bauvorhabenEntity.getId()))
            .thenReturn(Optional.of(bauvorhabenEntity));

        Mockito
            .doCallRealMethod()
            .when(abfrageService)
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );

        Mockito
            .when(abfragevarianteRepository.findById(abfragevarianteBauleitplanverfahren.getId()))
            .thenReturn(Optional.of(abfragevarianteBauleitplanverfahren));

        assertThrows(
            UniqueViolationException.class,
            () -> this.bauvorhabenService.changeRelevanteAbfragevariante(otherAbfragevariante)
        );

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).saveAndFlush(Mockito.any());
        Mockito.verify(this.abfrageService, Mockito.times(1)).getByAbfragevarianteId(otherAbfragevariante);
        Mockito
            .verify(this.abfrageService, Mockito.times(1))
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );
    }

    @Test
    void changeRelevanteAbfragevarianteEntityNotFoundExceptionTest()
        throws AbfrageStatusNotAllowedException, EntityNotFoundException {
        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(UUID.randomUUID());
        final AbfragevarianteBauleitplanverfahren abfragevarianteBauleitplanverfahren =
            new AbfragevarianteBauleitplanverfahren();
        abfragevarianteBauleitplanverfahren.setId(UUID.randomUUID());

        final AbfrageModel abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setId(UUID.randomUUID());
        abfrageModel.setName("test1");
        abfrageModel.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);
        abfrageModel.setBauvorhaben(bauvorhabenEntity.getId());

        Mockito
            .when(this.abfrageService.getByAbfragevarianteId(abfragevarianteBauleitplanverfahren.getId()))
            .thenReturn(abfrageModel);
        Mockito
            .when(bauvorhabenRepository.findById(bauvorhabenEntity.getId()))
            .thenReturn(Optional.of(bauvorhabenEntity));

        Mockito
            .doCallRealMethod()
            .when(abfrageService)
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );

        Mockito
            .when(abfragevarianteRepository.findById(abfragevarianteBauleitplanverfahren.getId()))
            .thenReturn(Optional.empty());

        assertThrows(
            EntityNotFoundException.class,
            () -> this.bauvorhabenService.changeRelevanteAbfragevariante(abfragevarianteBauleitplanverfahren.getId())
        );

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(0)).saveAndFlush(Mockito.any());
        Mockito
            .verify(this.abfrageService, Mockito.times(1))
            .getByAbfragevarianteId(abfragevarianteBauleitplanverfahren.getId());
        Mockito
            .verify(this.abfrageService, Mockito.times(1))
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );
    }

    @Test
    void changeRelevanteAbfragevarianteBauvorhabenNotReferencedExceptionTest()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        UUID abfragevarianteId = UUID.randomUUID();

        final AbfrageModel abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setId(UUID.randomUUID());
        abfrageModel.setName("test1");
        abfrageModel.setStatusAbfrage(StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG);

        Mockito.when(this.abfrageService.getByAbfragevarianteId(abfragevarianteId)).thenReturn(abfrageModel);
        Mockito
            .doCallRealMethod()
            .when(abfrageService)
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );

        assertThrows(
            BauvorhabenNotReferencedException.class,
            () -> this.bauvorhabenService.changeRelevanteAbfragevariante(abfragevarianteId)
        );

        Mockito.verify(this.abfrageService, Mockito.times(1)).getByAbfragevarianteId(abfragevarianteId);
        Mockito
            .verify(this.abfrageService, Mockito.times(1))
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );
    }

    @Test
    void changeRelevanteAbfragevarianteAbfrageStatusNotAllowedExceptionTest()
        throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        UUID abfragevarianteId = UUID.randomUUID();

        final AbfrageModel abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setId(UUID.randomUUID());
        abfrageModel.setName("test1");
        abfrageModel.setStatusAbfrage(StatusAbfrage.ANGELEGT);

        Mockito.when(this.abfrageService.getByAbfragevarianteId(abfragevarianteId)).thenReturn(abfrageModel);
        Mockito
            .doCallRealMethod()
            .when(abfrageService)
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );

        assertThrows(
            AbfrageStatusNotAllowedException.class,
            () -> this.bauvorhabenService.changeRelevanteAbfragevariante(abfragevarianteId)
        );

        Mockito.verify(this.abfrageService, Mockito.times(1)).getByAbfragevarianteId(abfragevarianteId);
        Mockito
            .verify(this.abfrageService, Mockito.times(1))
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                Mockito.any(AbfrageModel.class),
                Mockito.any(StatusAbfrage.class)
            );
    }

    @Test
    void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben() throws EntityIsReferencedException {
        final Bauleitplanverfahren abfrage = new Bauleitplanverfahren();
        abfrage.setName("test1");

        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(UUID.randomUUID());

        Mockito.when(this.abfrageRepository.findAllByBauvorhabenId(bauvorhaben.getId())).thenReturn(Stream.of());
        this.bauvorhabenService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(bauvorhaben);
        Mockito.reset(this.abfrageRepository);

        Mockito.when(this.abfrageRepository.findAllByBauvorhabenId(bauvorhaben.getId())).thenReturn(Stream.of(abfrage));
        assertThrows(
            EntityIsReferencedException.class,
            () ->
                this.bauvorhabenService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(bauvorhaben)
        );
        Mockito.reset(this.abfrageRepository);
    }

    @Test
    void throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben() {
        final Kinderkrippe kinderkrippe = new Kinderkrippe();
        kinderkrippe.setNameEinrichtung("Kinderkrippe");

        final BauvorhabenModel bauvorhaben = new BauvorhabenModel();
        bauvorhaben.setId(UUID.randomUUID());

        Mockito
            .when(this.infrastruktureinrichtungRepository.findAllByBauvorhabenId(bauvorhaben.getId()))
            .thenReturn(Stream.of(kinderkrippe));
        assertThrows(
            EntityIsReferencedException.class,
            () ->
                this.bauvorhabenService.throwEntityIsReferencedExceptionWhenInfrastruktureinrichtungIsReferencingBauvorhaben(
                        bauvorhaben
                    )
        );
    }
}
