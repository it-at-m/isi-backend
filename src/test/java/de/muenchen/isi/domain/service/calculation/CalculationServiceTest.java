package de.muenchen.isi.domain.service.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.model.AbfragevarianteBaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteWeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.calculation.BedarfeForAbfragevarianteModel;
import de.muenchen.isi.domain.model.calculation.InfrastrukturbedarfProJahrModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerBedarfModel;
import de.muenchen.isi.domain.model.calculation.PersonenProJahrModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
class CalculationServiceTest {

    @Mock
    private PlanungsursaechlicheWohneinheitenService planungsursaechlicheWohneinheitenService;

    @Mock
    private InfrastrukturbedarfService infrastrukturbedarfService;

    private CalculationService calculationService;

    @BeforeEach
    public void beforeEach() {
        this.calculationService =
            Mockito.spy(new CalculationService(planungsursaechlicheWohneinheitenService, infrastrukturbedarfService));
        Mockito.reset(planungsursaechlicheWohneinheitenService, infrastrukturbedarfService, calculationService);
    }

    @Test
    void calculateBedarfeForEachAbfragevarianteOfAbfrageUnspecified() {
        final var abfrage = new WeiteresVerfahrenModel();
        abfrage.setArtAbfrage(ArtAbfrage.UNSPECIFIED);
        Assertions.assertThrows(
            CalculationException.class,
            () -> this.calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage)
        );
    }

    @Test
    void calculateBedarfeForEachAbfragevarianteOfAbfrageWeiteresVerfahren() throws CalculationException {
        final var abfrage = new WeiteresVerfahrenModel();
        abfrage.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        abfrage.setAbfragevariantenWeiteresVerfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(null);
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateBedarfeForAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenWeiteresVerfahren(List.of());
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateBedarfeForAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenWeiteresVerfahren(null);
        var abfragevariante = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante));
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateBedarfeForAbfragevariante(abfragevariante);

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenWeiteresVerfahren(null);
        abfragevariante = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante));
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateBedarfeForAbfragevariante(abfragevariante);

        Mockito.reset(calculationService);
        abfragevariante = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfrage.setAbfragevariantenWeiteresVerfahren(List.of(abfragevariante));
        abfragevariante = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of(abfragevariante));
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(2)).calculateBedarfeForAbfragevariante(abfragevariante);
    }

    @Test
    void calculateBedarfeForEachAbfragevarianteOfAbfrageBaugenehmigungsverfahren() throws CalculationException {
        final var abfrage = new BaugenehmigungsverfahrenModel();
        abfrage.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(null);
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateBedarfeForAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateBedarfeForAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        var abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of(abfragevariante));
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateBedarfeForAbfragevariante(abfragevariante);

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(null);
        abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of(abfragevariante));
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateBedarfeForAbfragevariante(abfragevariante);

        Mockito.reset(calculationService);
        abfragevariante = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(List.of(abfragevariante));
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of(abfragevariante));
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(2)).calculateBedarfeForAbfragevariante(abfragevariante);
    }

    @Test
    void calculateBedarfeForEachAbfragevarianteOfAbfrageBauleitplanverfahren() throws CalculationException {
        final var abfrage = new BauleitplanverfahrenModel();
        abfrage.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfrage.setAbfragevariantenBauleitplanverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(null);
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateBedarfeForAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBauleitplanverfahren(List.of());
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateBedarfeForAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBauleitplanverfahren(null);
        var abfragevariante = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante));
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateBedarfeForAbfragevariante(abfragevariante);

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBauleitplanverfahren(null);
        abfragevariante = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante));
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateBedarfeForAbfragevariante(abfragevariante);

        Mockito.reset(calculationService);
        abfragevariante = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfrage.setAbfragevariantenBauleitplanverfahren(List.of(abfragevariante));
        abfragevariante = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevariante.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of(abfragevariante));
        calculationService.calculateBedarfeForEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(2)).calculateBedarfeForAbfragevariante(abfragevariante);
    }

    @Test
    void calculateBedarfeForAbfragevarianteUnspecified() {
        final var abfragevarianteBauleitplanverfahren = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteBauleitplanverfahren.setArtAbfragevariante(ArtAbfrage.UNSPECIFIED);
        Assertions.assertThrows(
            CalculationException.class,
            () -> this.calculationService.calculateBedarfeForAbfragevariante(abfragevarianteBauleitplanverfahren)
        );
    }

    @Test
    void calculateBedarfeForAbfragevarianteWeiteresVerfahren() throws CalculationException {
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);

        final var abfragevarianteBauleitplanverfahren = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteBauleitplanverfahren.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevarianteBauleitplanverfahren.setBauabschnitte(bauabschnitte);
        abfragevarianteBauleitplanverfahren.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        abfragevarianteBauleitplanverfahren.setStammdatenGueltigAb(stammdatenGueltigAb);

        final var langfristigerPlanungsursaechlicherBedarf = new LangfristigerBedarfModel();
        langfristigerPlanungsursaechlicherBedarf.setWohneinheiten(
            List.of(new WohneinheitenProFoerderartProJahrModel())
        );

        Mockito
            .doReturn(langfristigerPlanungsursaechlicherBedarf)
            .when(calculationService)
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );

        final var bedarfeForAbfragevariante = calculationService.calculateBedarfeForAbfragevariante(
            abfragevarianteBauleitplanverfahren
        );

        final var expected = new BedarfeForAbfragevarianteModel();
        expected.setLangfristigerPlanungsursaechlicherBedarf(langfristigerPlanungsursaechlicherBedarf);

        assertThat(bedarfeForAbfragevariante, is(expected));

        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );
    }

    @Test
    void calculateBedarfeForAbfragevarianteBaugenehmigungsverfahren() throws CalculationException {
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);

        final var abfragevarianteBauleitplanverfahren = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevarianteBauleitplanverfahren.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevarianteBauleitplanverfahren.setBauabschnitte(bauabschnitte);
        abfragevarianteBauleitplanverfahren.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        abfragevarianteBauleitplanverfahren.setStammdatenGueltigAb(stammdatenGueltigAb);

        final var langfristigerPlanungsursaechlicherBedarf = new LangfristigerBedarfModel();
        langfristigerPlanungsursaechlicherBedarf.setWohneinheiten(
            List.of(new WohneinheitenProFoerderartProJahrModel())
        );

        Mockito
            .doReturn(langfristigerPlanungsursaechlicherBedarf)
            .when(calculationService)
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );

        final var bedarfeForAbfragevariante = calculationService.calculateBedarfeForAbfragevariante(
            abfragevarianteBauleitplanverfahren
        );

        final var expected = new BedarfeForAbfragevarianteModel();
        expected.setLangfristigerPlanungsursaechlicherBedarf(langfristigerPlanungsursaechlicherBedarf);

        assertThat(bedarfeForAbfragevariante, is(expected));

        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );
    }

    @Test
    void calculateBedarfeForAbfragevarianteBauleitplanverfahren() throws CalculationException {
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);

        final var abfragevarianteBauleitplanverfahren = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteBauleitplanverfahren.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteBauleitplanverfahren.setBauabschnitte(bauabschnitte);
        abfragevarianteBauleitplanverfahren.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        abfragevarianteBauleitplanverfahren.setStammdatenGueltigAb(stammdatenGueltigAb);

        final var langfristigerPlanungsursaechlicherBedarf = new LangfristigerBedarfModel();
        langfristigerPlanungsursaechlicherBedarf.setWohneinheiten(
            List.of(new WohneinheitenProFoerderartProJahrModel())
        );

        Mockito
            .doReturn(langfristigerPlanungsursaechlicherBedarf)
            .when(calculationService)
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );

        final var bedarfeForAbfragevariante = calculationService.calculateBedarfeForAbfragevariante(
            abfragevarianteBauleitplanverfahren
        );

        final var expected = new BedarfeForAbfragevarianteModel();
        expected.setLangfristigerPlanungsursaechlicherBedarf(langfristigerPlanungsursaechlicherBedarf);

        assertThat(bedarfeForAbfragevariante, is(expected));

        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );
    }

    @Test
    void calculateLangfristigerPlanungsursaechlicherBedarfReturnNull() throws CalculationException {
        var result = calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(null, null, null);
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                null,
                SobonOrientierungswertJahr.JAHR_2017,
                LocalDate.now()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                new ArrayList<>(),
                SobonOrientierungswertJahr.JAHR_2017,
                LocalDate.now()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                List.of(new BauabschnittModel()),
                null,
                null
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                List.of(new BauabschnittModel()),
                SobonOrientierungswertJahr.JAHR_2017,
                null
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                List.of(new BauabschnittModel()),
                null,
                LocalDate.now()
            );
        assertThat(result, is(nullValue()));

        result =
            calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
                List.of(new BauabschnittModel()),
                SobonOrientierungswertJahr.STANDORTABFRAGE,
                LocalDate.now()
            );
        assertThat(result, is(nullValue()));
    }

    @Test
    void calculateLangfristigerPlanungsursaechlicherBedarf() throws CalculationException {
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);

        final var wohneinheiten = List.of(new WohneinheitenProFoerderartProJahrModel());

        Mockito
            .when(
                this.planungsursaechlicheWohneinheitenService.calculatePlanungsursaechlicheWohneinheiten(
                        bauabschnitte,
                        sobonOrientierungswertJahr,
                        stammdatenGueltigAb
                    )
            )
            .thenReturn(wohneinheiten);

        final var sumWohneinheiten10Years = List.of(
            new WohneinheitenProFoerderartProJahrModel(),
            new WohneinheitenProFoerderartProJahrModel()
        );
        Mockito
            .when(
                this.planungsursaechlicheWohneinheitenService.sumWohneinheitenForNumberOfYearsForEachFoerderart(
                        wohneinheiten,
                        10
                    )
            )
            .thenReturn(sumWohneinheiten10Years);

        final var sumWohneinheiten15Years = List.of(
            new WohneinheitenProFoerderartProJahrModel(),
            new WohneinheitenProFoerderartProJahrModel(),
            new WohneinheitenProFoerderartProJahrModel()
        );
        Mockito
            .when(
                this.planungsursaechlicheWohneinheitenService.sumWohneinheitenForNumberOfYearsForEachFoerderart(
                        wohneinheiten,
                        15
                    )
            )
            .thenReturn(sumWohneinheiten15Years);

        final var sumWohneinheiten20Years = List.of(
            new WohneinheitenProFoerderartProJahrModel(),
            new WohneinheitenProFoerderartProJahrModel(),
            new WohneinheitenProFoerderartProJahrModel(),
            new WohneinheitenProFoerderartProJahrModel()
        );
        Mockito
            .when(
                this.planungsursaechlicheWohneinheitenService.sumWohneinheitenForNumberOfYearsForEachFoerderart(
                        wohneinheiten,
                        20
                    )
            )
            .thenReturn(sumWohneinheiten20Years);

        final var sumWohneinheitenOverFoerderartenEachYear = List.of(
            new WohneinheitenProFoerderartProJahrModel(),
            new WohneinheitenProFoerderartProJahrModel(),
            new WohneinheitenProFoerderartProJahrModel(),
            new WohneinheitenProFoerderartProJahrModel(),
            new WohneinheitenProFoerderartProJahrModel()
        );
        Mockito
            .when(
                this.planungsursaechlicheWohneinheitenService.sumWohneinheitenOverFoerderartenForEachYear(
                        Stream
                            .of(
                                wohneinheiten,
                                sumWohneinheiten10Years,
                                sumWohneinheiten15Years,
                                sumWohneinheiten20Years
                            )
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList())
                    )
            )
            .thenReturn(sumWohneinheitenOverFoerderartenEachYear);

        final var bedarfeProJahrKinderkrippe = List.of(new InfrastrukturbedarfProJahrModel());
        final var bedarfeProJahrMeanKinderkrippe10 = new InfrastrukturbedarfProJahrModel();
        bedarfeProJahrMeanKinderkrippe10.setAnzahlPersonenGesamt(BigDecimal.valueOf(10));
        final var bedarfeProJahrMeanKinderkrippe15 = new InfrastrukturbedarfProJahrModel();
        bedarfeProJahrMeanKinderkrippe15.setAnzahlPersonenGesamt(BigDecimal.valueOf(15));
        final var bedarfeProJahrMeanKinderkrippe20 = new InfrastrukturbedarfProJahrModel();
        bedarfeProJahrMeanKinderkrippe20.setAnzahlPersonenGesamt(BigDecimal.valueOf(20));

        Mockito
            .when(
                this.infrastrukturbedarfService.calculateBedarfForKinderkrippeRounded(
                        wohneinheiten,
                        sobonOrientierungswertJahr,
                        InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
                        stammdatenGueltigAb
                    )
            )
            .thenReturn(bedarfeProJahrKinderkrippe);

        Mockito
            .when(this.infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(bedarfeProJahrKinderkrippe, 10))
            .thenReturn(bedarfeProJahrMeanKinderkrippe10);
        Mockito
            .when(this.infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(bedarfeProJahrKinderkrippe, 15))
            .thenReturn(bedarfeProJahrMeanKinderkrippe15);
        Mockito
            .when(this.infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(bedarfeProJahrKinderkrippe, 20))
            .thenReturn(bedarfeProJahrMeanKinderkrippe20);

        final var bedarfeProJahrKindergarten = List.of(
            new InfrastrukturbedarfProJahrModel(),
            new InfrastrukturbedarfProJahrModel()
        );
        final var bedarfeProJahrMeanKindergarten10 = new InfrastrukturbedarfProJahrModel();
        bedarfeProJahrMeanKindergarten10.setAnzahlPersonenGesamt(BigDecimal.valueOf(110));
        final var bedarfeProJahrMeanKindergarten15 = new InfrastrukturbedarfProJahrModel();
        bedarfeProJahrMeanKindergarten15.setAnzahlPersonenGesamt(BigDecimal.valueOf(115));
        final var bedarfeProJahrMeanKindergarten20 = new InfrastrukturbedarfProJahrModel();
        bedarfeProJahrMeanKindergarten20.setAnzahlPersonenGesamt(BigDecimal.valueOf(120));

        Mockito
            .when(
                this.infrastrukturbedarfService.calculateBedarfForKindergartenRounded(
                        wohneinheiten,
                        sobonOrientierungswertJahr,
                        InfrastrukturbedarfService.ArtInfrastrukturbedarf.PLANUNGSURSAECHLICH,
                        stammdatenGueltigAb
                    )
            )
            .thenReturn(bedarfeProJahrKindergarten);

        Mockito
            .when(this.infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(bedarfeProJahrKindergarten, 10))
            .thenReturn(bedarfeProJahrMeanKindergarten10);
        Mockito
            .when(this.infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(bedarfeProJahrKindergarten, 15))
            .thenReturn(bedarfeProJahrMeanKindergarten15);
        Mockito
            .when(this.infrastrukturbedarfService.calculateMeanInfrastrukturbedarfe(bedarfeProJahrKindergarten, 20))
            .thenReturn(bedarfeProJahrMeanKindergarten20);

        final var alleEinwohnerProJahr = List.of(new PersonenProJahrModel(), new PersonenProJahrModel());
        final var alleEinwohnerMean10 = new PersonenProJahrModel();
        alleEinwohnerMean10.setAnzahlPersonenGesamt(BigDecimal.valueOf(210));
        final var alleEinwohnerMean15 = new PersonenProJahrModel();
        alleEinwohnerMean15.setAnzahlPersonenGesamt(BigDecimal.valueOf(215));
        final var alleEinwohnerMean20 = new PersonenProJahrModel();
        alleEinwohnerMean20.setAnzahlPersonenGesamt(BigDecimal.valueOf(220));

        Mockito
            .when(
                this.infrastrukturbedarfService.calculateAlleEinwohnerRounded(wohneinheiten, sobonOrientierungswertJahr)
            )
            .thenReturn(alleEinwohnerProJahr);

        Mockito
            .when(this.infrastrukturbedarfService.calculateMeanPersonen(alleEinwohnerProJahr, 10))
            .thenReturn(alleEinwohnerMean10);
        Mockito
            .when(this.infrastrukturbedarfService.calculateMeanPersonen(alleEinwohnerProJahr, 15))
            .thenReturn(alleEinwohnerMean15);
        Mockito
            .when(this.infrastrukturbedarfService.calculateMeanPersonen(alleEinwohnerProJahr, 20))
            .thenReturn(alleEinwohnerMean20);

        final var result = calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
            bauabschnitte,
            sobonOrientierungswertJahr,
            stammdatenGueltigAb
        );

        final var expected = new LangfristigerBedarfModel();

        expected.setWohneinheiten(wohneinheiten);
        expected.setWohneinheitenSumme10Jahre(sumWohneinheiten10Years);
        expected.setWohneinheitenSumme15Jahre(sumWohneinheiten15Years);
        expected.setWohneinheitenSumme20Jahre(sumWohneinheiten20Years);
        expected.setWohneinheitenGesamt(sumWohneinheitenOverFoerderartenEachYear);
        expected.setBedarfKinderkrippe(bedarfeProJahrKinderkrippe);
        expected.setBedarfKinderkrippeMittelwert10(bedarfeProJahrMeanKinderkrippe10);
        expected.setBedarfKinderkrippeMittelwert15(bedarfeProJahrMeanKinderkrippe15);
        expected.setBedarfKinderkrippeMittelwert20(bedarfeProJahrMeanKinderkrippe20);
        expected.setBedarfKindergarten(bedarfeProJahrKindergarten);
        expected.setBedarfKindergartenMittelwert10(bedarfeProJahrMeanKindergarten10);
        expected.setBedarfKindergartenMittelwert15(bedarfeProJahrMeanKindergarten15);
        expected.setBedarfKindergartenMittelwert20(bedarfeProJahrMeanKindergarten20);
        expected.setAlleEinwohner(alleEinwohnerProJahr);
        expected.setAlleEinwohnerMittelwert10(alleEinwohnerMean10);
        expected.setAlleEinwohnerMittelwert15(alleEinwohnerMean15);
        expected.setAlleEinwohnerMittelwert20(alleEinwohnerMean20);

        assertThat(result, is(expected));
    }
}
