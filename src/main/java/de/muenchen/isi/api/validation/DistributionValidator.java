package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

public class DistributionValidator {

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
