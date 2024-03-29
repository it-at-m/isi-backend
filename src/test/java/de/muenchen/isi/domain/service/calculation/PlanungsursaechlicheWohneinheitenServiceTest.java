package de.muenchen.isi.domain.service.calculation;

import static de.muenchen.isi.TestConstants.BAU;
import static de.muenchen.isi.TestConstants.EOF;
import static de.muenchen.isi.TestConstants.FF;
import static de.muenchen.isi.TestConstants.FH;
import static de.muenchen.isi.TestConstants.KMB;
import static de.muenchen.isi.TestConstants.MM;
import static de.muenchen.isi.TestConstants.PMB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtebaulicheOrientierungswert;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.UmlegungFoerderartenRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
public class PlanungsursaechlicheWohneinheitenServiceTest {

    private PlanungsursaechlicheWohneinheitenService planungsursaechlicheWohneinheitenService;

    @Mock
    private UmlegungFoerderartenRepository umlegungFoerderartenRepository;

    @Mock
    private StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    @BeforeEach
    public void beforeEach() {
        final var foerdermixUmlageService = new FoerdermixUmlageService(umlegungFoerderartenRepository);
        this.planungsursaechlicheWohneinheitenService =
            new PlanungsursaechlicheWohneinheitenService(
                foerdermixUmlageService,
                staedtebaulicheOrientierungswertRepository
            );
        Mockito.reset(umlegungFoerderartenRepository, staedtebaulicheOrientierungswertRepository);
    }

