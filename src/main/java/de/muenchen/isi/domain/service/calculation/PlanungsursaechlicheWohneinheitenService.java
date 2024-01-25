package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaurateModel;
import de.muenchen.isi.domain.model.FoerderartModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
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
public class PlanungsursaechlicheWohneinheitenService {

    private final FoerdermixUmlageService foerdermixUmlageService;

    private final StaedtebaulicheOrientierungswertRepository staedtebaulicheOrientierungswertRepository;

    /**
     * Errechnet planungsursächliche Wohneinheiten pro Förderart und Jahr.
     * Dafür werden entweder bevorzugt die Anzahl der Wohneinheiten oder die Geschossflächen von Bauraten herangezogen.
     * Hat eine Baurate nur die Geschossfläche, wird sie entsprechend der städtebaulichen Orientierungswerte in Wohneinheiten umgerechnet.
     * Die Fördermixe aller Bauraten werden vor der Berechnung umgelegt, siehe {@link FoerdermixUmlageService}.
     *
     * @param bauabschnitte Eine List von {@link BauabschnittModel}, aus denen die {@link BaurateModel} extrahiert werden.
     * @param sobonJahr Das SoBoN-Jahr, welches die städtebaulichen Orientierungswerte diktiert.
     * @param gueltigAb Das Gültigkeitsdatum der Stammdaten, welche die Umlegung diktieren.
     * @return Eine List von {@link WohneinheitenProFoerderartProJahrModel}, welche alle Wohneinheiten pro Förderart und Jahr darstellt.
     */
    public List<WohneinheitenProFoerderartProJahrModel> calculatePlanungsursaechlicheWohneinheiten(
        final List<BauabschnittModel> bauabschnitte,
        final SobonOrientierungswertJahr sobonJahr,
        final LocalDate gueltigAb
    ) {
        final var wohneinheitenProFoerderartProJahrList = new ArrayList<WohneinheitenProFoerderartProJahrModel>();

        // Aggregation aller Bauraten und Umlegen von Förderarten.

        final var bauraten = bauabschnitte
            .stream()
            .flatMap(bauabschnitt ->
                bauabschnitt.getBaugebiete().stream().flatMap(baugebiet -> baugebiet.getBauraten().stream())
            )
            .collect(Collectors.toList());

        // Berechnen der Wohneinheiten pro Förderart und Jahr.

        for (final var baurate : bauraten) {
            final var foerdermixUmgelegt = foerdermixUmlageService.legeFoerdermixUm(baurate.getFoerdermix(), gueltigAb);
            for (final var foerderart : foerdermixUmgelegt.getFoerderarten()) {
                final var wohneinheiten = calculateWohneinheiten(baurate, foerderart, sobonJahr);
                mergeWohneinheitenProFoerderartProJahr(
                    wohneinheitenProFoerderartProJahrList,
                    foerderart.getBezeichnung(),
                    baurate.getJahr().toString(),
                    wohneinheiten
                );
            }
        }
        return wohneinheitenProFoerderartProJahrList;
    }

    /**
     * Berechnet die Wohneinheiten für eine Baurate mit Angabe einer Förderart und eines SoBoN-Jahres.
     * Falls die Baurate Wohneinheiten hat, werden diese mit dem prozentualen Anteil der Förderart multipliziert.
     * Falls die Baurate nur Geschossfläche hat, wird diese zuerst anhand der Stammdaten des SoBoN-Jahres in Wohneinheiten umgerechnet und dann multipliziert.
     * Falls die Baurate weder Wohneinheiten noch Geschossfläche hat, wird 0 zurückgegeben.
     * Sollte nur mit Förderarten verwendet werden, die nicht umgelegt werden müssen.
     *
     * @param baurate Das {@link BaurateModel}, für welches die Wohneinheiten berechnet werden sollen.
     * @param foerderart Das {@link FoerderartModel}, das für die Berechnung angewandt werden soll.
     * @param sobonJahr Das {@link SobonOrientierungswertJahr}, das für die Berechnung angewandt werden soll.
     * @return Eine Anzahl von Wohneinheiten als BigDecimal.
     */
    protected BigDecimal calculateWohneinheiten(
        final BaurateModel baurate,
        final FoerderartModel foerderart,
        final SobonOrientierungswertJahr sobonJahr
    ) {
        if (baurate.getWeGeplant() != null) {
            return BigDecimal
                .valueOf(baurate.getWeGeplant())
                .multiply(foerderart.getAnteilProzent().scaleByPowerOfTen(-2));
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
                    .multiply(foerderart.getAnteilProzent().scaleByPowerOfTen(-2))
                    .divide(average, CalculationService.DIVISION_SCALE, RoundingMode.HALF_UP);
            }
        }

        return BigDecimal.ZERO;
    }

    protected void mergeWohneinheitenProFoerderartProJahr(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheitenProFoerderartProJahrList,
        final String foerderart,
        final String jahr,
        final BigDecimal wohneinheiten
    ) {
        final var wohneinheitenProFoerderartProJahrOptional = wohneinheitenProFoerderartProJahrList
            .stream()
            .filter(wohneinheitenProFoerderartProJahr ->
                wohneinheitenProFoerderartProJahr.getFoerderart().equals(foerderart) &&
                wohneinheitenProFoerderartProJahr.getJahr().equals(jahr)
            )
            .findAny();

        if (wohneinheitenProFoerderartProJahrOptional.isEmpty()) {
            wohneinheitenProFoerderartProJahrList.add(
                new WohneinheitenProFoerderartProJahrModel(foerderart, jahr, wohneinheiten)
            );
        } else {
            wohneinheitenProFoerderartProJahrOptional
                .get()
                .setWohneinheiten(
                    wohneinheitenProFoerderartProJahrOptional.get().getWohneinheiten().add(wohneinheiten)
                );
        }
    }
}
