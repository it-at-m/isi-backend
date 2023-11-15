package de.muenchen.isi.domain.service;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProJahrModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationService {

    private final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    public static final String SUMMARY_NAME = "Gesamt";

    /**
     * TODO
     *
     * @param abfragevariante Die Abfragvariante, für die der planungsursächliche Bedarf ermittelt werden soll.
     * @return Der ermittelte planungsursächliche Bedarf.
     */
    public PlanungsursaechlicherBedarfModel calculatePlanungsursaechlicherBedarf(
        final AbfragevarianteModel abfragevariante,
        final LocalDate gueltigAb
    ) throws CalculationException {
        final var durchschnittlicheGf = 90;

        // Sammeln der Berechnungsgrundlagen

        SobonOrientierungswertJahr sobonJahr = null;
        List<BauabschnittModel> bauabschnitte = null;

        final var art = abfragevariante.getArtAbfragevariante();
        if (art != null) {
            switch (art) {
                case BAULEITPLANVERFAHREN:
                    var bauleitplanverfahren = (AbfragevarianteBauleitplanverfahrenModel) abfragevariante;
                    sobonJahr = bauleitplanverfahren.getSobonOrientierungswertJahr();
                    bauabschnitte = bauleitplanverfahren.getBauabschnitte();
                    break;
                default:
                    break;
            }
        }

        if (sobonJahr == null || bauabschnitte == null) {
            throw new CalculationException(
                "Die Berechnung kann für diese Art von Abfragevariante nicht durchgeführt werden."
            );
        }

        // Aggregation aller Bauraten + Umlegung von Förderarten

        final var bauraten = bauabschnitte
            .stream()
            .flatMap(bauabschnitt ->
                emptyIfNull(bauabschnitt.getBaugebiete())
                    .stream()
                    .flatMap(baugebiet ->
                        emptyIfNull(baugebiet.getBauraten())
                            .stream()
                            .peek(baurate -> baurate.setFoerdermix(foerdermixUmlegen(baurate.getFoerdermix())))
                    )
            )
            .collect(Collectors.toList());

        if (bauraten.isEmpty()) {
            throw new CalculationException("Die Berechnung erfordert Bauraten.");
        }

        // Berechnung der Wohneinheiten pro Förderart pro Jahr

        // Strutkur von List<WohneinheitenProFoerderartModel> in Map-Darstellung
        final var wohneinheitenList = new HashMap<String, Map<Integer, BigDecimal>>();
        bauraten.forEach(baurate ->
            baurate
                .getFoerdermix()
                .getFoerderarten()
                .forEach(foerderart -> {
                    wohneinheitenList.putIfAbsent(foerderart.getBezeichnung(), new HashMap<>());
                    final Map<Integer, BigDecimal> jahrMap = wohneinheitenList.get(foerderart.getBezeichnung());

                    var wohneinheiten = BigDecimal.ZERO;
                    if (baurate.getWeGeplant() != null) {
                        wohneinheiten =
                            BigDecimal.valueOf(baurate.getWeGeplant()).multiply(foerderart.getAnteilProzent());
                    } else if (baurate.getGfWohnenGeplant() != null) {
                        final var average = BigDecimal.valueOf(durchschnittlicheGf);
                        wohneinheiten =
                            baurate
                                .getGfWohnenGeplant()
                                .multiply(foerderart.getAnteilProzent())
                                .divide(average, 10, RoundingMode.HALF_EVEN);
                    }

                    jahrMap.merge(baurate.getJahr(), wohneinheiten, BigDecimal::add);
                })
        );

        // Hinzufügen einer Förderart mit den Summen aller anderer Förderarten pro Jahr

        wohneinheitenList.put(
            SUMMARY_NAME,
            wohneinheitenList
                .values()
                .stream()
                .flatMap(entry -> entry.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, BigDecimal::add))
        );

        final var bedarf = new PlanungsursaechlicherBedarfModel();
        bedarf.setWohneinheitenProFoerderart(mapToWohneinheiten(wohneinheitenList));

        return bedarf;
    }

    private FoerdermixModel foerdermixUmlegen(final FoerdermixModel foerdermix) {
        final var umlegungen = Map.of(
            "preis-gedämpfter Mietwohnungsbau",
            Map.of(
                "freifinanzierter Geschosswohnungsbau",
                new BigDecimal("0.25"),
                "geförderter Mietwohnungsbau",
                new BigDecimal("0.75")
            ),
            "konzeptioneller Mietwohnungsbau",
            Map.of(
                "freifinanzierter Geschosswohnungsbau",
                new BigDecimal("0.25"),
                "geförderter Mietwohnungsbau",
                new BigDecimal("0.75")
            ),
            "Baugemeinschaften",
            Map.of("Ein-/Zweifamilienhäuser", new BigDecimal("0.5"), "MünchenModell", new BigDecimal("0.5"))
        );

        // Strutkur vom FoerdermixModel in Map-Darstellung
        final var foerderarten = new HashMap<String, BigDecimal>();
        emptyIfNull(foerdermix != null ? foerdermix.getFoerderarten() : null)
            .forEach(foerderart -> {
                if (foerderart.getBezeichnung() != null && foerderart.getAnteilProzent() != null) {
                    if (umlegungen.containsKey(foerderart.getBezeichnung())) {
                        umlegungen
                            .get(foerderart.getBezeichnung())
                            .forEach((bezeichnung, anteil) -> {
                                final var umgelegterAnteil = foerderart.getAnteilProzent().multiply(anteil);
                                foerderarten.merge(bezeichnung, umgelegterAnteil, BigDecimal::add);
                            });
                    } else {
                        foerderarten.putIfAbsent(foerderart.getBezeichnung(), foerderart.getAnteilProzent());
                    }
                }
            });

        final var umgelegterFoerdermix = new FoerdermixModel();
        umgelegterFoerdermix.setFoerderarten(
            foerderarten
                .entrySet()
                .stream()
                .map(entry -> {
                    final var foerderart = new FoerderartModel();
                    foerderart.setBezeichnung(entry.getKey());
                    foerderart.setAnteilProzent(entry.getValue());
                    return foerderart;
                })
                .collect(Collectors.toList())
        );

        return umgelegterFoerdermix;
    }

    private List<WohneinheitenProFoerderartModel> mapToWohneinheiten(final Map<String, Map<Integer, BigDecimal>> map) {
        return map
            .entrySet()
            .stream()
            .map(entry -> {
                final var wohneinheitenProFoerderart = new WohneinheitenProFoerderartModel();
                wohneinheitenProFoerderart.setFoerderart(entry.getKey());
                wohneinheitenProFoerderart.setWohneinheitenProJahr(
                    entry
                        .getValue()
                        .entrySet()
                        .stream()
                        .map(jahresBedarf -> {
                            var wohneinheitenProJahr = new WohneinheitenProJahrModel();
                            wohneinheitenProJahr.setJahr(jahresBedarf.getKey());
                            wohneinheitenProJahr.setWohneinheiten(jahresBedarf.getValue());
                            return wohneinheitenProJahr;
                        })
                        .collect(Collectors.toList())
                );
                return wohneinheitenProFoerderart;
            })
            .collect(Collectors.toList());
    }
}