    @Test
    void calculatePlanungsursaechlicheWohneinheitenTest() {
        FoerdermixUmlageServiceTest.fillUmlegungFoerderartenRepository(umlegungFoerderartenRepository);
        fillStaedtebaulicheOrientierungswertRepository(staedtebaulicheOrientierungswertRepository);

        final var FF100 = new FoerderartModel();
        FF100.setBezeichnung(FF);
        FF100.setAnteilProzent(new BigDecimal("100"));

        final var MM75 = new FoerderartModel();
        MM75.setBezeichnung(MM);
        MM75.setAnteilProzent(new BigDecimal("75"));

        final var EOF25 = new FoerderartModel();
        EOF25.setBezeichnung(EOF);
        EOF25.setAnteilProzent(new BigDecimal("25"));

        final var FH50 = new FoerderartModel();
        FH50.setBezeichnung(FH);
        FH50.setAnteilProzent(new BigDecimal("50"));

        final var PMB75 = new FoerderartModel();
        PMB75.setBezeichnung(PMB);
        PMB75.setAnteilProzent(new BigDecimal("75"));

        final var KMB25 = new FoerderartModel();
        KMB25.setBezeichnung(KMB);
        KMB25.setAnteilProzent(new BigDecimal("25"));

        final var BAU50 = new FoerderartModel();
        BAU50.setBezeichnung(BAU);
        BAU50.setAnteilProzent(new BigDecimal("50"));

        final var foerdermixFF100 = new FoerdermixModel();
        foerdermixFF100.setFoerderarten(List.of(FF100));

        final var foerdermixFH50BAU50 = new FoerdermixModel();
        foerdermixFH50BAU50.setFoerderarten(List.of(FH50, BAU50));

        final var foerdermixMM75EOF25 = new FoerdermixModel();
        foerdermixMM75EOF25.setFoerderarten(List.of(MM75, EOF25));

        final var foerdermixPMB75KMB25 = new FoerdermixModel();
        foerdermixPMB75KMB25.setFoerderarten(List.of(PMB75, KMB25));

        final var jahr1 = 2024;
        final var jahr2 = 2034;

        final var baurate2024A = new BaurateModel();
        baurate2024A.setJahr(jahr1);
        baurate2024A.setWeGeplant(100);
        baurate2024A.setFoerdermix(foerdermixFF100);

        final var baurate2025A = new BaurateModel();
        baurate2025A.setJahr(jahr2);
        baurate2025A.setGfWohnenGeplant(BigDecimal.valueOf(30000));
        baurate2025A.setFoerdermix(foerdermixFH50BAU50);

        final var baurate2024B = new BaurateModel();
        baurate2024B.setJahr(jahr1);
        baurate2024B.setGfWohnenGeplant(BigDecimal.valueOf(20000));
        baurate2024B.setFoerdermix(foerdermixMM75EOF25);

        final var baurate2025B = new BaurateModel();
        baurate2025B.setJahr(jahr2);
        baurate2025B.setWeGeplant(100);
        baurate2025B.setGfWohnenGeplant(BigDecimal.valueOf(10000));
        baurate2025B.setFoerdermix(foerdermixPMB75KMB25);

        final var baugebietA = new BaugebietModel();
        baugebietA.setBauraten(List.of(baurate2024A, baurate2025A));

        final var bauabschnittA = new BauabschnittModel();
        bauabschnittA.setBaugebiete(List.of(baugebietA));

        final var baugebietB = new BaugebietModel();
        baugebietB.setBauraten(List.of(baurate2024B, baurate2025B));

        final var bauabschnittB = new BauabschnittModel();
        bauabschnittB.setBaugebiete(List.of(baugebietB));

        final var bauabschnitte = List.of(bauabschnittA, bauabschnittB);

        final var jahr1String = String.valueOf(jahr1);
        final var jahr2String = String.valueOf(jahr2);

        final var expected = List.of(
            new WohneinheitenProFoerderartProJahrModel(FF, jahr1String, new BigDecimal("100.00")),
            new WohneinheitenProFoerderartProJahrModel(FF, jahr2String, new BigDecimal("25.0000")),
            new WohneinheitenProFoerderartProJahrModel(EOF, jahr1String, new BigDecimal("55.555555555555556")),
            new WohneinheitenProFoerderartProJahrModel(EOF, jahr2String, new BigDecimal("75.0000")),
            new WohneinheitenProFoerderartProJahrModel(MM, jahr1String, new BigDecimal("150.000000000000000")),
            new WohneinheitenProFoerderartProJahrModel(MM, jahr2String, new BigDecimal("75.000000000000000")),
            new WohneinheitenProFoerderartProJahrModel(FH, jahr2String, new BigDecimal("140.625000000000000"))
        );

        final var actual = planungsursaechlicheWohneinheitenService.calculatePlanungsursaechlicheWohneinheiten(
            bauabschnitte,
            SobonOrientierungswertJahr.JAHR_2022,
            LocalDate.EPOCH
        );
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void calculateWohneinheitenTest() {
        fillStaedtebaulicheOrientierungswertRepository(staedtebaulicheOrientierungswertRepository);

        final var baurate = new BaurateModel();
        final var foerderart = new FoerderartModel();
        foerderart.setBezeichnung(MM);
        foerderart.setAnteilProzent(new BigDecimal("50"));
        final var sobonJahr = SobonOrientierungswertJahr.JAHR_2022;

        final var result1 = planungsursaechlicheWohneinheitenService.calculateWohneinheiten(
            baurate,
            foerderart,
            sobonJahr
        );
        assertThat(result1, is(BigDecimal.ZERO));

        baurate.setGfWohnenGeplant(new BigDecimal("20000"));
        final var result2 = planungsursaechlicheWohneinheitenService.calculateWohneinheiten(
            baurate,
            foerderart,
            sobonJahr
        );
        assertThat(result2, is(new BigDecimal("100.000000000000000")));

        baurate.setWeGeplant(100);
        final var result3 = planungsursaechlicheWohneinheitenService.calculateWohneinheiten(
            baurate,
            foerderart,
            sobonJahr
        );
        assertThat(result3, is(new BigDecimal("50.00")));
    }

    @Test
    void mergeWohneinheitenProFoerderartProJahrTest() {
        final var foerderart1 = FF;
        final var foerderart2 = EOF;
        final var jahr1 = "2024";
        final var jahr2 = "2034";
        final var wohneinheiten1 = new BigDecimal("1000");
        final var wohneinheiten2 = new BigDecimal("500");
        final var wohneinheiten3 = new BigDecimal("2000");
        final var wohneinheiten4 = new BigDecimal("3000");

        final var wohneinheitenProFoerderartProJahr1 = new WohneinheitenProFoerderartProJahrModel();
        wohneinheitenProFoerderartProJahr1.setFoerderart(foerderart1);
        wohneinheitenProFoerderartProJahr1.setJahr(jahr1);
        wohneinheitenProFoerderartProJahr1.setWohneinheiten(wohneinheiten1);
        final var wohneinheitenProFoerderartProJahrList = new ArrayList<WohneinheitenProFoerderartProJahrModel>();
        wohneinheitenProFoerderartProJahrList.add(wohneinheitenProFoerderartProJahr1);

        planungsursaechlicheWohneinheitenService.mergeWohneinheitenProFoerderartProJahr(
            wohneinheitenProFoerderartProJahrList,
            foerderart2,
            jahr1,
            wohneinheiten2
        );
        assertThat(wohneinheitenProFoerderartProJahrList.size(), is(2));
        assertThat(wohneinheitenProFoerderartProJahrList.get(0).getFoerderart(), is(foerderart1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(0).getJahr(), is(jahr1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(0).getWohneinheiten(), is(wohneinheiten1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(1).getFoerderart(), is(foerderart2));
        assertThat(wohneinheitenProFoerderartProJahrList.get(1).getJahr(), is(jahr1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(1).getWohneinheiten(), is(wohneinheiten2));

        planungsursaechlicheWohneinheitenService.mergeWohneinheitenProFoerderartProJahr(
            wohneinheitenProFoerderartProJahrList,
            foerderart1,
            jahr2,
            wohneinheiten3
        );
        assertThat(wohneinheitenProFoerderartProJahrList.size(), is(3));
        assertThat(wohneinheitenProFoerderartProJahrList.get(0).getFoerderart(), is(foerderart1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(0).getJahr(), is(jahr1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(0).getWohneinheiten(), is(wohneinheiten1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(1).getFoerderart(), is(foerderart2));
        assertThat(wohneinheitenProFoerderartProJahrList.get(1).getJahr(), is(jahr1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(1).getWohneinheiten(), is(wohneinheiten2));
        assertThat(wohneinheitenProFoerderartProJahrList.get(2).getFoerderart(), is(foerderart1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(2).getJahr(), is(jahr2));
        assertThat(wohneinheitenProFoerderartProJahrList.get(2).getWohneinheiten(), is(wohneinheiten3));

        planungsursaechlicheWohneinheitenService.mergeWohneinheitenProFoerderartProJahr(
            wohneinheitenProFoerderartProJahrList,
            foerderart2,
            jahr1,
            wohneinheiten4
        );
        assertThat(wohneinheitenProFoerderartProJahrList.size(), is(3));
        assertThat(wohneinheitenProFoerderartProJahrList.get(0).getFoerderart(), is(foerderart1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(0).getJahr(), is(jahr1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(0).getWohneinheiten(), is(wohneinheiten1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(1).getFoerderart(), is(foerderart2));
        assertThat(wohneinheitenProFoerderartProJahrList.get(1).getJahr(), is(jahr1));
        assertThat(
            wohneinheitenProFoerderartProJahrList.get(1).getWohneinheiten(),
            is(wohneinheiten2.add(wohneinheiten4))
        );
        assertThat(wohneinheitenProFoerderartProJahrList.get(2).getFoerderart(), is(foerderart1));
        assertThat(wohneinheitenProFoerderartProJahrList.get(2).getJahr(), is(jahr2));
        assertThat(wohneinheitenProFoerderartProJahrList.get(2).getWohneinheiten(), is(wohneinheiten3));
    }

    public static void fillStaedtebaulicheOrientierungswertRepository(
        StaedtebaulicheOrientierungswertRepository repository
    ) {
        final var orientierungswertFF2022 = new StaedtebaulicheOrientierungswert();
        orientierungswertFF2022.setFoerderartBezeichnung(FF);
        orientierungswertFF2022.setGueltigAb(SobonOrientierungswertJahr.JAHR_2022.getGueltigAb());
        orientierungswertFF2022.setDurchschnittlicheGrundflaeche(95L);

        final var orientierungswertEOF2022 = new StaedtebaulicheOrientierungswert();
        orientierungswertEOF2022.setFoerderartBezeichnung(EOF);
        orientierungswertEOF2022.setGueltigAb(SobonOrientierungswertJahr.JAHR_2022.getGueltigAb());
        orientierungswertEOF2022.setDurchschnittlicheGrundflaeche(90L);

        final var orientierungswertMM2022 = new StaedtebaulicheOrientierungswert();
        orientierungswertMM2022.setFoerderartBezeichnung(MM);
        orientierungswertMM2022.setGueltigAb(SobonOrientierungswertJahr.JAHR_2022.getGueltigAb());
        orientierungswertMM2022.setDurchschnittlicheGrundflaeche(100L);

        final var orientierungswertFH2022 = new StaedtebaulicheOrientierungswert();
        orientierungswertFH2022.setFoerderartBezeichnung(FH);
        orientierungswertFH2022.setGueltigAb(SobonOrientierungswertJahr.JAHR_2022.getGueltigAb());
        orientierungswertFH2022.setDurchschnittlicheGrundflaeche(160L);

        Mockito
            .when(
                repository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    FF,
                    SobonOrientierungswertJahr.JAHR_2022.getGueltigAb()
                )
            )
            .thenReturn(Optional.of(orientierungswertFF2022));
        Mockito
            .when(
                repository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    EOF,
                    SobonOrientierungswertJahr.JAHR_2022.getGueltigAb()
                )
            )
            .thenReturn(Optional.of(orientierungswertEOF2022));
        Mockito
            .when(
                repository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    MM,
                    SobonOrientierungswertJahr.JAHR_2022.getGueltigAb()
                )
            )
            .thenReturn(Optional.of(orientierungswertMM2022));
        Mockito
            .when(
                repository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    FH,
                    SobonOrientierungswertJahr.JAHR_2022.getGueltigAb()
                )
            )
            .thenReturn(Optional.of(orientierungswertFH2022));
    }
}
