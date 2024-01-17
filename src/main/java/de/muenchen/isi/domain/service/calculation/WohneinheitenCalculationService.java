package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.model.calculation.WohneinheitenProFoerderartProJahrModel;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

@Data
public abstract class WohneinheitenCalculationService {

    /**
     * Die Methode summiert die im Parameter gegebenen Wohneinheiten für jede Förderart  über den im Parameter gegebenen Zeitraum.
     *
     * @param wohneinheitenProJahrToSum zum Summieren.
     * @param numberOfYears als Anzahl der Jahre welche summiert werden sollen.
     * @return die über die Jahre summierten Wohneinheiten je Förderart.
     */
    public List<WohneinheitenProFoerderartProJahrModel> sumWohneinheitenForNumberOfYearsForEachFoerderart(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheitenProJahrToSum,
        final int numberOfYears
    ) {
        return wohneinheitenProJahrToSum
            .stream()
            .collect(
                Collectors.groupingBy(
                    WohneinheitenProFoerderartProJahrModel::getFoerderart,
                    Collectors.mapping(Function.identity(), Collectors.toList())
                )
            )
            .values()
            .stream()
            .map(wohneinheitenProJahrForFoerderart ->
                sumWohneinheitenForNumberOfYears(wohneinheitenProJahrForFoerderart, numberOfYears)
            )
            .collect(Collectors.toList());
    }

    /**
     * Die Methode summiert die im Parameter gegebenen Wohneinheiten über den im Parameter gegebenen Zeitraum.
     *
     * @param wohneinheitenProJahrToSum zum Summieren.
     * @param numberOfYears als Anzahl der Jahre welche summiert werden sollen.
     * @return die über die Jahre summierten Wohneinheiten.
     */
    protected WohneinheitenProFoerderartProJahrModel sumWohneinheitenForNumberOfYears(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheitenProJahrToSum,
        final int numberOfYears
    ) {
        wohneinheitenProJahrToSum.sort(Comparator.comparingInt(a -> Integer.parseInt(a.getJahr())));
        final var firstYear = Integer.parseInt(wohneinheitenProJahrToSum.get(0).getJahr());
        final var summedWohneinheitenProJahr = new WohneinheitenProFoerderartProJahrModel();
        summedWohneinheitenProJahr.setJahr(String.format(CalculationService.SUMMATION_PERIOD_NAME, numberOfYears));
        summedWohneinheitenProJahr.setWohneinheiten(BigDecimal.ZERO);
        for (final var wohneinheiten : wohneinheitenProJahrToSum) {
            if (Integer.parseInt(wohneinheiten.getJahr()) <= firstYear + numberOfYears - 1) {
                summedWohneinheitenProJahr.setFoerderart(wohneinheiten.getFoerderart());
                final var sum = summedWohneinheitenProJahr
                    .getWohneinheiten()
                    .add(ObjectUtils.defaultIfNull(wohneinheiten.getWohneinheiten(), BigDecimal.ZERO));
                summedWohneinheitenProJahr.setWohneinheiten(sum);
            }
        }
        return summedWohneinheitenProJahr;
    }

    /**
     * Die Methode summiert je Jahr über alle Förderarten.
     *
     * @param wohneinheitenProJahrToSum zum summieren.
     * @return Du Summe je Jahr über alle Förderarten.
     */
    public List<WohneinheitenProFoerderartProJahrModel> sumWohneinheitenOverFoerderartenForEachYear(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheitenProJahrToSum
    ) {
        return wohneinheitenProJahrToSum
            .stream()
            .collect(
                Collectors.groupingBy(
                    WohneinheitenProFoerderartProJahrModel::getJahr,
                    Collectors.mapping(Function.identity(), Collectors.toList())
                )
            )
            .values()
            .stream()
            .map(this::sumWohneinheiten)
            .collect(Collectors.toList());
    }

    /**
     * Die Methode summiert die Wohneinheiten der gegebenen Objekte.
     *
     * @param wohneinheitenProJahrToSum zum Summieren.
     * @return die Summe der Wohneinheiten für die gegebenen Objekte.
     */
    protected WohneinheitenProFoerderartProJahrModel sumWohneinheiten(
        final List<WohneinheitenProFoerderartProJahrModel> wohneinheitenProJahrToSum
    ) {
        final var summedWohneinheiten = new WohneinheitenProFoerderartProJahrModel();
        summedWohneinheiten.setFoerderart(CalculationService.SUMMATION_TOTAL_NAME);
        summedWohneinheiten.setWohneinheiten(BigDecimal.ZERO);
        for (final var wohneinheiten : wohneinheitenProJahrToSum) {
            summedWohneinheiten.setJahr(wohneinheiten.getJahr());
            final var sum = summedWohneinheiten
                .getWohneinheiten()
                .add(ObjectUtils.defaultIfNull(wohneinheiten.getWohneinheiten(), BigDecimal.ZERO));
            summedWohneinheiten.setWohneinheiten(sum);
        }
        return summedWohneinheiten;
    }
}
