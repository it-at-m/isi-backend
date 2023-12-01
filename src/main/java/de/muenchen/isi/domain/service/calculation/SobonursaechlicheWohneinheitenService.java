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
public class SobonursaechlicheWohneinheitenService {

    private final FoerdermixUmlageService foerdermixUmlageService;

    private final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    public static final String SUMMARY_NAME = "Gesamt";

    public static final List<Integer> SUMMARY_PERIODS = List.of(10, 15, 20);

    public static final String SUMMARY_OVER_PERIOD_NAME = "Summe der ersten %d Jahre";

    /**
     * Errechnet SoBoN-ursächliche Wohneinheiten je Förderart und Jahr.
     * Ursprungsinfo ist die SoBoN-ursächliche Geschossfläche in der Abfragevariante
     * Die Fördermixe aller Bauraten werden vor der Berechnung umgelegt, siehe {@link FoerdermixUmlageService}.
     *
     * Ist das Ergebnis aus dieser Rechnung über 1000 Wohneinheiten, werden die Wohneinheiten auf mehrere Bauraten (mehrere Jahre) aufgeteilt.
     * Es wird immer bei 1000 WE ein Schnitt gemacht. Im angehängten Dokument ist der Rechenweg abgebildet.
     *
     * Tabelle 2: Errechnung der Wohneinheiten je Förderart für die Jahre mit 1000 Wohneinheiten (siehe Formel im gelben Feld).
     * Tabelle 3: Errechnung der letzten Bauraten, die keine 1000 Wohneinheiten mehr hat (siehe Formel im gelben Feld).
     *
     * Für die Summenbildung gibt es eine zusätzliche "Förderart", die die Summen aller Förderarten pro Jahr enthält.
     * Außerdem gibt es zusätzliche Jahre pro Förderart, die über 10, 15 & 20 Jahre die Wohneinheiten aufsummieren.
     *
     *
     * @param sobonGF SoBoN-ursächliche Geschossfläche in der Abfragevariante
     * @param baurate Baurate (erstes Jahr). Werden mehrere Bauabschnitte angelegt, wird vom ersten Bauabschnitt,
     *         der angelegt wurde, das erste Baugebiet, das angelegt wurde und dann davon die erste Baurate verwendet.
     * @param sobonJahr Das SoBoN-Jahr, welches die städtebaulichen Orientierungswerte diktiert.
     * @param gueltigAb Das Gültigkeitsdatum der Stammdaten, welche die Umlegung diktieren.
     * @return Eine Liste von {@link PlanungsursachlicheWohneinheitenModel}, welche alle Wohneinheiten pro Förderart und Jahr darstellt.
     */
    public List<PlanungsursachlicheWohneinheitenModel> calculateSobonursaechlicheWohneinheiten(
        final BigDecimal sobonGF,
        final BaurateModel baurate,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
        final var sobonsursachlicheWohneinheitenList = new ArrayList<PlanungsursachlicheWohneinheitenModel>();

        // Umlegen von Förderarten
        baurate.setFoerdermix(foerdermixUmlageService.legeFoerdermixUm(baurate.getFoerdermix(), gueltigAb));

        // Berechnen der Wohneinheiten pro Förderart und Jahr
        calculateWohneinheiten(baurate, 0, sobonGF, sobonJahr.getGueltigAb(), sobonsursachlicheWohneinheitenList);

        // Summe der Wohneinheiten aller Förderarten
        BigDecimal summeWE = BigDecimal.ZERO;
        for (PlanungsursachlicheWohneinheitenModel sobonursaechlicheWE : sobonsursachlicheWohneinheitenList) {
            summeWE = summeWE.add(sobonursaechlicheWE.getWohneinheiten());
        }

        // Ist das Ergebnis über 1000, werden 1000er-Blöcke/Jahre gebildet.
        if (summeWE.intValue() > 1000) {
            sobonsursachlicheWohneinheitenList.clear();

            BigDecimal gfWohnen1000 = sobonGF.multiply(
                BigDecimal.valueOf(1000).divide(summeWE, CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN)
            );
            // Anzahl 1000er-Blöcke/Jahre summeWE / 1000 = Ergebnis abrunden
            Integer anzahl = summeWE
                .divide(new BigDecimal(1000), CalculationService.DIVISION_SCALE, RoundingMode.HALF_DOWN)
                .intValue();
            System.out.println("anzahl: " + anzahl);
            for (int i = 0; i < anzahl; i++) {
                calculateWohneinheiten(
                    baurate,
                    i,
                    gfWohnen1000,
                    sobonJahr.getGueltigAb(),
                    sobonsursachlicheWohneinheitenList
                );
            }

            // letztes Jahr
            BigDecimal gfWohnenRest = sobonGF.multiply(
                summeWE
                    .remainder(new BigDecimal(1000))
                    .divide(summeWE, CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN)
            );
            calculateWohneinheiten(
                baurate,
                anzahl,
                gfWohnenRest,
                sobonJahr.getGueltigAb(),
                sobonsursachlicheWohneinheitenList
            );
        }

        // TODO doch nur eine Liste verwenden, damit Summenbildung an einem Ort stattfinden kann
        // Hinzufügen von zusätzlichen Jahren pro Förderart, welche eine Anzahl von Jahren über einen Zeitraum summieren.
        sobonsursachlicheWohneinheitenList.sort(Comparator.comparingInt(a -> Integer.parseInt(a.getJahr())));
        final var firstYear = Integer.parseInt(sobonsursachlicheWohneinheitenList.get(0).getJahr());
        final var sumsPerFoerderart = new ArrayList<PlanungsursachlicheWohneinheitenModel>();
        for (final var sobonursachlicheWohneinheiten : sobonsursachlicheWohneinheitenList) {
            for (final var period : SUMMARY_PERIODS) {
                if (Integer.parseInt(sobonursachlicheWohneinheiten.getJahr()) <= firstYear + period - 1) {
                    mergeSobonursaechlicheWohneinheiten(
                        sumsPerFoerderart,
                        sobonursachlicheWohneinheiten.getFoerderart(),
                        String.format(SUMMARY_OVER_PERIOD_NAME, period),
                        sobonursachlicheWohneinheiten.getWohneinheiten()
                    );
                }
            }
        }
        sobonsursachlicheWohneinheitenList.addAll(sumsPerFoerderart);

        // Hinzufügen einer Förderart mit den Summen aller Förderarten pro Jahr.
        final var sumsPerYear = new ArrayList<PlanungsursachlicheWohneinheitenModel>();
        for (final var sobonursachlicheWohneinheiten : sobonsursachlicheWohneinheitenList) {
            mergeSobonursaechlicheWohneinheiten(
                sumsPerYear,
                SUMMARY_NAME,
                sobonursachlicheWohneinheiten.getJahr(),
                sobonursachlicheWohneinheiten.getWohneinheiten()
            );
        }
        sobonsursachlicheWohneinheitenList.addAll(sumsPerYear);

        return sobonsursachlicheWohneinheitenList;
    }

