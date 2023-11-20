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

import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenBedarfModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtebaulicheOrientierungswert;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.UmlegungFoerderartenRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class PlanungsursaechlicheBedarfServiceTest {

    private PlanungsursaechlicheBedarfService planungsursaechlicheBedarfService;

    @Mock
    private UmlegungFoerderartenRepository umlegungFoerderartenRepository;

    @Mock
    private StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    @BeforeEach
    public void beforeEach() {
        final var foerdermixUmlageService = new FoerdermixUmlageService(umlegungFoerderartenRepository);
        this.planungsursaechlicheBedarfService =
            new PlanungsursaechlicheBedarfService(foerdermixUmlageService, staedtebaulicheOrientierungswertRepository);
        Mockito.reset(umlegungFoerderartenRepository, staedtebaulicheOrientierungswertRepository);
    }

    @Test
    void calculatePlanungsursaechlicherBedarf() {
        // Input

        final var FF100 = new FoerderartModel();
        FF100.setBezeichnung(FF);
        FF100.setAnteilProzent(BigDecimal.ONE);

        final var MM75 = new FoerderartModel();
        MM75.setBezeichnung(MM);
        MM75.setAnteilProzent(new BigDecimal("0.75"));

        final var EOF25 = new FoerderartModel();
        EOF25.setBezeichnung(EOF);
        EOF25.setAnteilProzent(new BigDecimal("0.25"));

        final var FH50 = new FoerderartModel();
        FH50.setBezeichnung(FH);
        FH50.setAnteilProzent(new BigDecimal("0.5"));

        final var PMB75 = new FoerderartModel();
        PMB75.setBezeichnung(PMB);
        PMB75.setAnteilProzent(new BigDecimal("0.75"));

        final var KMB25 = new FoerderartModel();
        KMB25.setBezeichnung(KMB);
        KMB25.setAnteilProzent(new BigDecimal("0.25"));

        final var BAU50 = new FoerderartModel();
        BAU50.setBezeichnung(BAU);
        BAU50.setAnteilProzent(new BigDecimal("0.5"));

        final var foerdermixFF100 = new FoerdermixModel();
        foerdermixFF100.setFoerderarten(List.of(FF100));

        final var foerdermixFH50BAU50 = new FoerdermixModel();
        foerdermixFH50BAU50.setFoerderarten(List.of(FH50, BAU50));

        final var foerdermixMM75EOF25 = new FoerdermixModel();
        foerdermixMM75EOF25.setFoerderarten(List.of(MM75, EOF25));

        final var foerdermixPMB75KMB25 = new FoerdermixModel();
        foerdermixPMB75KMB25.setFoerderarten(List.of(PMB75, KMB25));

        final var baurate2024A = new BaurateModel();
        baurate2024A.setJahr(2024);
        baurate2024A.setWeGeplant(100);
        baurate2024A.setFoerdermix(foerdermixFF100);

        final var baurate2025A = new BaurateModel();
        baurate2025A.setJahr(2025);
        baurate2025A.setGfWohnenGeplant(BigDecimal.valueOf(30000));
        baurate2025A.setFoerdermix(foerdermixFH50BAU50);

        final var baurate2024B = new BaurateModel();
        baurate2024B.setJahr(2024);
        baurate2024B.setGfWohnenGeplant(BigDecimal.valueOf(20000));
        baurate2024B.setFoerdermix(foerdermixMM75EOF25);

        final var baurate2025B = new BaurateModel();
        baurate2025B.setJahr(2025);
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

        // Repo Mocking

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
                staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    FF,
                    SobonOrientierungswertJahr.JAHR_2022.getGueltigAb()
                )
            )
            .thenReturn(Optional.of(orientierungswertFF2022));
        Mockito
            .when(
                staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    EOF,
                    SobonOrientierungswertJahr.JAHR_2022.getGueltigAb()
                )
            )
            .thenReturn(Optional.of(orientierungswertEOF2022));
        Mockito
            .when(
                staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    MM,
                    SobonOrientierungswertJahr.JAHR_2022.getGueltigAb()
                )
            )
            .thenReturn(Optional.of(orientierungswertMM2022));
        Mockito
            .when(
                staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    FH,
                    SobonOrientierungswertJahr.JAHR_2022.getGueltigAb()
                )
            )
            .thenReturn(Optional.of(orientierungswertFH2022));

        FoerdermixUmlageServiceTest.fillUmlegungFoerderartenRepository(umlegungFoerderartenRepository);

        // Erwarteter Output

        final var expected = new PlanungsursaechlicherBedarfModel();
        expected.setWohneinheitenBedarfe(
            List.of(
                new WohneinheitenBedarfModel(FF, 2024, new BigDecimal("100")),
                new WohneinheitenBedarfModel(FF, 2025, new BigDecimal("25.0000")),
                new WohneinheitenBedarfModel(EOF, 2024, new BigDecimal("55.5555555556")),
                new WohneinheitenBedarfModel(EOF, 2025, new BigDecimal("75.0000")),
                new WohneinheitenBedarfModel(MM, 2024, new BigDecimal("150.0000000000")),
                new WohneinheitenBedarfModel(MM, 2025, new BigDecimal("75.0000000000")),
                new WohneinheitenBedarfModel(FH, 2025, new BigDecimal("140.6250000000")),
                new WohneinheitenBedarfModel(
                    PlanungsursaechlicheBedarfService.SUMMARY_NAME,
                    2024,
                    new BigDecimal("305.5555555556")
                ),
                new WohneinheitenBedarfModel(
                    PlanungsursaechlicheBedarfService.SUMMARY_NAME,
                    2025,
                    new BigDecimal("315.6250000000")
                )
            )
        );

        final var actual = planungsursaechlicheBedarfService.calculatePlanungsursaechlicherBedarf(
            bauabschnitte,
            SobonOrientierungswertJahr.JAHR_2022,
            LocalDate.EPOCH
        );
        assertThat(actual.getWohneinheitenBedarfe(), containsInAnyOrder(expected.getWohneinheitenBedarfe().toArray()));
    }
}