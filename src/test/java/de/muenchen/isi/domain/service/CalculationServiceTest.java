package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenBedarfModel;
import de.muenchen.isi.infrastructure.entity.Foerderart;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.entity.stammdaten.StaedtebaulicheOrientierungswert;
import de.muenchen.isi.infrastructure.entity.stammdaten.UmlegungFoerderarten;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.UmlegungFoerderartenRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
public class CalculationServiceTest {

    private final String FF = "freifinanzierter Geschosswohnungsbau";
    private final String EOF = "geförderter Mietwohnungsbau";
    private final String MM = "MünchenModell";
    private final String FH = "Ein-/Zweifamilienhäuser";
    private final String PMB = "preis-gedämpfter Mietwohnungsbau";
    private final String KMB = "konzeptioneller Mietwohnungsbau";
    private final String BAU = "Baugemeinschaften";

    private final LocalDate SOBON_2022 = LocalDate.of(2022, 1, 1);
    private final LocalDate GUELTIG_AB = LocalDate.of(2023, 11, 20);

    private CalculationService calculationService;

    @Mock
    private UmlegungFoerderartenRepository umlegungFoerderartenRepository;

    @Mock
    private StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    @BeforeEach
    public void beforeEach() {
        this.calculationService =
            new CalculationService(umlegungFoerderartenRepository, staedtebaulicheOrientierungswertRepository);
    }

