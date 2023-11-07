package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

public class DistributionValidator {

    /**
     *
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
     * - {@link BaugebietDto#technical}
     * - {@link BauabschnittDto#technical}
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
     * - {@link BaugebietDto#technical}
     * - {@link BauabschnittDto#technical}
     *
     * @param bauabschnitte zur Extraktion der Baurante von technischen Baugebieten.
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
