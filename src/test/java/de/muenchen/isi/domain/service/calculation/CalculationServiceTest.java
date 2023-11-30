package de.muenchen.isi.domain.service.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.calculation.InfrastrukturbedarfProJahrModel;
import de.muenchen.isi.domain.model.calculation.LangfristigerPlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.calculation.PersonenProJahrModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
class CalculationServiceTest {

    @Mock
    private PlanungsursaechlicheWohneinheitenService planungsursaechlicheWohneinheitenService;

    @Mock
    private InfrastrukturbedarfService infrastrukturbedarfService;

    private CalculationService calculationService;

    @BeforeEach
    public void beforeEach() {
        this.calculationService =
            new CalculationService(planungsursaechlicheWohneinheitenService, infrastrukturbedarfService);
        Mockito.reset(planungsursaechlicheWohneinheitenService, infrastrukturbedarfService);
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
        expected.setWohneinheitenSumme10Jahre(null);
        expected.setWohneinheitenSumme15Jahre(null);
        expected.setWohneinheitenSumme20Jahre(null);
        expected.setWohneinheitenGesamt(null);
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