    @Test
    void calculatePlanungsursaechlicherBedarf() {
        // Input

        var FF100 = new FoerderartModel();
        FF100.setBezeichnung(FF);
        FF100.setAnteilProzent(BigDecimal.ONE);

        var MM75 = new FoerderartModel();
        MM75.setBezeichnung(MM);
        MM75.setAnteilProzent(new BigDecimal("0.75"));

        var EOF25 = new FoerderartModel();
        EOF25.setBezeichnung(EOF);
        EOF25.setAnteilProzent(new BigDecimal("0.25"));

        var FH50 = new FoerderartModel();
        FH50.setBezeichnung(FH);
        FH50.setAnteilProzent(new BigDecimal("0.5"));

        var PMB75 = new FoerderartModel();
        PMB75.setBezeichnung(PMB);
        PMB75.setAnteilProzent(new BigDecimal("0.75"));

        var KMB25 = new FoerderartModel();
        KMB25.setBezeichnung(KMB);
        KMB25.setAnteilProzent(new BigDecimal("0.25"));

        var BAU50 = new FoerderartModel();
        BAU50.setBezeichnung(BAU);
        BAU50.setAnteilProzent(new BigDecimal("0.5"));

        var foerdermixFF100 = new FoerdermixModel();
        foerdermixFF100.setFoerderarten(List.of(FF100));

        var foerdermixFH50BAU50 = new FoerdermixModel();
        foerdermixFH50BAU50.setFoerderarten(List.of(FH50, BAU50));

        var foerdermixMM75EOF25 = new FoerdermixModel();
        foerdermixMM75EOF25.setFoerderarten(List.of(MM75, EOF25));

        var foerdermixPMB75KMB25 = new FoerdermixModel();
        foerdermixPMB75KMB25.setFoerderarten(List.of(PMB75, KMB25));

        var baurate2024A = new BaurateModel();
        baurate2024A.setJahr(2024);
        baurate2024A.setWeGeplant(100);
        baurate2024A.setFoerdermix(foerdermixFF100);

        var baurate2025A = new BaurateModel();
        baurate2025A.setJahr(2025);
        baurate2025A.setGfWohnenGeplant(BigDecimal.valueOf(30000));
        baurate2025A.setFoerdermix(foerdermixFH50BAU50);

        var baurate2024B = new BaurateModel();
        baurate2024B.setJahr(2024);
        baurate2024B.setGfWohnenGeplant(BigDecimal.valueOf(20000));
        baurate2024B.setFoerdermix(foerdermixMM75EOF25);

        var baurate2025B = new BaurateModel();
        baurate2025B.setJahr(2025);
        baurate2025B.setWeGeplant(100);
        baurate2025B.setGfWohnenGeplant(BigDecimal.valueOf(10000));
        baurate2025B.setFoerdermix(foerdermixPMB75KMB25);

        var baugebietA = new BaugebietModel();
        baugebietA.setBauraten(List.of(baurate2024A, baurate2025A));

        var bauabschnittA = new BauabschnittModel();
        bauabschnittA.setBaugebiete(List.of(baugebietA));

        var baugebietB = new BaugebietModel();
        baugebietB.setBauraten(List.of(baurate2024B, baurate2025B));

        var bauabschnittB = new BauabschnittModel();
        bauabschnittB.setBaugebiete(List.of(baugebietB));

        var bauabschnitte = List.of(bauabschnittA, bauabschnittB);

        // Repo Mocking

        final var orientierungswertFF2022 = new StaedtebaulicheOrientierungswert();
        orientierungswertFF2022.setFoerderartBezeichnung(FF);
        orientierungswertFF2022.setGueltigAb(SOBON_2022);
        orientierungswertFF2022.setDurchschnittlicheGrundflaeche(95L);

        final var orientierungswertEOF2022 = new StaedtebaulicheOrientierungswert();
        orientierungswertEOF2022.setFoerderartBezeichnung(EOF);
        orientierungswertEOF2022.setGueltigAb(SOBON_2022);
        orientierungswertEOF2022.setDurchschnittlicheGrundflaeche(90L);

        final var orientierungswertMM2022 = new StaedtebaulicheOrientierungswert();
        orientierungswertMM2022.setFoerderartBezeichnung(MM);
        orientierungswertMM2022.setGueltigAb(SOBON_2022);
        orientierungswertMM2022.setDurchschnittlicheGrundflaeche(100L);

        final var orientierungswertFH2022 = new StaedtebaulicheOrientierungswert();
        orientierungswertFH2022.setFoerderartBezeichnung(FH);
        orientierungswertFH2022.setGueltigAb(SOBON_2022);
        orientierungswertFH2022.setDurchschnittlicheGrundflaeche(160L);

        Mockito
            .when(
                staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    FF,
                    SOBON_2022
                )
            )
            .thenReturn(Optional.of(orientierungswertFF2022));
        Mockito
            .when(
                staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    EOF,
                    SOBON_2022
                )
            )
            .thenReturn(Optional.of(orientierungswertEOF2022));
        Mockito
            .when(
                staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    MM,
                    SOBON_2022
                )
            )
            .thenReturn(Optional.of(orientierungswertMM2022));
        Mockito
            .when(
                staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    FH,
                    SOBON_2022
                )
            )
            .thenReturn(Optional.of(orientierungswertFH2022));

        final var umlegungFF25 = new Foerderart();
        umlegungFF25.setBezeichnung(FF);
        umlegungFF25.setAnteilProzent(new BigDecimal("0.25"));

        final var umlegungEOF75 = new Foerderart();
        umlegungEOF75.setBezeichnung(EOF);
        umlegungEOF75.setAnteilProzent(new BigDecimal("0.75"));

        final var umlegungFH50 = new Foerderart();
        umlegungFH50.setBezeichnung(FH);
        umlegungFH50.setAnteilProzent(new BigDecimal("0.50"));

        final var umlegungMM50 = new Foerderart();
        umlegungMM50.setBezeichnung(MM);
        umlegungMM50.setAnteilProzent(new BigDecimal("0.50"));

        final var umlegungPMB = new UmlegungFoerderarten();
        umlegungPMB.setBezeichnung(PMB);
        umlegungPMB.setGueltigAb(GUELTIG_AB);
        umlegungPMB.setUmlegungsschluessel(Set.of(umlegungFF25, umlegungEOF75));

        final var umlegungKMB = new UmlegungFoerderarten();
        umlegungKMB.setBezeichnung(KMB);
        umlegungKMB.setGueltigAb(GUELTIG_AB);
        umlegungKMB.setUmlegungsschluessel(Set.of(umlegungFF25, umlegungEOF75));

        final var umlegungBAU = new UmlegungFoerderarten();
        umlegungBAU.setBezeichnung(BAU);
        umlegungBAU.setGueltigAb(GUELTIG_AB);
        umlegungBAU.setUmlegungsschluessel(Set.of(umlegungFH50, umlegungMM50));

        Mockito
            .when(
                umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    PMB,
                    GUELTIG_AB
                )
            )
            .thenReturn(Optional.of(umlegungPMB));
        Mockito
            .when(
                umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    KMB,
                    GUELTIG_AB
                )
            )
            .thenReturn(Optional.of(umlegungKMB));
        Mockito
            .when(
                umlegungFoerderartenRepository.findFirstByBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    BAU,
                    GUELTIG_AB
                )
            )
            .thenReturn(Optional.of(umlegungBAU));

        // Erwarteter Output

        final PlanungsursaechlicherBedarfModel expected = new PlanungsursaechlicherBedarfModel();
        expected.setWohneinheitenBedarfe(
            List.of(
                new WohneinheitenBedarfModel(FF, 2024, new BigDecimal("100")),
                new WohneinheitenBedarfModel(FF, 2025, new BigDecimal("25.0000")),
                new WohneinheitenBedarfModel(EOF, 2024, new BigDecimal("55.5555555556")),
                new WohneinheitenBedarfModel(EOF, 2025, new BigDecimal("75.0000")),
                new WohneinheitenBedarfModel(MM, 2024, new BigDecimal("150.0000000000")),
                new WohneinheitenBedarfModel(MM, 2025, new BigDecimal("75.0000000000")),
                new WohneinheitenBedarfModel(FH, 2025, new BigDecimal("140.6250000000")),
                new WohneinheitenBedarfModel(CalculationService.SUMMARY_NAME, 2024, new BigDecimal("305.5555555556")),
                new WohneinheitenBedarfModel(CalculationService.SUMMARY_NAME, 2025, new BigDecimal("315.6250000000"))
            )
        );

        final PlanungsursaechlicherBedarfModel actual = calculationService.calculatePlanungsursaechlicherBedarf(
            bauabschnitte,
            SobonOrientierungswertJahr.JAHR_2022,
            GUELTIG_AB
        );
        assertThat(actual.getWohneinheitenBedarfe(), containsInAnyOrder(expected.getWohneinheitenBedarfe().toArray()));
    }
}
