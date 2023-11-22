package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursachlicheWohneinheitenModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanungsursaechlicheWohneinheitenService {

    public static final String SUMMARY_NAME = "Gesamt";
    public static final List<Integer> SUMMARY_PERIODS = List.of(10, 15, 20);

    public static final String SUMMARY_OVER_PERIOD_NAME = "Summe der ersten %d Jahre";

    private final FoerdermixUmlageService foerdermixUmlageService;

    private final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    /**
     * Errechnet planungsursächliche Wohneinheiten pro Förderart und Jahr.
     * Dafür werden entweder bevorzugt die Anzahl der Wohneinheiten oder die Geschossflächen von Bauraten herangezogen.
     * Hat eine Baurate nur die Geschossfläche, wird sie entsprechend der städtebaulichen Orientierungswerte in Wohneinheiten umgerechnet.
     * Die Fördermixe aller Bauraten werden vor der Berechnung umgelegt, siehe {@link FoerdermixUmlageService}.
     * Außerdem wird eine zusätzliche "Förderart" hinzugefügt, welche die Summen aller Förderarten pro Jahr enthält.
     *
     * @param bauabschnitte Eine List von {@link BauabschnittModel}, aus denen die {@link BaurateModel} extrahiert werden.
     * @param sobonJahr Das SoBoN-Jahr, welches die städtebaulichen Orientierungswerte diktiert.
     * @param gueltigAb Das Gültigkeitsdatum der Stammdaten, welche die Umlegung diktieren.
     * @return Eine List von {@link PlanungsursachlicheWohneinheitenModel}, welche alle Wohneinheiten pro Förderart und Jahr darstellt.
     */
    public List<PlanungsursachlicheWohneinheitenModel> calculatePlanungsursaechlicheWohneinheiten(
        final List<BauabschnittModel> bauabschnitte,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
        final var planungsursachlicheWohneinheitenList = new ArrayList<PlanungsursachlicheWohneinheitenModel>();

        // Aggregation aller Bauraten und Umlegen von Förderarten.

        final var bauraten = bauabschnitte
            .stream()
            .flatMap(bauabschnitt ->
                bauabschnitt
                    .getBaugebiete()
                    .stream()
                    .flatMap(baugebiet ->
                        baugebiet
                            .getBauraten()
                            .stream()
                            .peek(baurate ->
                                baurate.setFoerdermix(
                                    foerdermixUmlageService.legeFoerdermixUm(baurate.getFoerdermix(), gueltigAb)
                                )
                            )
                    )
            )
            .collect(Collectors.toList());

        // Berechnen der Wohneinheiten pro Förderart und Jahr.

        for (final var baurate : bauraten) {
            for (final var foerderart : baurate.getFoerdermix().getFoerderarten()) {
                final var wohneinheiten = calculateWohneinheiten(baurate, foerderart, sobonJahr);
                mergePlanungsursaechlicheWohneinheiten(
                    planungsursachlicheWohneinheitenList,
                    foerderart.getBezeichnung(),
                    baurate.getJahr().toString(),
                    wohneinheiten
                );
            }
        }

        if (planungsursachlicheWohneinheitenList.isEmpty()) {
            return planungsursachlicheWohneinheitenList;
        }

        // Hinzufügen von zusätzlichen Jahren pro Förderart, welche eine Anzahl von Jahren über einen Zeitraum summieren.

        planungsursachlicheWohneinheitenList.sort(Comparator.comparingInt(a -> Integer.parseInt(a.getJahr())));
        final var firstYear = Integer.parseInt(planungsursachlicheWohneinheitenList.get(0).getJahr());
        final var sumsPerFoerderart = new ArrayList<PlanungsursachlicheWohneinheitenModel>();
        for (final var planungsursachlicheWohneinheiten : planungsursachlicheWohneinheitenList) {
            for (final var period : SUMMARY_PERIODS) {
                if (Integer.parseInt(planungsursachlicheWohneinheiten.getJahr()) <= firstYear + period - 1) {
                    mergePlanungsursaechlicheWohneinheiten(
                        sumsPerFoerderart,
                        planungsursachlicheWohneinheiten.getFoerderart(),
                        String.format(SUMMARY_OVER_PERIOD_NAME, period),
                        planungsursachlicheWohneinheiten.getWohneinheiten()
                    );
                }
            }
        }
        planungsursachlicheWohneinheitenList.addAll(sumsPerFoerderart);

        // Hinzufügen einer Förderart mit den Summen aller anderer Förderarten pro Jahr.

        final var sumsPerYear = new ArrayList<PlanungsursachlicheWohneinheitenModel>();
        for (final var planungsursachlicheWohneinheiten : planungsursachlicheWohneinheitenList) {
            mergePlanungsursaechlicheWohneinheiten(
                sumsPerYear,
                SUMMARY_NAME,
                planungsursachlicheWohneinheiten.getJahr(),
                planungsursachlicheWohneinheiten.getWohneinheiten()
            );
        }
        planungsursachlicheWohneinheitenList.addAll(sumsPerYear);

        return planungsursachlicheWohneinheitenList;
    }

    protected BigDecimal calculateWohneinheiten(
        final BaurateModel baurate,
        final FoerderartModel foerderart,
        final SobonOrientierungswertJahr sobonJahr
    ) {
        if (baurate.getWeGeplant() != null) {
            return BigDecimal.valueOf(baurate.getWeGeplant()).multiply(foerderart.getAnteilProzent());
        } else if (baurate.getGfWohnenGeplant() != null) {
            final var orientierungswert =
                staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    foerderart.getBezeichnung(),
                    sobonJahr.getGueltigAb()
                );
            if (orientierungswert.isPresent()) {
                final var average = BigDecimal.valueOf(orientierungswert.get().getDurchschnittlicheGrundflaeche());
                return baurate
                    .getGfWohnenGeplant()
                    .multiply(foerderart.getAnteilProzent())
                    .divide(average, CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
            }
        }

        return BigDecimal.ZERO;
    }

    protected void mergePlanungsursaechlicheWohneinheiten(
        final List<PlanungsursachlicheWohneinheitenModel> planungsursachlicheWohneinheitenList,
        final String foerderart,
        final String jahr,
        final BigDecimal wohneinheiten
    ) {
        final var planungsursaechlicheWohneinheitenOptional = planungsursachlicheWohneinheitenList
            .stream()
            .filter(planungsursachlicheWohneinheiten ->
                planungsursachlicheWohneinheiten.getFoerderart().equals(foerderart) &&
                planungsursachlicheWohneinheiten.getJahr().equals(jahr)
            )
            .findAny();

        if (planungsursaechlicheWohneinheitenOptional.isEmpty()) {
            planungsursachlicheWohneinheitenList.add(
                new PlanungsursachlicheWohneinheitenModel(foerderart, jahr, wohneinheiten)
            );
        } else {
            planungsursaechlicheWohneinheitenOptional
                .get()
                .setWohneinheiten(
                    planungsursaechlicheWohneinheitenOptional.get().getWohneinheiten().add(wohneinheiten)
                );
        }
    }
}