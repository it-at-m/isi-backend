package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.mapper.BaurateDomainMapper;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import de.muenchen.isi.infrastructure.entity.enums.IdealtypischeBaurateTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Bauratenmethodik;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.repository.stammdaten.IdealtypischeBaurateRepository;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SobonursaechlicheWohneinheitenService {

    public static final BigDecimal TAUSEND = new BigDecimal(1000);
    private final FoerdermixUmlageService foerdermixUmlageService;
    private final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;
    private final BaurateDomainMapper baurateDomainMapper;
    private final IdealtypischeBaurateRepository idealtypischeBaurateRepository;

    /**
     * Errechnet SoBoN-ursächliche Wohneinheiten je Förderart und Jahr.
     * Ursprungsinfo ist die SoBoN-ursächliche Geschossfläche in der Abfragevariante.
     * Die Fördermixe aller Bauraten werden vor der Berechnung umgelegt, siehe {@link FoerdermixUmlageService}.
     * <p>
     * Je nach {@code bauratenmethodik} wird entweder die alte Methodik (max. 1.000 WE pro Jahr) oder die neue
     * Methodik (idealtypische Bauraten gemäß ISI2-292) angewandt. Bei {@code null} wird die alte Methodik verwendet
     * (Rückwärtskompatibilität für bestehende Abfragen).
     *
     * @param sobonGf          SoBoN-ursächliche Geschossfläche in der Abfragevariante.
     * @param bauabschnitte    Eine Liste von {@link BauabschnittModel}, aus denen die erste {@link BaurateModel} extrahiert wird.
     * @param sobonJahr        Das SoBoN-Jahr, welches die städtebaulichen Orientierungswerte diktiert.
     * @param gueltigAb        Das Gültigkeitsdatum der Stammdaten, welche die Umlegung diktieren.
     * @param foerdermix       Der anzuwendende Fördermix.
     * @param bauratenmethodik Die zu verwendende Bauratenmethodik; bei {@code null} wird {@link Bauratenmethodik#ALTE_BAURATENMETHODIK} angewandt.
     * @return Eine Liste von {@link WohneinheitenProFoerderartProJahrModel}, welche alle Wohneinheiten pro Förderart und Jahr darstellt.
     */
    public List<WohneinheitenProFoerderartProJahrModel> calculateSobonursaechlicheWohneinheiten(
        final BigDecimal sobonGf,
        final List<BauabschnittModel> bauabschnitte,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb,
        final FoerdermixModel foerdermix,
        final Bauratenmethodik bauratenmethodik
    ) throws CalculationException {
        if (bauabschnitte == null || bauabschnitte.isEmpty()) {
            throw new CalculationException("Keine Bauabschnitte vorhanden.");
        }
        final var ersteBaugebiete = bauabschnitte.get(0).getBaugebiete();
        if (ersteBaugebiete == null || ersteBaugebiete.isEmpty()) {
            throw new CalculationException("Keine Baugebiete im ersten Bauabschnitt vorhanden.");
        }
        final var ersteBauraten = ersteBaugebiete.get(0).getBauraten();
        if (ersteBauraten == null || ersteBauraten.isEmpty()) {
            throw new CalculationException("Keine Bauraten im ersten Baugebiet vorhanden.");
        }
        final var baurateClone = baurateDomainMapper.deepClone(ersteBauraten.get(0));
        baurateClone.setFoerdermix(foerdermixUmlageService.legeFoerdermixUm(foerdermix, gueltigAb));

        if (Bauratenmethodik.NEUE_BAURATENMETHODIK == bauratenmethodik) {
            return calculateNeueBauratenmethodik(baurateClone, sobonGf, sobonJahr);
        }
        return calculateAlteBauratenmethodik(baurateClone, sobonGf, sobonJahr);
    }

    /**
     * Alte Bauratenmethodik: Die berechnete WE-Anzahl wird in Blöcke zu maximal 1.000 WE pro Jahr aufgeteilt.
     */
    private List<WohneinheitenProFoerderartProJahrModel> calculateAlteBauratenmethodik(
        final BaurateModel baurateClone,
        final BigDecimal sobonGf,
        final SobonOrientierungswertJahr sobonJahr
    ) {
        final var sobonsursachlicheWohneinheitenList = new ArrayList<WohneinheitenProFoerderartProJahrModel>();
        calculateWohneinheiten(baurateClone, 0, sobonGf, sobonJahr.getGueltigAb(), sobonsursachlicheWohneinheitenList);

        BigDecimal summeWe = summiereWohneinheiten(sobonsursachlicheWohneinheitenList);

        if (1 == summeWe.compareTo(TAUSEND)) {
            sobonsursachlicheWohneinheitenList.clear();

            BigDecimal gfWohnen1000 = sobonGf.multiply(
                TAUSEND.divide(summeWe, CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP)
            );
            BigDecimal anzahl = summeWe.divide(TAUSEND, 0, RoundingMode.FLOOR);

            int jahr = 0;
            for (BigDecimal i = BigDecimal.ZERO; i.compareTo(anzahl) < 0; i = i.add(BigDecimal.ONE)) {
                calculateWohneinheiten(
                    baurateClone,
                    jahr,
                    gfWohnen1000,
                    sobonJahr.getGueltigAb(),
                    sobonsursachlicheWohneinheitenList
                );
                jahr++;
            }

            final BigDecimal weRemainder = summeWe.remainder(TAUSEND);
            if (weRemainder.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal gfWohnenRest = sobonGf.multiply(
                    weRemainder.divide(summeWe, CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP)
                );
                calculateWohneinheiten(
                    baurateClone,
                    anzahl.intValue(),
                    gfWohnenRest,
                    sobonJahr.getGueltigAb(),
                    sobonsursachlicheWohneinheitenList
                );
            }
        }

        return sobonsursachlicheWohneinheitenList;
    }

    /**
     * Neue Bauratenmethodik: Die berechnete WE-Anzahl wird gemäß den idealtypischen Bauraten (ISI2-292)
     * auf Jahresraten verteilt. Die Jahresraten werden anhand der Gesamt-WE aus den Stammdaten ermittelt.
     */
    private List<WohneinheitenProFoerderartProJahrModel> calculateNeueBauratenmethodik(
        final BaurateModel baurateClone,
        final BigDecimal sobonGf,
        final SobonOrientierungswertJahr sobonJahr
    ) throws CalculationException {
        final var weProFoerderart = new ArrayList<WohneinheitenProFoerderartProJahrModel>();
        calculateWohneinheiten(baurateClone, 0, sobonGf, sobonJahr.getGueltigAb(), weProFoerderart);

        final BigDecimal totalWe = summiereWohneinheiten(weProFoerderart);

        final var idealtypischeBaurate = idealtypischeBaurateRepository
            .findByTypAndVonLessThanEqualAndBisExklusivGreaterThan(
                IdealtypischeBaurateTyp.ANZAHL_WOHNEINHEITEN_GESAMT,
                totalWe
            )
            .orElseThrow(() ->
                new CalculationException(
                    "Für " +
                        totalWe +
                        " SoBoN-ursächliche Wohneinheiten konnte keine idealtypische Baurate ermittelt werden."
                )
            );

        final var result = new ArrayList<WohneinheitenProFoerderartProJahrModel>();
        final var jahresraten = idealtypischeBaurate.getJahresraten();
        final Map<String, BigDecimal> partialSumPerFoerderart = new HashMap<>();

        for (int i = 0; i < jahresraten.size(); i++) {
            final var jahresrate = jahresraten.get(i);
            final int ausgabeJahr = baurateClone.getJahr() + jahresrate.getJahr() - 1;
            final boolean isLastJahr = (i == jahresraten.size() - 1);

            for (final var weEntry : weProFoerderart) {
                final var foerderart = weEntry.getFoerderart();
                final BigDecimal foerderartWe = weEntry.getWohneinheiten();
                final BigDecimal partial = partialSumPerFoerderart.getOrDefault(foerderart, BigDecimal.ZERO);

                final BigDecimal weForJahr;
                if (isLastJahr) {
                    weForJahr = foerderartWe.subtract(partial);
                } else {
                    weForJahr = foerderartWe
                        .multiply(jahresrate.getRate())
                        .setScale(CalculationService.DIVISION_SCALE, RoundingMode.DOWN);
                    partialSumPerFoerderart.put(foerderart, partial.add(weForJahr));
                }

                result.add(
                    new WohneinheitenProFoerderartProJahrModel(foerderart, String.valueOf(ausgabeJahr), weForJahr)
                );
            }
        }

        return result;
    }

    /**
     * Wohneinheiten je Förderart = SoBoN-ursächliche GF Wohnen * Anteil GF von gesamt (Prozentsatz Förderart) / Durchschnittliche GF je Wohnungstyp
     * <p>
     * Durchschnittliche GF je Wohnungstyp:
     * Stammdaten aus den städtebaulichen Orientierungswerten (Werte in Abhängigkeit des Jahres der SoBoN-Orientierungswerte -->
     * Auswahl in Abfragevariante im Rahmen "Weitere Berechnungsgrundlagen" im Feld "Jahr für SoBoN-Orientierungswerte"
     *
     * @param baurate                            Baurate (erstes Jahr). Werden mehrere Bauabschnitte angelegt, wird vom ersten Bauabschnitt,
     *                                           der angelegt wurde, das erste Baugebiet, das angelegt wurde und dann davon die erste Baurate verwendet.
     * @param jahr                               bzw. 1000er Block
     * @param sobonGf                            SoBoN-ursächliche Geschossfläche in der Abfragevariante
     * @param sobonJahr                          Das SoBoN-Jahr, welches die städtebaulichen Orientierungswerte diktiert.
     * @param sobonsursachlicheWohneinheitenList Eine Liste von {@link WohneinheitenProFoerderartProJahrModel}, welche alle Wohneinheiten pro Förderart und Jahr darstellt.
     */
    private BigDecimal summiereWohneinheiten(final List<WohneinheitenProFoerderartProJahrModel> wohneinheitenList) {
        BigDecimal summe = BigDecimal.ZERO;
        for (final WohneinheitenProFoerderartProJahrModel we : wohneinheitenList) {
            summe = summe.add(we.getWohneinheiten());
        }
        return summe;
    }

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
                final var average = orientierungswert.get().getDurchschnittlicheGrundflaeche();
                wohneinheiten = sobonGf
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
