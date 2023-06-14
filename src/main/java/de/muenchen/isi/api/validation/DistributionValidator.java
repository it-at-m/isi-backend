package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
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
     * @param abfragevariante zur Extraktion der nichttechnischen Baugebiete.
     * @return die Liste an nichttechnscieh Baugebieten der Abfragevariante identifiziert über {@link BaugebietDto#getTechnical()}.
     */
    public List<BaugebietDto> getNonTechnicalBaugebiete(final AbfragevarianteAngelegtDto abfragevariante) {
        return CollectionUtils
            .emptyIfNull(abfragevariante.getBauabschnitte())
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
     * @param abfragevariante zur Extraktion der Baurante von technischen Baugebieten.
     * @return die Liste an Bauraten aller technischen Baugebiete der Abfragevariante identifiziert über {@link BaugebietDto#getTechnical()}.
     */
    public List<BaurateDto> getBauratenFromAllTechnicalBaugebiete(final AbfragevarianteAngelegtDto abfragevariante) {
        return CollectionUtils
            .emptyIfNull(abfragevariante.getBauabschnitte())
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .filter(baugebiet -> BooleanUtils.isTrue(baugebiet.getTechnical()))
            .flatMap(baugebiet -> CollectionUtils.emptyIfNull(baugebiet.getBauraten()).stream())
            .collect(Collectors.toList());
    }
}
