package de.muenchen.isi.domain.service;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.FoerdermixModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenBedarfModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public static final int DIVISION_SCALE = 10;

    public PlanungsursaechlicherBedarfModel calculatePlanungsursaechlicherBedarf(
        final List<BauabschnittModel> bauabschnitte,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) throws CalculationException {
        final var planungsursaechlicherBedarf = new PlanungsursaechlicherBedarfModel();
        final var wohneinheitenBedarfe = new ArrayList<WohneinheitenBedarfModel>();
        planungsursaechlicherBedarf.setWohneinheitenBedarfe(wohneinheitenBedarfe);

        // Aggregation aller Bauraten + Umlegen von Förderarten

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
                            .peek(baurate -> baurate.setFoerdermix(legeFoerdermixUm(baurate.getFoerdermix(), gueltigAb))
                            )
                    )
            )
            .collect(Collectors.toList());

        if (bauraten.isEmpty()) {
            throw new CalculationException("Die Berechnung erfordert Bauraten.");
        }

        // Berechnen der Wohneinheiten pro Förderart pro Jahr

        for (final var baurate : bauraten) {
            for (final var foerderart : baurate.getFoerdermix().getFoerderarten()) {
                final var wohneinheiten = calculateWohneinheiten(baurate, foerderart, sobonJahr);
                mergeWohneinheitenBedarf(
                    wohneinheitenBedarfe,
                    foerderart.getBezeichnung(),
                    baurate.getJahr(),
                    wohneinheiten
                );
            }
        }

        // Hinzufügen einer Förderart mit den Summen aller anderer Förderarten pro Jahr

        final var sums = new ArrayList<WohneinheitenBedarfModel>();
        for (final var bedarf : wohneinheitenBedarfe) {
            mergeWohneinheitenBedarf(sums, SUMMARY_NAME, bedarf.getJahr(), bedarf.getWohneinheiten());
        }
        wohneinheitenBedarfe.addAll(sums);

        return planungsursaechlicherBedarf;
    }

    private FoerdermixModel legeFoerdermixUm(final FoerdermixModel foerdermix, final LocalDate gueltigAb) {
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

        final var umgelegterFoerdermix = new FoerdermixModel();
        final var umgelegteFoerderarten = new ArrayList<FoerderartModel>();
        umgelegterFoerdermix.setFoerderarten(umgelegteFoerderarten);

        for (final var foerderart : foerdermix.getFoerderarten()) {
            if (foerderart.getBezeichnung() != null && foerderart.getAnteilProzent() != null) {
                if (umlegungen.containsKey(foerderart.getBezeichnung())) {
                    umlegungen
                        .get(foerderart.getBezeichnung())
                        .forEach((bezeichnung, anteil) -> {
                            final var umgelegterAnteil = foerderart.getAnteilProzent().multiply(anteil);
                            mergeFoerderart(umgelegteFoerderarten, bezeichnung, umgelegterAnteil);
                        });
                } else {
                    mergeFoerderart(umgelegteFoerderarten, foerderart.getBezeichnung(), foerderart.getAnteilProzent());
                }
            }
        }

        return umgelegterFoerdermix;
    }

    private BigDecimal calculateWohneinheiten(
        final BaurateModel baurate,
        final FoerderartModel foerderart,
        final SobonOrientierungswertJahr sobonJahr
    ) {
        final var durchschnittlicheGf = 90;

        if (baurate.getWeGeplant() != null) {
            return BigDecimal.valueOf(baurate.getWeGeplant()).multiply(foerderart.getAnteilProzent());
        } else if (baurate.getGfWohnenGeplant() != null) {
            final var average = BigDecimal.valueOf(durchschnittlicheGf);
            return baurate
                .getGfWohnenGeplant()
                .multiply(foerderart.getAnteilProzent())
                .divide(average, DIVISION_SCALE, RoundingMode.HALF_EVEN);
        }

        return BigDecimal.ZERO;
    }

    private void mergeWohneinheitenBedarf(
        final List<WohneinheitenBedarfModel> wohneinheitenBedarfe,
        final String foerderart,
        final Integer jahr,
        final BigDecimal wohneinheiten
    ) {
        final var wohneinheitenBedarf = wohneinheitenBedarfe
            .stream()
            .filter(bedarf -> bedarf.getFoerderart().equals(foerderart) && bedarf.getJahr().equals(jahr))
            .findAny();

        if (wohneinheitenBedarf.isEmpty()) {
            wohneinheitenBedarfe.add(new WohneinheitenBedarfModel(foerderart, jahr, wohneinheiten));
        } else {
            wohneinheitenBedarf.get().setWohneinheiten(wohneinheitenBedarf.get().getWohneinheiten().add(wohneinheiten));
        }
    }

    private void mergeFoerderart(
        final List<FoerderartModel> foerderarten,
        final String bezeichnung,
        final BigDecimal anteilProzent
    ) {
        final var foerderart = foerderarten
            .stream()
            .filter(bedarf -> bedarf.getBezeichnung().equals(bezeichnung))
            .findAny();

        if (foerderart.isEmpty()) {
            final var newFoerderart = new FoerderartModel();
            newFoerderart.setBezeichnung(bezeichnung);
            newFoerderart.setAnteilProzent(anteilProzent);
            foerderarten.add(newFoerderart);
        } else {
            foerderart.get().setAnteilProzent(foerderart.get().getAnteilProzent().add(anteilProzent));
        }
    }
}
