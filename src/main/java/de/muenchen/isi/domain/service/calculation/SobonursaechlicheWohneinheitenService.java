package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SobonursaechlicheWohneinheitenService {

    private final FoerdermixUmlageService foerdermixUmlageService;

    private final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    public static final BigDecimal TAUSEND = new BigDecimal(1000);

    /**
     * Errechnet SoBoN-ursächliche Wohneinheiten je Förderart und Jahr.
     * Ursprungsinfo ist die SoBoN-ursächliche Geschossfläche in der Abfragevariante
     * Die Fördermixe aller Bauraten werden vor der Berechnung umgelegt, siehe {@link FoerdermixUmlageService}.
     *
     * Ist das Ergebnis aus dieser Rechnung über 1000 Wohneinheiten, werden die Wohneinheiten auf mehrere Bauraten (mehrere Jahre) aufgeteilt.
     * Es wird immer bei 1000 WE ein Schnitt gemacht.
     *
     * Für die Summenbildung gibt es eine zusätzliche "Förderart", die die Summen aller Förderarten pro Jahr enthält.
     * Außerdem gibt es zusätzliche Jahre pro Förderart, die über 10, 15 & 20 Jahre die Wohneinheiten aufsummieren.
     *
     * @param sobonGf SoBoN-ursächliche Geschossfläche in der Abfragevariante.
     * @param bauabschnitte Eine List von {@link BauabschnittModel}, aus denen die erste {@link BaurateModel} extrahiert wird.
     * @param sobonJahr Das SoBoN-Jahr, welches die städtebaulichen Orientierungswerte diktiert.
     * @param gueltigAb Das Gültigkeitsdatum der Stammdaten, welche die Umlegung diktieren.
     * @return Eine Liste von {@link WohneinheitenProFoerderartProJahrModel}, welche alle Wohneinheiten pro Förderart und Jahr darstellt.
     */
    public List<WohneinheitenProFoerderartProJahrModel> calculateSobonursaechlicheWohneinheiten(
        final BigDecimal sobonGf,
        final List<BauabschnittModel> bauabschnitte,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
        final var sobonsursachlicheWohneinheitenList = new ArrayList<WohneinheitenProFoerderartProJahrModel>();

        // Ermittlung der ersten Baurate
        final var baurate = bauabschnitte.get(0).getBaugebiete().get(0).getBauraten().get(0);

        // Umlegen von Förderarten
        baurate.setFoerdermix(foerdermixUmlageService.legeFoerdermixUm(baurate.getFoerdermix(), gueltigAb));

        // Berechnen der Wohneinheiten pro Förderart und Jahr
        calculateWohneinheiten(baurate, 0, sobonGf, sobonJahr.getGueltigAb(), sobonsursachlicheWohneinheitenList);

        // Summe der Wohneinheiten aller Förderarten
        BigDecimal summeWe = BigDecimal.ZERO;
        for (WohneinheitenProFoerderartProJahrModel sobonursaechlicheWe : sobonsursachlicheWohneinheitenList) {
            summeWe = summeWe.add(sobonursaechlicheWe.getWohneinheiten());
        }

        // Ist das Ergebnis über 1000, werden 1000er-Blöcke/Jahre gebildet.
        if (1 == summeWe.compareTo(TAUSEND)) {
            sobonsursachlicheWohneinheitenList.clear();

            BigDecimal gfWohnen1000 = sobonGf.multiply(
                TAUSEND.divide(summeWe, CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP)
            );
            // Anzahl 1000er-Blöcke/Jahre = summeWe / 1000 = Ergebnis abrunden
            BigDecimal anzahl = summeWe.divide(TAUSEND, 0, RoundingMode.FLOOR);

            int jahr = 0;
            for (BigDecimal i = BigDecimal.ZERO; i.compareTo(anzahl) < 0; i = i.add(BigDecimal.ONE)) {
                calculateWohneinheiten(
                    baurate,
                    jahr,
                    gfWohnen1000,
                    sobonJahr.getGueltigAb(),
                    sobonsursachlicheWohneinheitenList
                );
                jahr++;
            }

            // letztes Jahr
            BigDecimal gfWohnenRest = sobonGf.multiply(
                summeWe.remainder(TAUSEND).divide(summeWe, CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP)
            );
            calculateWohneinheiten(
                baurate,
                anzahl.intValue(),
                gfWohnenRest,
                sobonJahr.getGueltigAb(),
                sobonsursachlicheWohneinheitenList
            );
        }

        return sobonsursachlicheWohneinheitenList;
    }

    /** Wohneinheiten je Förderart = SoBoN-ursächliche GF Wohnen * Anteil GF von gesamt (Prozentsatz Förderart) / Durchschnittliche GF je Wohnungstyp
     *
     *  Durchschnittliche GF je Wohnungstyp:
     *  Stammdaten aus den städtebaulichen Orientierungswerten (Werte in Abhängigkeit des Jahres der SoBoN-Orientierungswerte -->
     *  Auswahl in Abfragevariante im Rahmen "Weitere Berechnungsgrundlagen" im Feld "Jahr für SoBoN-Orientierungswerte"
     *
     * @param baurate Baurate (erstes Jahr). Werden mehrere Bauabschnitte angelegt, wird vom ersten Bauabschnitt,
     *         der angelegt wurde, das erste Baugebiet, das angelegt wurde und dann davon die erste Baurate verwendet.
     * @param jahr bzw. 1000er Block
     * @param sobonGf SoBoN-ursächliche Geschossfläche in der Abfragevariante
     * @param sobonJahr Das SoBoN-Jahr, welches die städtebaulichen Orientierungswerte diktiert.
     * @param sobonsursachlicheWohneinheitenList Eine Liste von {@link WohneinheitenProFoerderartProJahrModel}, welche alle Wohneinheiten pro Förderart und Jahr darstellt.
     */
    protected void calculateWohneinheiten(
        final BaurateModel baurate,
        final int jahr,
        final BigDecimal sobonGf,
        final LocalDate sobonJahr,
        final List<WohneinheitenProFoerderartProJahrModel> sobonsursachlicheWohneinheitenList
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
                    sobonGf
                        .multiply(foerderart.getAnteilProzent().scaleByPowerOfTen(-2))
                        .divide(average, CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
            }

            sobonsursachlicheWohneinheitenList.add(
                new WohneinheitenProFoerderartProJahrModel(
                    foerderart.getBezeichnung(),
                    String.valueOf(baurate.getJahr() + jahr),
                    wohneinheiten
                )
            );
        }
    }
}