    // Wohneinheiten je Förderart = SoBoN-ursächliche GF Wohnen * Anteil GF von gesamt (Prozentsatz Förderart) / Durchschnittliche GF je Wohnungstyp
    //
    // Durchschnittliche GF je Wohnungstyp:
    // Stammdaten aus den städtebaulichen Orientierungswerten (Werte in Abhängigkeit des Jahres der SoBoN-Orientierungswerte -->
    // Auswahl in Abfragevariante im Rahmen "Weitere Berechnungsgrundlagen" im Feld "Jahr für SoBoN-Orientierungswerte"
    protected void calculateWohneinheiten(
        final BaurateModel baurate,
        final Integer jahr,
        final BigDecimal sobonGF,
        final LocalDate sobonJahr,
        final List<PlanungsursachlicheWohneinheitenModel> sobonsursachlicheWohneinheitenList
    ) {
        // Berechnen der Wohneinheiten pro Förderart und Jahr
        for (final var foerderart : baurate.getFoerdermix().getFoerderarten()) {
            var wohneinheiten = BigDecimal.ZERO;
            final var orientierungswert =
                staedtebaulicheOrientierungswertRepository.findFirstByFoerderartBezeichnungAndGueltigAbIsLessThanEqualOrderByGueltigAbDesc(
                    foerderart.getBezeichnung(),
                    sobonJahr
                );
            if (orientierungswert.isPresent()) {
                final var average = BigDecimal.valueOf(orientierungswert.get().getDurchschnittlicheGrundflaeche());
                wohneinheiten =
                    sobonGF
                        .multiply(foerderart.getAnteilProzent())
                        .divide(average, CalculationService.DIVISION_SCALE, RoundingMode.HALF_EVEN);
            }

            sobonsursachlicheWohneinheitenList.add(
                new PlanungsursachlicheWohneinheitenModel(
                    foerderart.getBezeichnung(),
                    String.valueOf(baurate.getJahr() + jahr),
                    wohneinheiten
                )
            );
        }
    }

    protected void mergeSobonursaechlicheWohneinheiten(
        final List<PlanungsursachlicheWohneinheitenModel> sobonursachlicheWohneinheitenList,
        final String foerderart,
        final String jahr,
        final BigDecimal wohneinheiten
    ) {
        final var sobonursaechlicheWohneinheitenOptional = sobonursachlicheWohneinheitenList
            .stream()
            .filter(sobonursachlicheWohneinheiten ->
                sobonursachlicheWohneinheiten.getFoerderart().equals(foerderart) &&
                sobonursachlicheWohneinheiten.getJahr().equals(jahr)
            )
            .findAny();

        if (sobonursaechlicheWohneinheitenOptional.isEmpty()) {
            sobonursachlicheWohneinheitenList.add(
                new PlanungsursachlicheWohneinheitenModel(foerderart, jahr, wohneinheiten)
            );
        } else {
            sobonursaechlicheWohneinheitenOptional
                .get()
                .setWohneinheiten(sobonursaechlicheWohneinheitenOptional.get().getWohneinheiten().add(wohneinheiten));
        }
    }
}
