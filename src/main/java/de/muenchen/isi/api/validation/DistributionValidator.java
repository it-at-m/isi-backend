package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class DistributionValidator {

    /**
     * @param bauabschnitte zur Validierung.
     * @param weGesamt zur Validierung
     * @return true, falls die Anzahl der Wohneinheiten der Summe der Wohneinheiten in den nicht technischen Baugebieten entspricht.
     * True, falls die Anzahl der Wohneinheiten der Summe der Wohneinheiten in den Bauraten für technische Baugebiete entspricht.
     * True, falls keine Baugebiete vorhanden sind.
     * Andernfalls false.
     */
    public boolean isWohneinheitenDistributionValid(final List<BauabschnittDto> bauabschnitte, final Integer weGesamt) {
        boolean isValid = true;

        final List<BaugebietDto> nonTechnicalBaugebiete = getNonTechnicalBaugebiete(bauabschnitte);
        final List<BaurateDto> bauratenFromAllTechnicalBaugebiete = getBauratenFromAllTechnicalBaugebiete(
            bauabschnitte
        );

        final boolean containsNonTechnicalBaugebiet = CollectionUtils.isNotEmpty(nonTechnicalBaugebiete);
        final boolean containsBauratenInTechnicalBaugebiet = CollectionUtils.isNotEmpty(
            bauratenFromAllTechnicalBaugebiete
        );

        final var wohneinheiten = ObjectUtils.isEmpty(weGesamt) ? 0 : weGesamt;

        if (containsNonTechnicalBaugebiet) {
            final var sumVerteilteWohneinheitenBaugebiete = nonTechnicalBaugebiete
                .stream()
                .map(baugebiet -> ObjectUtils.isEmpty(baugebiet.getWeGeplant()) ? 0 : baugebiet.getWeGeplant())
                .reduce(0, Integer::sum);

            isValid = wohneinheiten == sumVerteilteWohneinheitenBaugebiete;
        } else if (containsBauratenInTechnicalBaugebiet) {
            final var sumVerteilteWohneinheitenBauraten = bauratenFromAllTechnicalBaugebiete
                .stream()
                .map(baurate -> ObjectUtils.isEmpty(baurate.getWeGeplant()) ? 0 : baurate.getWeGeplant())
                .reduce(0, Integer::sum);

            isValid = NumberUtils.compare(wohneinheiten, sumVerteilteWohneinheitenBauraten) == 0;
        }
        return isValid;
    }

    /**
     * @param bauabschnitte zur Validierung.
     * @param gfWohnenGesamt zur Validierung
     * @return true, falls die Geschossfläche Wohnen der Summe der Geschossfläche Wohnen in den nicht technischen Baugebieten entspricht.
     * True, falls die Geschossfläche Wohnen der Summe der Geschossfläche Wohnen in den Bauraten für technische Baugebiete entspricht.
     * True, falls keine Baugebiete vorhanden sind.
     * Andernfalls false.
     */
    public boolean isGeschossflaecheWohnenDistributionValid(
        final List<BauabschnittDto> bauabschnitte,
        final BigDecimal gfWohnenGesamt
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

        final var geschossflaecheWohnen = ObjectUtils.isEmpty(gfWohnenGesamt) ? BigDecimal.ZERO : gfWohnenGesamt;

        if (containsNonTechnicalBaugebiet) {
            final var sumVerteilteGeschossflaecheWohnenBaugebiete = nonTechnicalBaugebiete
                .stream()
                .map(baugebiet ->
                    ObjectUtils.isEmpty(baugebiet.getGfWohnenGeplant())
                        ? BigDecimal.ZERO
                        : baugebiet.getGfWohnenGeplant()
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            isValid = sumVerteilteGeschossflaecheWohnenBaugebiete.compareTo(geschossflaecheWohnen) == 0;
        } else if (containsBauratenInTechnicalBaugebiet) {
            final var sumVerteilteGeschossflaecheWohnenBauraten = bauratenFromAllTechnicalBaugebiete
                .stream()
                .map(baurate ->
                    ObjectUtils.isEmpty(baurate.getGfWohnenGeplant()) ? BigDecimal.ZERO : baurate.getGfWohnenGeplant()
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            isValid = sumVerteilteGeschossflaecheWohnenBauraten.compareTo(geschossflaecheWohnen) == 0;
        }
        return isValid;
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
        return CollectionUtils
            .emptyIfNull(bauabschnitte)
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
        return CollectionUtils
            .emptyIfNull(bauabschnitte)
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .filter(baugebiet -> BooleanUtils.isTrue(baugebiet.getTechnical()))
            .flatMap(baugebiet -> CollectionUtils.emptyIfNull(baugebiet.getBauraten()).stream())
            .collect(Collectors.toList());
    }
}
