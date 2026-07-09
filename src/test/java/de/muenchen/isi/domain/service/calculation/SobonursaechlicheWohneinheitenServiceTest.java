package de.muenchen.isi.domain.service.calculation;

import static de.muenchen.isi.TestConstants.EOF;
import static de.muenchen.isi.TestConstants.FF;
import static de.muenchen.isi.TestConstants.MM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.mapper.BaurateDomainMapper;
import de.muenchen.isi.domain.mapper.BaurateDomainMapperImpl;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Bauratenmethodik;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.Jahresrate;
import de.muenchen.isi.infrastructure.repository.stammdaten.IdealtypischeBaurateRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.UmlegungFoerderartenRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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
public class SobonursaechlicheWohneinheitenServiceTest {

    private SobonursaechlicheWohneinheitenService sobonursaechlicheWohneinheitenService;

    @Mock
    private UmlegungFoerderartenRepository umlegungFoerderartenRepository;

    @Mock
    private StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    @Mock
    private IdealtypischeBaurateRepository idealtypischeBaurateRepository;

    private final BaurateDomainMapper baurateDomainMapper = new BaurateDomainMapperImpl();

    @BeforeEach
    public void beforeEach() {
        final var foerdermixUmlageService = new FoerdermixUmlageService(umlegungFoerderartenRepository);
        this.sobonursaechlicheWohneinheitenService = new SobonursaechlicheWohneinheitenService(
            foerdermixUmlageService,
            staedtebaulicheOrientierungswertRepository,
            baurateDomainMapper,
            idealtypischeBaurateRepository
        );
        Mockito.reset(umlegungFoerderartenRepository, staedtebaulicheOrientierungswertRepository);
    }

    @Test
    void calculateSobonursaechlicheWohneinheitenTest() throws CalculationException {
        FoerdermixUmlageServiceTest.fillUmlegungFoerderartenRepository(umlegungFoerderartenRepository);
        PlanungsursaechlicheWohneinheitenServiceTest.fillStaedtebaulicheOrientierungswertRepository(
            staedtebaulicheOrientierungswertRepository
        );

        // Fördermixe
        final var FF50 = new FoerderartModel();
        FF50.setBezeichnung(FF);
        FF50.setAnteilProzent(new BigDecimal("50"));

        final var MM30 = new FoerderartModel();
        MM30.setBezeichnung(MM);
        MM30.setAnteilProzent(new BigDecimal("30"));

        final var EOF20 = new FoerderartModel();
        EOF20.setBezeichnung(EOF);
        EOF20.setAnteilProzent(new BigDecimal("20"));

        final var meinFoerdermix = new FoerdermixModel();
        meinFoerdermix.setFoerderarten(List.of(FF50, EOF20, MM30));

        final var baurate2024 = new BaurateModel();
        baurate2024.setJahr(2024);
        baurate2024.setFoerdermix(meinFoerdermix);

        final var baugebiet = new BaugebietModel();
        baugebiet.setBauraten(Collections.singletonList(baurate2024));

        final var bauabschnitt = new BauabschnittModel();
        bauabschnitt.setBaugebiete(Collections.singletonList(baugebiet));
        final var bauabschnitte = Collections.singletonList(bauabschnitt);

        // 1. Test unter 1000 Wohneinheiten
        final var expected = List.of(
            new WohneinheitenProFoerderartProJahrModel(FF, "2024", new BigDecimal("157.894736842105263")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2024", new BigDecimal("66.666666666666667")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2024", new BigDecimal("90.000000000000000"))
        );

        final var actual = sobonursaechlicheWohneinheitenService.calculateSobonursaechlicheWohneinheiten(
            new BigDecimal(30000),
            bauabschnitte,
            SobonOrientierungswertJahr.JAHR_2022,
            LocalDate.of(2013, 1, 1),
            meinFoerdermix,
            Bauratenmethodik.ALTE_BAURATENMETHODIK
        );

        System.out.println("----------------- TEST A ---------------------------------");
        for (WohneinheitenProFoerderartProJahrModel weModel : actual) {
            System.out.println("Jahr: " + weModel.getJahr()); // Jahr/Info aus Baurate
            System.out.println("Förderart: " + weModel.getFoerderart());
            System.out.println("Wohneinheiten: " + weModel.getWohneinheiten());
            System.out.println("--------------------------------------------------");
        }

        assertThat(actual, containsInAnyOrder(expected.toArray()));

        // 2. Test über 1000 Wohneinheiten mit einem 1000er Block/Jahr
        final var expectedB = List.of(
            new WohneinheitenProFoerderartProJahrModel(FF, "2024", new BigDecimal("501.952035694366947")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2024", new BigDecimal("211.935303959843822")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2024", new BigDecimal("286.112660345789160")),
            new WohneinheitenProFoerderartProJahrModel(FF, "2025", new BigDecimal("182.258490621422526")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2025", new BigDecimal("76.953584929045067")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2025", new BigDecimal("103.887339654210840"))
        );

        final var actualB = sobonursaechlicheWohneinheitenService.calculateSobonursaechlicheWohneinheiten(
            new BigDecimal(130000),
            bauabschnitte,
            SobonOrientierungswertJahr.JAHR_2022,
            LocalDate.of(2013, 1, 1),
            meinFoerdermix,
            Bauratenmethodik.ALTE_BAURATENMETHODIK
        );

        System.out.println("----------------- TEST B ---------------------------------");
        for (WohneinheitenProFoerderartProJahrModel weModel : actualB) {
            System.out.println("Jahr: " + weModel.getJahr()); // Jahr/Info aus Baurate
            System.out.println("Förderart: " + weModel.getFoerderart());
            System.out.println("Wohneinheiten: " + weModel.getWohneinheiten());
            System.out.println("--------------------------------------------------");
        }

        assertThat(actualB, containsInAnyOrder(expectedB.toArray()));

        // 3. Test über 1000 Wohneinheiten mit zwei 1000er Blöcken/Jahren
        final var expectedC = List.of(
            new WohneinheitenProFoerderartProJahrModel(FF, "2024", new BigDecimal("501.952035694367000")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2024", new BigDecimal("211.935303959843844")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2024", new BigDecimal("286.112660345789190")),
            new WohneinheitenProFoerderartProJahrModel(FF, "2025", new BigDecimal("501.952035694367000")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2025", new BigDecimal("211.935303959843844")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2025", new BigDecimal("286.112660345789190")),
            new WohneinheitenProFoerderartProJahrModel(FF, "2026", new BigDecimal("206.622244400739684")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2026", new BigDecimal("87.240503191423422")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2026", new BigDecimal("117.774679308421620"))
        );

        final var actualC = sobonursaechlicheWohneinheitenService.calculateSobonursaechlicheWohneinheiten(
            new BigDecimal(230000),
            bauabschnitte,
            SobonOrientierungswertJahr.JAHR_2022,
            LocalDate.of(2013, 1, 1),
            meinFoerdermix,
            Bauratenmethodik.ALTE_BAURATENMETHODIK
        );

        System.out.println("----------------- TEST C ---------------------------------");
        for (WohneinheitenProFoerderartProJahrModel weModel : actualC) {
            System.out.println("Jahr: " + weModel.getJahr()); // Jahr/Info aus Baurate
            System.out.println("Förderart: " + weModel.getFoerderart());
            System.out.println("Wohneinheiten: " + weModel.getWohneinheiten());
            System.out.println("--------------------------------------------------");
        }
        assertThat(actualC, containsInAnyOrder(expectedC.toArray()));
    }

