package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.BauvorhabenNotReferencedException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.mapper.AbfrageAltDomainMapper;
import de.muenchen.isi.domain.mapper.AbfrageAltDomainMapperImpl;
import de.muenchen.isi.domain.mapper.AbfragevarianteAltDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauabschnittDomainMapperImpl;
import de.muenchen.isi.domain.mapper.BauvorhabenDomainMapper;
import de.muenchen.isi.domain.mapper.BauvorhabenDomainMapperImpl;
import de.muenchen.isi.domain.mapper.DokumentDomainMapperImpl;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapper;
import de.muenchen.isi.domain.mapper.InfrastruktureinrichtungDomainMapperImpl;
import de.muenchen.isi.domain.model.AbfrageAltModel;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.BauvorhabenModel;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.model.abfrageAbfrageerstellerAngelegt.AbfrageAngelegtModel;
import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.domain.model.common.VerortungModel;
import de.muenchen.isi.domain.model.enums.SearchResultType;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.InfrastruktureinrichtungModel;
import de.muenchen.isi.domain.model.infrastruktureinrichtung.KinderkrippeModel;
import de.muenchen.isi.domain.model.search.response.AbfrageSearchResultModel;
import de.muenchen.isi.domain.model.search.response.InfrastruktureinrichtungSearchResultModel;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import de.muenchen.isi.infrastructure.entity.AbfrageAlt;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.Infrastrukturabfrage;
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
import de.muenchen.isi.infrastructure.repository.AbfragevarianteRepository;
import de.muenchen.isi.infrastructure.repository.BauvorhabenRepository;
import de.muenchen.isi.infrastructure.repository.InfrastrukturabfrageRepository;
import de.muenchen.isi.infrastructure.repository.InfrastruktureinrichtungRepository;
import de.muenchen.isi.infrastructure.repository.common.GlobalCounterRepository;
import de.muenchen.isi.infrastructure.repository.common.KommentarRepository;
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

    private final InfrastruktureinrichtungDomainMapper infrastruktureinrichtungDomainMapper =
        new InfrastruktureinrichtungDomainMapperImpl();

    private final AbfrageAltDomainMapper abfrageDomainMapper = new AbfrageAltDomainMapperImpl(
        new AbfragevarianteAltDomainMapperImpl(new BauabschnittDomainMapperImpl()),
        new DokumentDomainMapperImpl()
    );

    private BauvorhabenService bauvorhabenService;

    @Mock
    private AbfrageAltService abfrageService;

    @Mock
    private BauvorhabenRepository bauvorhabenRepository;

    @Mock
    private InfrastrukturabfrageRepository infrastrukturabfrageRepository;

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
    public void beforeEach() {
        this.bauvorhabenService =
            new BauvorhabenService(
                this.bauvorhabenDomainMapper,
                this.infrastruktureinrichtungDomainMapper,
                this.abfrageDomainMapper,
                this.bauvorhabenRepository,
                this.infrastrukturabfrageRepository,
                this.infrastruktureinrichtungRepository,
                this.abfragevarianteRepository,
                this.globalCounterRepository,
                this.abfrageService,
                this.dokumentService,
                this.kommentarRepository
            );

        Mockito.reset(
            this.bauvorhabenRepository,
            this.infrastrukturabfrageRepository,
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

        final Infrastrukturabfrage abfrage1 = new Infrastrukturabfrage();
        abfrage1.setId(UUID.randomUUID());
        abfrage1.setAbfrage(new AbfrageAlt());
        abfrage1.getAbfrage().setNameAbfrage("NameAbfrage1");
        abfrage1.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        abfrage1.getAbfrage().setFristStellungnahme(LocalDate.of(2022, 11, 1));
        abfrage1.getAbfrage().setBauvorhaben(bauvorhaben);

        final Infrastrukturabfrage abfrage2 = new Infrastrukturabfrage();
        abfrage2.setId(UUID.randomUUID());
        abfrage2.setAbfrage(new AbfrageAlt());
        abfrage2.getAbfrage().setNameAbfrage("NameAbfrage2");
        abfrage2.getAbfrage().setStatusAbfrage(StatusAbfrage.ANGELEGT);
        abfrage2.getAbfrage().setFristStellungnahme(LocalDate.of(2022, 9, 1));
        abfrage2.getAbfrage().setBauvorhaben(bauvorhaben);

        final Infrastrukturabfrage abfrage3 = new Infrastrukturabfrage();
        abfrage3.setId(UUID.randomUUID());
        abfrage3.setAbfrage(new AbfrageAlt());
        abfrage3.getAbfrage().setNameAbfrage("NameAbfrage3");
        abfrage3.getAbfrage().setStatusAbfrage(StatusAbfrage.OFFEN);
        abfrage3.getAbfrage().setFristStellungnahme(LocalDate.of(2022, 12, 1));
        abfrage3.getAbfrage().setBauvorhaben(bauvorhaben);

        final Stream<Infrastrukturabfrage> listInfrastrukturabfrage = Stream.of(abfrage1, abfrage2, abfrage3);

        final List<AbfrageSearchResultModel> expectedAbfrageList = new ArrayList<>();

        var abfrageListElementModel1 = new AbfrageSearchResultModel();
        abfrageListElementModel1.setType(SearchResultType.INFRASTRUKTURABFRAGE);
        abfrageListElementModel1.setId(abfrage1.getId());
        abfrageListElementModel1.setNameAbfrage(abfrage1.getAbfrage().getNameAbfrage());
        abfrageListElementModel1.setStatusAbfrage(abfrage1.getAbfrage().getStatusAbfrage());
        abfrageListElementModel1.setFristStellungnahme(abfrage1.getAbfrage().getFristStellungnahme());
        abfrageListElementModel1.setType(SearchResultType.INFRASTRUKTURABFRAGE);
        abfrageListElementModel1.setBauvorhaben(bauvorhabenId);
        expectedAbfrageList.add(abfrageListElementModel1);

        var abfrageListElementModel2 = new AbfrageSearchResultModel();
        abfrageListElementModel2.setType(SearchResultType.INFRASTRUKTURABFRAGE);
        abfrageListElementModel2.setId(abfrage2.getId());
        abfrageListElementModel2.setNameAbfrage(abfrage2.getAbfrage().getNameAbfrage());
        abfrageListElementModel2.setStatusAbfrage(abfrage2.getAbfrage().getStatusAbfrage());
        abfrageListElementModel2.setFristStellungnahme(abfrage2.getAbfrage().getFristStellungnahme());
        abfrageListElementModel2.setType(SearchResultType.INFRASTRUKTURABFRAGE);
        abfrageListElementModel2.setBauvorhaben(bauvorhabenId);
        expectedAbfrageList.add(abfrageListElementModel2);

        var abfrageListElementModel3 = new AbfrageSearchResultModel();
        abfrageListElementModel3.setType(SearchResultType.INFRASTRUKTURABFRAGE);
        abfrageListElementModel3.setId(abfrage3.getId());
        abfrageListElementModel3.setNameAbfrage(abfrage3.getAbfrage().getNameAbfrage());
        abfrageListElementModel3.setStatusAbfrage(abfrage3.getAbfrage().getStatusAbfrage());
        abfrageListElementModel3.setFristStellungnahme(abfrage3.getAbfrage().getFristStellungnahme());
        abfrageListElementModel3.setType(SearchResultType.INFRASTRUKTURABFRAGE);
        abfrageListElementModel3.setBauvorhaben(bauvorhabenId);
        expectedAbfrageList.add(abfrageListElementModel3);

        Mockito
            .when(
                this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenIdOrderByCreatedDateTimeDesc(
                        bauvorhabenId
                    )
            )
            .thenReturn(listInfrastrukturabfrage);

        List<AbfrageSearchResultModel> abfrageResult =
            this.bauvorhabenService.getReferencedInfrastrukturabfragen(bauvorhabenId);

        assertThat(expectedAbfrageList, is(abfrageResult));

        Mockito
            .verify(this.infrastrukturabfrageRepository, Mockito.times(1))
            .findAllByAbfrageBauvorhabenIdOrderByCreatedDateTimeDesc(bauvorhabenId);
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

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(abfrageId);
        abfrage.setAbfrage(new AbfrageAltModel());

        final InfrastrukturabfrageModel abfrageToSave = new InfrastrukturabfrageModel();
        abfrageToSave.setId(abfrageId);
        abfrageToSave.setAbfrage(new AbfrageAltModel());
        abfrageToSave.getAbfrage().setBauvorhaben(this.bauvorhabenDomainMapper.entity2Model(saveResult));

        final InfrastrukturabfrageModel savedAbfrage = new InfrastrukturabfrageModel();
        savedAbfrage.setId(abfrageId);
        savedAbfrage.setAbfrage(new AbfrageAltModel());
        savedAbfrage.getAbfrage().setBauvorhaben(this.bauvorhabenDomainMapper.entity2Model(saveResult));

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(abfrageId)).thenReturn(abfrage);
        Mockito.when(this.abfrageService.saveInfrastrukturabfrage(abfrageToSave)).thenReturn(savedAbfrage);

        final BauvorhabenModel result = this.bauvorhabenService.saveBauvorhaben(bauvorhaben, abfrageId);

        final BauvorhabenModel expected = new BauvorhabenModel();
        expected.setId(saveResult.getId());

        assertThat(result, is(expected));

        final InfrastrukturabfrageModel expectedAbfrage = new InfrastrukturabfrageModel();
        expectedAbfrage.setId(abfrageId);
        expectedAbfrage.setAbfrage(new AbfrageAltModel());
        expectedAbfrage.getAbfrage().setBauvorhaben(result);
        assertThat(savedAbfrage, is(expectedAbfrage));

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).saveAndFlush(bauvorhabenEntity);
        Mockito.verify(this.abfrageService, Mockito.times(1)).getInfrastrukturabfrageById(abfrageId);
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

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(abfrageId);

        Mockito
            .when(this.abfrageService.getInfrastrukturabfrageById(abfrage.getId()))
            .thenThrow(new EntityNotFoundException("Abfrage nicht gefunden"));

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> this.bauvorhabenService.saveBauvorhaben(bauvorhaben, abfrageId)
        );

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).saveAndFlush(bauvorhabenEntity);
        Mockito.verify(this.abfrageService, Mockito.times(1)).getInfrastrukturabfrageById(abfrageId);
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

        final InfrastrukturabfrageModel abfrage = new InfrastrukturabfrageModel();
        abfrage.setId(abfrageId);
        abfrage.setAbfrage(new AbfrageAltModel());
        final BauvorhabenModel abfrageBauvorhaben = new BauvorhabenModel();
        abfrageBauvorhaben.setId(UUID.randomUUID());
        abfrageBauvorhaben.setNameVorhaben("Name Bauvorhaben");
        abfrage.getAbfrage().setBauvorhaben(abfrageBauvorhaben);

        Mockito.when(this.abfrageService.getInfrastrukturabfrageById(abfrageId)).thenReturn(abfrage);

        Assertions.assertThrows(
            EntityIsReferencedException.class,
            () -> this.bauvorhabenService.saveBauvorhaben(bauvorhaben, abfrageId)
        );

        Mockito.verify(this.bauvorhabenRepository, Mockito.times(1)).saveAndFlush(bauvorhabenEntity);
        Mockito.verify(this.abfrageService, Mockito.times(1)).getInfrastrukturabfrageById(abfrageId);
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
        final AbfrageAlt abfrage1 = new AbfrageAlt();
        abfrage1.setNameAbfrage("test1");
        infrastrukturabfrage.setAbfrage(abfrage1);

        Mockito.when(this.bauvorhabenRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        Mockito
            .when(this.infrastrukturabfrageRepository.findAllByAbfrageBauvorhabenId(entity.getId()))
            .thenReturn(Stream.of(infrastrukturabfrage));

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

        assertThrows(
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

        assertThrows(
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
    void changeRelevanteAbfragevarianteTest()
        throws BauvorhabenNotReferencedException, UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, EntityIsReferencedException {
        final UUID bauvorhabenId = UUID.randomUUID();
        final String bauvorhabenName = "Bauvorhaben";
        final UUID abfrageId = UUID.randomUUID();
        final UUID abfragevarianteId = UUID.randomUUID();
        final UUID otherAbfragevarianteId = UUID.randomUUID();

        final BauvorhabenModel bauvorhabenModel = new BauvorhabenModel();
        bauvorhabenModel.setId(bauvorhabenId);
        bauvorhabenModel.setNameVorhaben(bauvorhabenName);

        final Bauvorhaben bauvorhabenEntity = new Bauvorhaben();
        bauvorhabenEntity.setId(bauvorhabenId);
        bauvorhabenEntity.setNameVorhaben(bauvorhabenName);

        final InfrastrukturabfrageModel infrastrukturabfrageModel = new InfrastrukturabfrageModel();
        infrastrukturabfrageModel.setId(abfrageId);
        final AbfrageAltModel abfrageModel = new AbfrageAltModel();
        abfrageModel.setStatusAbfrage(StatusAbfrage.OFFEN);
        infrastrukturabfrageModel.setAbfrage(abfrageModel);

        final AbfragevarianteModel abfragevarianteModel = new AbfragevarianteModel();
        abfragevarianteModel.setId(abfragevarianteId);

        final AbfragevarianteModel otherAbfragevarianteModel = new AbfragevarianteModel();
        otherAbfragevarianteModel.setId(otherAbfragevarianteId);

        Mockito
            .when(abfragevarianteRepository.findAbfrageAbfragevariantenIdById(abfragevarianteId.toString()))
            .thenReturn(Optional.of(abfrageId.toString()));
        Mockito
            .when(abfragevarianteRepository.findAbfrageAbfragevariantenIdById(otherAbfragevarianteId.toString()))
            .thenReturn(Optional.of(abfrageId.toString()));
        Mockito.when(abfrageService.getInfrastrukturabfrageById(abfrageId)).thenReturn(infrastrukturabfrageModel);
        Mockito
            .doNothing()
            .when(abfrageService)
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                abfrageModel,
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG
            );
        Mockito
            .when(bauvorhabenRepository.findByNameVorhabenIgnoreCase(bauvorhabenName))
            .thenReturn(Optional.of(bauvorhabenEntity));
        Mockito.when(bauvorhabenRepository.saveAndFlush(bauvorhabenEntity)).thenReturn(bauvorhabenEntity);

        assertThrows(
            BauvorhabenNotReferencedException.class,
            () -> bauvorhabenService.changeRelevanteAbfragevariante(abfragevarianteModel)
        );
        abfrageModel.setBauvorhaben(bauvorhabenModel);
        bauvorhabenService.changeRelevanteAbfragevariante(abfragevarianteModel);
        assertThat(bauvorhabenModel.getRelevanteAbfragevariante(), sameInstance(abfragevarianteModel));
        assertThrows(
            UniqueViolationException.class,
            () -> bauvorhabenService.changeRelevanteAbfragevariante(otherAbfragevarianteModel)
        );
        bauvorhabenService.changeRelevanteAbfragevariante(abfragevarianteModel);
        assertThat(bauvorhabenModel.getRelevanteAbfragevariante(), nullValue());

        Mockito
            .verify(abfragevarianteRepository, Mockito.times(4))
            .findAbfrageAbfragevariantenIdById(abfragevarianteId.toString());
        Mockito
            .verify(abfragevarianteRepository, Mockito.times(1))
            .findAbfrageAbfragevariantenIdById(otherAbfragevarianteId.toString());
        Mockito.verify(abfrageService, Mockito.times(5)).getInfrastrukturabfrageById(abfrageId);
        Mockito
            .verify(abfrageService, Mockito.times(4))
            .throwAbfrageStatusNotAllowedExceptionWhenStatusAbfrageIsInvalid(
                abfrageModel,
                StatusAbfrage.IN_BEARBEITUNG_SACHBEARBEITUNG
            );
        Mockito.verify(bauvorhabenRepository, Mockito.times(2)).findByNameVorhabenIgnoreCase(bauvorhabenName);
        Mockito.verify(bauvorhabenRepository, Mockito.times(2)).saveAndFlush(Mockito.any(Bauvorhaben.class));
    }

    @Test
    void throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben() throws EntityIsReferencedException {
        final Infrastrukturabfrage infrastrukturabfrage = new Infrastrukturabfrage();
        final AbfrageAlt abfrage1 = new AbfrageAlt();
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
        assertThrows(
            EntityIsReferencedException.class,
            () ->
                this.bauvorhabenService.throwEntityIsReferencedExceptionWhenAbfrageIsReferencingBauvorhaben(bauvorhaben)
        );
        Mockito.reset(this.infrastrukturabfrageRepository);
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
