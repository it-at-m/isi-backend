package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

public class DistributionValidator {

    /**
     * @param abfragevariante zur Extraktion der nichttechnischen Baugebiete.
     * @return die Liste an nichttechnscieh Baugebieten der Abfragevariante identifiziert über {@link BaugebietDto#getTechnical()}.
     */
    public List<BaugebietDto> getNonTechnicalBaugebiete(
        final AbfrageerstellungAbfragevarianteAngelegtDto abfragevariante
    ) {
        return CollectionUtils
            .emptyIfNull(abfragevariante.getBauabschnitte())
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .filter(baugebiet -> BooleanUtils.isFalse(baugebiet.getTechnical()))
            .collect(Collectors.toList());
    }

    /**
     * @param abfragevariante zur Extraktion der Baurante von technischen Baugebieten.
     * @return die Liste an Bauraten aller technischen Baugebiete der Abfragevariante identifiziert über {@link BaugebietDto#getTechnical()}.
     */
    public List<BaurateDto> getBauratenFromAllTechnicalBaugebiete(
        final AbfrageerstellungAbfragevarianteAngelegtDto abfragevariante
    ) {
        return CollectionUtils
            .emptyIfNull(abfragevariante.getBauabschnitte())
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .filter(baugebiet -> BooleanUtils.isTrue(baugebiet.getTechnical()))
            .flatMap(baugebiet -> CollectionUtils.emptyIfNull(baugebiet.getBauraten()).stream())
            .collect(Collectors.toList());
    }
}
