package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenBedarfModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import de.muenchen.isi.infrastructure.repository.stammdaten.StaedtebaulicheOrientierungswertRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanungsursaechlicheBedarfService {

    public static final String SUMMARY_NAME = "Gesamt";

    public static final int DIVISION_SCALE = 10;

    private final FoerdermixUmlageService foerdermixUmlageService;

    private final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    /**
     * Errechnet planungsursächliche Wohneinheiten pro Förderart und Jahr.
     * Dafür werden entweder die Anzahl der Wohneinheiten oder die Geschossflächen von Bauraten herangezogen.
     * Hat eine Baurate nur die Geschossfläche, wird sie entsprechend der städtebäulichen Orientierungswerte in Wohneinheiten umgerechnet.
     * Die Fördermixe aller Bauraten werden vor der Berechnung umgelegt, siehe {@link FoerdermixUmlageService}.
     * Außerdem wird eine zusätzliche "Förderart" hinzugefügt, welche die Summen aller Förderarten pro Jahr enthält.
     *
     * @param bauabschnitte Eine List von {@link BauabschnittModel}, aus denen die {@link BaurateModel} extrahiert werden.
     * @param sobonJahr Das SoBoN-Jahr, welches die städtebäulichen Orientierungswerte diktiert.
     * @param gueltigAb Das Gültigkeitsdatum der Stammdaten, welche die Umlegung diktieren.
     * @return Ein {@link PlanungsursaechlicherBedarfModel} mit planungsursächlichen Wohneinheiten pro Förderart und Jahr.
     */
    public PlanungsursaechlicherBedarfModel calculatePlanungsursaechlicherBedarf(
        final List<BauabschnittModel> bauabschnitte,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
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
                            .peek(baurate ->
                                baurate.setFoerdermix(
                                    foerdermixUmlageService.legeFoerdermixUm(baurate.getFoerdermix(), gueltigAb)
                                )
                            )
                    )
            )
            .collect(Collectors.toList());

        // Berechnen der Wohneinheiten pro Förderart und Jahr

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

    private BigDecimal calculateWohneinheiten(
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
                    .divide(average, DIVISION_SCALE, RoundingMode.HALF_EVEN);
            }
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
}