    @Test
    void calculateSobonursaechlicheWohneinheiten_exakt2000WE_keinSpuriousRestjahr() throws CalculationException {
        // Regression test: when total WE is an exact multiple of 1000 the remainder block must NOT be emitted.
        // Setup: no umlegung (mocks return empty), FF anteil=100%, orientation=95 → 190000 GF → exactly 2000 WE.
        PlanungsursaechlicheWohneinheitenServiceTest.fillStaedtebaulicheOrientierungswertRepository(
            staedtebaulicheOrientierungswertRepository
        );

        final var ff100 = new FoerderartModel();
        ff100.setBezeichnung(FF);
        ff100.setAnteilProzent(new BigDecimal("100"));

        final var foerdermix = new FoerdermixModel();
        foerdermix.setFoerderarten(List.of(ff100));

        final var baurate = new BaurateModel();
        baurate.setJahr(2024);
        baurate.setFoerdermix(foerdermix);

        final var baugebiet = new BaugebietModel();
        baugebiet.setBauraten(Collections.singletonList(baurate));

        final var bauabschnitt = new BauabschnittModel();
        bauabschnitt.setBaugebiete(Collections.singletonList(baugebiet));

        // sobonGf=190000, orientation=95, anteil=100% → WE = 190000/95 = 2000 exactly (2 blocks of 1000, no rest)
        final var actual = sobonursaechlicheWohneinheitenService.calculateSobonursaechlicheWohneinheiten(
            new BigDecimal(190000),
            Collections.singletonList(bauabschnitt),
            SobonOrientierungswertJahr.JAHR_2022,
            LocalDate.of(2013, 1, 1),
            foerdermix,
            Bauratenmethodik.ALTE_BAURATENMETHODIK
        );

        final var expected = List.of(
            new WohneinheitenProFoerderartProJahrModel(FF, "2024", new BigDecimal("1000.000000000000000")),
            new WohneinheitenProFoerderartProJahrModel(FF, "2025", new BigDecimal("1000.000000000000000"))
        );
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void calculateSobonursaechlicheWohneinheitenNeueBauratenmethodik() throws CalculationException {
        FoerdermixUmlageServiceTest.fillUmlegungFoerderartenRepository(umlegungFoerderartenRepository);
        PlanungsursaechlicheWohneinheitenServiceTest.fillStaedtebaulicheOrientierungswertRepository(
            staedtebaulicheOrientierungswertRepository
        );

        final var FF50 = new FoerderartModel();
        FF50.setBezeichnung(FF);
        FF50.setAnteilProzent(new BigDecimal("50"));

        final var MM30 = new FoerderartModel();
        MM30.setBezeichnung(MM);
        MM30.setAnteilProzent(new BigDecimal("30"));

        final var EOF20 = new FoerderartModel();
        EOF20.setBezeichnung(EOF);
        EOF20.setAnteilProzent(new BigDecimal("20"));

        final var meinFoerdermix = new FoerdermixModel();
        meinFoerdermix.setFoerderarten(List.of(FF50, EOF20, MM30));

        final var baurate2024 = new BaurateModel();
        baurate2024.setJahr(2024);
        baurate2024.setFoerdermix(meinFoerdermix);

        final var baugebiet = new BaugebietModel();
        baugebiet.setBauraten(Collections.singletonList(baurate2024));

        final var bauabschnitt = new BauabschnittModel();
        bauabschnitt.setBaugebiete(Collections.singletonList(baugebiet));
        final var bauabschnitte = Collections.singletonList(bauabschnitt);

        // Idealtypische Baurate: 2 Jahresraten (60% Jahr 1, 40% Jahr 2)
        final var jahresrate1 = new Jahresrate();
        jahresrate1.setJahr(1);
        jahresrate1.setRate(new BigDecimal("0.60"));

        final var jahresrate2 = new Jahresrate();
        jahresrate2.setJahr(2);
        jahresrate2.setRate(new BigDecimal("0.40"));

        final var idealtypischeBaurate = new IdealtypischeBaurate();
        idealtypischeBaurate.setTyp(IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT);
        idealtypischeBaurate.setVon(BigDecimal.ZERO);
        idealtypischeBaurate.setBisExklusiv(new BigDecimal("1000"));
        idealtypischeBaurate.setJahresraten(List.of(jahresrate1, jahresrate2));

        Mockito.when(
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                Mockito.eq(IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT),
                Mockito.any(BigDecimal.class)
            )
        ).thenReturn(java.util.Optional.of(idealtypischeBaurate));

        // Für sobonGf=30000, JAHR_2022, 2013-01-01 ergibt calculateWohneinheiten:
        // FF: 157.894736842105263, EOF: 66.666666666666667, MM: 90.000000000000000
        // Jahr 1 (ausgabeJahr=2024): FF * 0.60 floor 15, EOF * 0.60 floor 15, MM * 0.60 floor 15
        // Jahr 2 (ausgabeJahr=2025, letztes): Reste
        final var expected = List.of(
            new WohneinheitenProFoerderartProJahrModel(FF, "2024", new BigDecimal("94.736842105263157")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2024", new BigDecimal("40.000000000000000")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2024", new BigDecimal("54.000000000000000")),
            new WohneinheitenProFoerderartProJahrModel(FF, "2025", new BigDecimal("63.157894736842106")),
            new WohneinheitenProFoerderartProJahrModel(EOF, "2025", new BigDecimal("26.666666666666667")),
            new WohneinheitenProFoerderartProJahrModel(MM, "2025", new BigDecimal("36.000000000000000"))
        );

        final var actual = sobonursaechlicheWohneinheitenService.calculateSobonursaechlicheWohneinheiten(
            new BigDecimal(30000),
            bauabschnitte,
            SobonOrientierungswertJahr.JAHR_2022,
            LocalDate.of(2013, 1, 1),
            meinFoerdermix,
            Bauratenmethodik.NEUE_BAURATENMETHODIK
        );

        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void calculateSobonursaechlicheWohneinheitenNeueBauratenmethodikKeineBaurateGefunden() {
        FoerdermixUmlageServiceTest.fillUmlegungFoerderartenRepository(umlegungFoerderartenRepository);
        PlanungsursaechlicheWohneinheitenServiceTest.fillStaedtebaulicheOrientierungswertRepository(
            staedtebaulicheOrientierungswertRepository
        );

        final var FF50 = new FoerderartModel();
        FF50.setBezeichnung(FF);
        FF50.setAnteilProzent(new BigDecimal("50"));

        final var meinFoerdermix = new FoerdermixModel();
        meinFoerdermix.setFoerderarten(List.of(FF50));

        final var baurate2024 = new BaurateModel();
        baurate2024.setJahr(2024);
        baurate2024.setFoerdermix(meinFoerdermix);

        final var baugebiet = new BaugebietModel();
        baugebiet.setBauraten(Collections.singletonList(baurate2024));

        final var bauabschnitt = new BauabschnittModel();
        bauabschnitt.setBaugebiete(Collections.singletonList(baugebiet));
        final var bauabschnitte = Collections.singletonList(bauabschnitt);

        Mockito.when(
            idealtypischeBaurateRepository.findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                Mockito.eq(IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT),
                Mockito.any(BigDecimal.class)
            )
        ).thenReturn(java.util.Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(CalculationException.class, () ->
            sobonursaechlicheWohneinheitenService.calculateSobonursaechlicheWohneinheiten(
                new BigDecimal(30000),
                bauabschnitte,
                SobonOrientierungswertJahr.JAHR_2022,
                LocalDate.of(2013, 1, 1),
                meinFoerdermix,
                Bauratenmethodik.NEUE_BAURATENMETHODIK
            )
        );
    }
}
