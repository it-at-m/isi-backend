package de.muenchen.isi.domain.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProJahrModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CalculationServiceTest {

    private CalculationService calculationService;

    @Mock
    private StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    @BeforeEach
    public void beforeEach() {
        this.calculationService = new CalculationService(staedtebaulicheOrientierungswertRepository);
    }

    @Test
    void calculatePlanungsursaechlicherBedarf() throws CalculationException {
        // Input

        var FF100 = new FoerderartModel();
        FF100.setBezeichnung("freifinanzierter Geschosswohnungsbau");
        FF100.setAnteilProzent(BigDecimal.ONE);

        var MM75 = new FoerderartModel();
        MM75.setBezeichnung("MünchenModell");
        MM75.setAnteilProzent(new BigDecimal("0.75"));

        var EOF25 = new FoerderartModel();
        EOF25.setBezeichnung("geförderter Mietwohnungsbau");
        EOF25.setAnteilProzent(new BigDecimal("0.25"));

        var FH50 = new FoerderartModel();
        FH50.setBezeichnung("Ein-/Zweifamilienhäuser");
        FH50.setAnteilProzent(new BigDecimal("0.5"));

        var PMB75 = new FoerderartModel();
        PMB75.setBezeichnung("preis-gedämpfter Mietwohnungsbau");
        PMB75.setAnteilProzent(new BigDecimal("0.75"));

        var KMB25 = new FoerderartModel();
        KMB25.setBezeichnung("konzeptioneller Mietwohnungsbau");
        KMB25.setAnteilProzent(new BigDecimal("0.25"));

        var BAU50 = new FoerderartModel();
        BAU50.setBezeichnung("Baugemeinschaften");
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

        var abfragevariante = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevariante.setBauabschnitte(List.of(bauabschnittA, bauabschnittB));
        abfragevariante.setSobonOrientierungswertJahr(SobonOrientierungswertJahr.JAHR_2022);

        // Erwarteter Output

        Map<String, Map<Integer, BigDecimal>> expected = Map.of(
            "freifinanzierter Geschosswohnungsbau",
            Map.of(2024, new BigDecimal("100"), 2025, new BigDecimal("25.0000")),
            "MünchenModell",
            Map.of(2024, new BigDecimal("166.6666666667"), 2025, new BigDecimal("83.3333333333")),
            "geförderter Mietwohnungsbau",
            Map.of(2024, new BigDecimal("55.5555555556"), 2025, new BigDecimal("75.0000")),
            "Ein-/Zweifamilienhäuser",
            Map.of(2025, new BigDecimal("250.0000000000")),
            CalculationService.SUMMARY_NAME,
            Map.of(2024, new BigDecimal("322.2222222223"), 2025, new BigDecimal("433.3333333333"))
        );

        PlanungsursaechlicherBedarfModel actual = calculationService.calculatePlanungsursaechlicherBedarf(
            abfragevariante,
            LocalDate.of(2024, 1, 1)
        );
        assertThat(planungsursaechlicherBedarfToMap(actual), is(expected));
    }

    private Map<String, Map<Integer, BigDecimal>> planungsursaechlicherBedarfToMap(
        PlanungsursaechlicherBedarfModel bedarf
    ) {
        return bedarf
            .getWohneinheitenProFoerderart()
            .stream()
            .collect(
                Collectors.toMap(
                    WohneinheitenProFoerderartModel::getFoerderart,
                    wohneinheitenProFoerderart ->
                        wohneinheitenProFoerderart
                            .getWohneinheitenProJahr()
                            .stream()
                            .collect(
                                Collectors.toMap(
                                    WohneinheitenProJahrModel::getJahr,
                                    WohneinheitenProJahrModel::getWohneinheiten
                                )
                            )
                )
            );
    }
}
