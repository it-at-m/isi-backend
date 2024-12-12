package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class DistributionValidator {

    /**
     * Überprüft, ob die Verteilung der Wohneinheiten und Geschossfläche Wohnen auf Baugebiete bzw. Bauraten valide ist.
     * WE bzw. GF gelten als "verteilt", wenn bei mindestens einem Baugebiet bzw. Baurate eine Angabe zu ihnen gemacht wurde.
     * WE bzw. GF gelten als "korrekt verteilt", wenn zusätzlich ihre Summe in den Baugebieten bzw. Bauraten der Gesamtanzahl entspricht.
     * Die Verteilung ist valide, wenn WE und GF korrekt verteilt wurden.
     * Ebenso ist die Verteilung valide, wenn entweder WE oder GF korrekt verteilt wurde, aber der andere Wert nicht verteilt wurde.
     *
     * @param bauabschnitte zur Validierung.
     * @param weGesamt zur Validierung
     * @param gfWohnenGesamt zur Validierung
     * @return ob die Verteilung entsprechend der Beschreibung valide ist.
     */
    public boolean isWeGfDistributionValid(
        final List<BauabschnittDto> bauabschnitte,
        final Integer weGesamt,
        final BigDecimal gfWohnenGesamt
    ) {
        final var wohneinheiten = ObjectUtils.isEmpty(weGesamt) ? 0 : weGesamt;
        var wohneinheitenEqual = true;
        var allWohneinheitenEmpty = new AtomicBoolean(true);
        final var geschossflaecheWohnen = ObjectUtils.isEmpty(gfWohnenGesamt) ? BigDecimal.ZERO : gfWohnenGesamt;
        var geschossflaecheWohnenEqual = true;
        var allGeschossflaecheWohnenEmpty = new AtomicBoolean(true);

        final List<BaugebietDto> nonTechnicalBaugebiete = getNonTechnicalBaugebiete(bauabschnitte);
        final List<BaurateDto> bauratenFromAllTechnicalBaugebiete = getBauratenFromAllTechnicalBaugebiete(
            bauabschnitte
        );

        final boolean containsNonTechnicalBaugebiet = CollectionUtils.isNotEmpty(nonTechnicalBaugebiete);
        final boolean containsBauratenInTechnicalBaugebiet = CollectionUtils.isNotEmpty(
            bauratenFromAllTechnicalBaugebiete
        );

        /*
        Unterscheidung zwischen technischen und nicht-technischen Baugebieten, da technische Baugebiete keine Daten enthalten sollen.
        Bei ihnen werden stattdessen die untergeordneten Bauraten als Datenquelle hergenommen.
        */
        if (containsNonTechnicalBaugebiet) {
            final var sumVerteilteWohneinheitenBaugebiete = nonTechnicalBaugebiete
                .stream()
                .map(baugebiet -> {
                    if (ObjectUtils.isNotEmpty(baugebiet.getWeGeplant())) {
                        allWohneinheitenEmpty.set(false);
                        return baugebiet.getWeGeplant();
                    }
                    return 0;
                })
                .reduce(0, Integer::sum);
            wohneinheitenEqual = NumberUtils.compare(wohneinheiten, sumVerteilteWohneinheitenBaugebiete) == 0;

            final var sumVerteilteGeschossflaecheWohnenBaugebiete = nonTechnicalBaugebiete
                .stream()
                .map(baugebiet -> {
                    if (ObjectUtils.isNotEmpty(baugebiet.getGfWohnenGeplant())) {
                        allGeschossflaecheWohnenEmpty.set(false);
                        return baugebiet.getGfWohnenGeplant();
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            geschossflaecheWohnenEqual =
                sumVerteilteGeschossflaecheWohnenBaugebiete.compareTo(geschossflaecheWohnen) == 0;
        } else if (containsBauratenInTechnicalBaugebiet) {
            final var sumVerteilteWohneinheitenBauraten = bauratenFromAllTechnicalBaugebiete
                .stream()
                .map(baurate -> {
                    if (ObjectUtils.isNotEmpty(baurate.getWeGeplant())) {
                        allWohneinheitenEmpty.set(false);
                        return baurate.getWeGeplant();
                    }
                    return 0;
                })
                .reduce(0, Integer::sum);
            wohneinheitenEqual = NumberUtils.compare(wohneinheiten, sumVerteilteWohneinheitenBauraten) == 0;

            final var sumVerteilteGeschossflaecheWohnenBauraten = bauratenFromAllTechnicalBaugebiete
                .stream()
                .map(baurate -> {
                    if (ObjectUtils.isNotEmpty(baurate.getGfWohnenGeplant())) {
                        allGeschossflaecheWohnenEmpty.set(false);
                        return baurate.getGfWohnenGeplant();
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            geschossflaecheWohnenEqual =
                sumVerteilteGeschossflaecheWohnenBauraten.compareTo(geschossflaecheWohnen) == 0;
        } else {
            return true;
        }

        final var wohneinheitenCorrect = wohneinheitenEqual && !allWohneinheitenEmpty.get();
        final var geschossflaecheWohnenCorrect = geschossflaecheWohnenEqual && !allGeschossflaecheWohnenEmpty.get();

        return (
            (wohneinheitenCorrect && geschossflaecheWohnenCorrect) ||
            (wohneinheitenCorrect && allGeschossflaecheWohnenEmpty.get()) ||
            (allWohneinheitenEmpty.get() && geschossflaecheWohnenCorrect)
        );
    }

    /**
     * @param bauabschnitte zur Validierung.
     * @param realisierungVon zur Validierung
     * @return true falls das Realisierungsjahr vor oder gleich der Realisierungsjahre der in den Bauabschnitten enthaltenen Baugebiete oder einer Baurate ist.
     */
    public boolean isRealisierungVonDistributionValid(
        final List<BauabschnittDto> bauabschnitte,
        final Integer realisierungVon
    ) {
        boolean isValid = true;

        final List<BaugebietDto> nonTechnicalBaugebiete = getNonTechnicalBaugebiete(bauabschnitte);
        final List<BaurateDto> bauratenFromAllTechnicalBaugebiete = getBauratenFromAllTechnicalBaugebiete(
            bauabschnitte
        );

        final boolean containsNonTechnicalBaugebiet = CollectionUtils.isNotEmpty(nonTechnicalBaugebiete);
        final boolean containsBauratenInTechnicalBaugebiet = CollectionUtils.isNotEmpty(
            bauratenFromAllTechnicalBaugebiete
        );

        /*
        Unterscheidung zwischen technischen und nicht-technischen Baugebieten, da technische Baugebiete keine Daten enthalten sollen.
        Bei ihnen werden stattdessen die untergeordneten Bauraten als Datenquelle hergenommen.
        */
        if (containsNonTechnicalBaugebiet) {
            final Optional<Integer> minJahrBaugebiete = nonTechnicalBaugebiete
                .stream()
                .map(BaugebietDto::getRealisierungVon)
                .filter(Objects::nonNull)
                .min(Integer::compareTo);

            isValid = minJahrBaugebiete.isEmpty() || realisierungVon.compareTo(minJahrBaugebiete.get()) <= 0;
        } else if (containsBauratenInTechnicalBaugebiet) {
            final Optional<Integer> minJahrBauraten = bauratenFromAllTechnicalBaugebiete
                .stream()
                .map(BaurateDto::getJahr)
                .filter(Objects::nonNull)
                .min(Integer::compareTo);

            isValid = minJahrBauraten.isEmpty() || realisierungVon.compareTo(minJahrBauraten.get()) <= 0;
        }
        return isValid;
    }

    /**
     * Dokumentation bezüglich Verwendung technischer und nicht technischer Baugebiete und Bauabschnitte siehe:
     * - {@link BaugebietDto#getTechnical()}
     * - {@link BauabschnittDto#getTechnical}
     *
     * @param bauabschnitte zur Extraktion der nichttechnischen Baugebiete.
     * @return die Liste an nichttechnischen Baugebieten identifiziert über {@link BaugebietDto#getTechnical()}.
     */
    public List<BaugebietDto> getNonTechnicalBaugebiete(final List<BauabschnittDto> bauabschnitte) {
        return CollectionUtils.emptyIfNull(bauabschnitte)
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .filter(baugebiet -> BooleanUtils.isFalse(baugebiet.getTechnical()))
            .collect(Collectors.toList());
    }

    /**
     * Dokumentation bezüglich Verwendung technischer und nicht technischer Baugebiete und Bauabschnitte siehe:
     * - {@link BaugebietDto#getTechnical}
     * - {@link BauabschnittDto#getTechnical}
     *
     * @param bauabschnitte zur Extraktion der Bauraten von technischen Baugebieten.
     * @return die Liste an Bauraten aller technischen Baugebiete identifiziert über {@link BaugebietDto#getTechnical()}.
     */
    public List<BaurateDto> getBauratenFromAllTechnicalBaugebiete(final List<BauabschnittDto> bauabschnitte) {
        return CollectionUtils.emptyIfNull(bauabschnitte)
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .filter(baugebiet -> BooleanUtils.isTrue(baugebiet.getTechnical()))
            .flatMap(baugebiet -> CollectionUtils.emptyIfNull(baugebiet.getBauraten()).stream())
            .collect(Collectors.toList());
    }
}
