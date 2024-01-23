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
import de.muenchen.isi.domain.model.calculation.InfrastrukturbedarfProJahrModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerPlanungsursaechlicherBedarfModel;
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
    void calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrageUnspecified() {
        final var abfrage = new WeiteresVerfahrenModel();
        abfrage.setArtAbfrage(ArtAbfrage.UNSPECIFIED);
        Assertions.assertThrows(
            CalculationException.class,
            () -> this.calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage)
        );
    }

    @Test
    void calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrageWeiteresVerfahren() throws CalculationException {
        final var abfrage = new WeiteresVerfahrenModel();
        abfrage.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        abfrage.setAbfragevariantenWeiteresVerfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(null);
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenWeiteresVerfahren(List.of());
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenWeiteresVerfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(
            List.of(new AbfragevarianteWeiteresVerfahrenModel())
        );
        Mockito
            .doNothing()
            .when(calculationService)
            .calculateAndAppendBedarfeToAbfragevariante(new AbfragevarianteWeiteresVerfahrenModel());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenWeiteresVerfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(
            List.of(new AbfragevarianteWeiteresVerfahrenModel())
        );
        Mockito
            .doNothing()
            .when(calculationService)
            .calculateAndAppendBedarfeToAbfragevariante(new AbfragevarianteWeiteresVerfahrenModel());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenWeiteresVerfahren(List.of(new AbfragevarianteWeiteresVerfahrenModel()));
        abfrage.setAbfragevariantenSachbearbeitungWeiteresVerfahren(
            List.of(new AbfragevarianteWeiteresVerfahrenModel())
        );
        Mockito
            .doNothing()
            .when(calculationService)
            .calculateAndAppendBedarfeToAbfragevariante(new AbfragevarianteWeiteresVerfahrenModel());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(2)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());
    }

    @Test
    void calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrageBaugenehmigungsverfahren() throws CalculationException {
        final var abfrage = new BaugenehmigungsverfahrenModel();
        abfrage.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(null);
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(new AbfragevarianteBaugenehmigungsverfahrenModel())
        );
        Mockito
            .doNothing()
            .when(calculationService)
            .calculateAndAppendBedarfeToAbfragevariante(new AbfragevarianteBaugenehmigungsverfahrenModel());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(new AbfragevarianteBaugenehmigungsverfahrenModel())
        );
        Mockito
            .doNothing()
            .when(calculationService)
            .calculateAndAppendBedarfeToAbfragevariante(new AbfragevarianteBaugenehmigungsverfahrenModel());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBaugenehmigungsverfahren(
            List.of(new AbfragevarianteBaugenehmigungsverfahrenModel())
        );
        abfrage.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(
            List.of(new AbfragevarianteBaugenehmigungsverfahrenModel())
        );
        Mockito
            .doNothing()
            .when(calculationService)
            .calculateAndAppendBedarfeToAbfragevariante(new AbfragevarianteBaugenehmigungsverfahrenModel());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(2)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());
    }

    @Test
    void calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrageBauleitplanverfahren() throws CalculationException {
        final var abfrage = new BauleitplanverfahrenModel();
        abfrage.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfrage.setAbfragevariantenBauleitplanverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(null);
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBauleitplanverfahren(List.of());
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(0)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBauleitplanverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(new AbfragevarianteBauleitplanverfahrenModel())
        );
        Mockito
            .doNothing()
            .when(calculationService)
            .calculateAndAppendBedarfeToAbfragevariante(new AbfragevarianteBauleitplanverfahrenModel());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBauleitplanverfahren(null);
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(new AbfragevarianteBauleitplanverfahrenModel())
        );
        Mockito
            .doNothing()
            .when(calculationService)
            .calculateAndAppendBedarfeToAbfragevariante(new AbfragevarianteBauleitplanverfahrenModel());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(1)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());

        Mockito.reset(calculationService);
        abfrage.setAbfragevariantenBauleitplanverfahren(List.of(new AbfragevarianteBauleitplanverfahrenModel()));
        abfrage.setAbfragevariantenSachbearbeitungBauleitplanverfahren(
            List.of(new AbfragevarianteBauleitplanverfahrenModel())
        );
        Mockito
            .doNothing()
            .when(calculationService)
            .calculateAndAppendBedarfeToAbfragevariante(new AbfragevarianteBauleitplanverfahrenModel());
        calculationService.calculateAndAppendBedarfeToEachAbfragevarianteOfAbfrage(abfrage);
        Mockito.verify(calculationService, Mockito.times(2)).calculateAndAppendBedarfeToAbfragevariante(Mockito.any());
    }

    @Test
    void calculateAndAppendBedarfeToAbfragevarianteUnspecified() {
        final var abfragevarianteBauleitplanverfahren = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteBauleitplanverfahren.setArtAbfragevariante(ArtAbfrage.UNSPECIFIED);
        Assertions.assertThrows(
            CalculationException.class,
            () ->
                this.calculationService.calculateAndAppendBedarfeToAbfragevariante(abfragevarianteBauleitplanverfahren)
        );
    }

    @Test
    void calculateAndAppendBedarfeToAbfragevarianteWeiteresVerfahren() throws CalculationException {
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);

        final var abfragevarianteBauleitplanverfahren = new AbfragevarianteWeiteresVerfahrenModel();
        abfragevarianteBauleitplanverfahren.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        abfragevarianteBauleitplanverfahren.setBauabschnitte(bauabschnitte);
        abfragevarianteBauleitplanverfahren.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        abfragevarianteBauleitplanverfahren.setStammdatenGueltigAb(stammdatenGueltigAb);

        final var langfristigerPlanungsursaechlicherBedarf = new LangfristigerPlanungsursaechlicherBedarfModel();
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

        calculationService.calculateAndAppendBedarfeToAbfragevariante(abfragevarianteBauleitplanverfahren);

        final var expectedAbfragevariante = new AbfragevarianteWeiteresVerfahrenModel();
        expectedAbfragevariante.setArtAbfragevariante(ArtAbfrage.WEITERES_VERFAHREN);
        expectedAbfragevariante.setBauabschnitte(bauabschnitte);
        expectedAbfragevariante.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        expectedAbfragevariante.setStammdatenGueltigAb(stammdatenGueltigAb);
        expectedAbfragevariante.setLangfristigerPlanungsursaechlicherBedarf(langfristigerPlanungsursaechlicherBedarf);

        assertThat(abfragevarianteBauleitplanverfahren, is(expectedAbfragevariante));

        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );
    }

    @Test
    void calculateAndAppendBedarfeToAbfragevarianteBaugenehmigungsverfahren() throws CalculationException {
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);

        final var abfragevarianteBauleitplanverfahren = new AbfragevarianteBaugenehmigungsverfahrenModel();
        abfragevarianteBauleitplanverfahren.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        abfragevarianteBauleitplanverfahren.setBauabschnitte(bauabschnitte);
        abfragevarianteBauleitplanverfahren.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        abfragevarianteBauleitplanverfahren.setStammdatenGueltigAb(stammdatenGueltigAb);

        final var langfristigerPlanungsursaechlicherBedarf = new LangfristigerPlanungsursaechlicherBedarfModel();
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

        calculationService.calculateAndAppendBedarfeToAbfragevariante(abfragevarianteBauleitplanverfahren);

        final var expectedAbfragevariante = new AbfragevarianteBaugenehmigungsverfahrenModel();
        expectedAbfragevariante.setArtAbfragevariante(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        expectedAbfragevariante.setBauabschnitte(bauabschnitte);
        expectedAbfragevariante.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        expectedAbfragevariante.setStammdatenGueltigAb(stammdatenGueltigAb);
        expectedAbfragevariante.setLangfristigerPlanungsursaechlicherBedarf(langfristigerPlanungsursaechlicherBedarf);

        assertThat(abfragevarianteBauleitplanverfahren, is(expectedAbfragevariante));

        Mockito
            .verify(calculationService, Mockito.times(1))
            .calculateLangfristigerPlanungsursaechlicherBedarf(
                bauabschnitte,
                sobonOrientierungswertJahr,
                stammdatenGueltigAb
            );
    }

    @Test
    void calculateAndAppendBedarfeToAbfragevarianteBauleitplanverfahren() throws CalculationException {
        final var bauabschnitte = List.of(new BauabschnittModel());
        final var sobonOrientierungswertJahr = SobonOrientierungswertJahr.JAHR_2017;
        final var stammdatenGueltigAb = LocalDate.of(2020, 5, 30);

        final var abfragevarianteBauleitplanverfahren = new AbfragevarianteBauleitplanverfahrenModel();
        abfragevarianteBauleitplanverfahren.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        abfragevarianteBauleitplanverfahren.setBauabschnitte(bauabschnitte);
        abfragevarianteBauleitplanverfahren.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        abfragevarianteBauleitplanverfahren.setStammdatenGueltigAb(stammdatenGueltigAb);

        final var langfristigerPlanungsursaechlicherBedarf = new LangfristigerPlanungsursaechlicherBedarfModel();
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

        calculationService.calculateAndAppendBedarfeToAbfragevariante(abfragevarianteBauleitplanverfahren);

        final var expectedAbfragevariante = new AbfragevarianteBauleitplanverfahrenModel();
        expectedAbfragevariante.setArtAbfragevariante(ArtAbfrage.BAULEITPLANVERFAHREN);
        expectedAbfragevariante.setBauabschnitte(bauabschnitte);
        expectedAbfragevariante.setSobonOrientierungswertJahr(sobonOrientierungswertJahr);
        expectedAbfragevariante.setStammdatenGueltigAb(stammdatenGueltigAb);
        expectedAbfragevariante.setLangfristigerPlanungsursaechlicherBedarf(langfristigerPlanungsursaechlicherBedarf);

        assertThat(abfragevarianteBauleitplanverfahren, is(expectedAbfragevariante));

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

        final var expected = new LangfristigerPlanungsursaechlicherBedarfModel();

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