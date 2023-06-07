package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class WohneinheitenDistributionValidator
    implements ConstraintValidator<WohneinheitenDistributionValid, AbfrageerstellungAbfragevarianteAngelegtDto> {

    /**
     * Prüft, ob die Summe der über die Baugebiete verteilten Wohneinheiten der Anzahl der Wohneinheiten in der Abfragevariante entspricht.
     *
     * @param value als AbfragevarianteDto
     * @param context in welchem die Validierung stattfindet
     * @return true falls die Anzahl der Wohneinheiten in der Abfragevariante der Summe der Wohneinheiten in den Baugebieten entspricht oder falls keine Baugebiete vorhanden sind. Andernfalls false.
     */
    @Override
    public boolean isValid(
        final AbfrageerstellungAbfragevarianteAngelegtDto value,
        final ConstraintValidatorContext context
    ) {
        final boolean isValid;
        final Collection<BaugebietDto> baugebiete = CollectionUtils
            .emptyIfNull(value.getBauabschnitte())
            .stream()
            .flatMap(bauabschnittDto -> CollectionUtils.emptyIfNull(bauabschnittDto.getBaugebiete()).stream())
            .collect(Collectors.toList());
        if (baugebiete.isEmpty()) {
            isValid = true;
        } else {
            final Integer wohneinheitenAbfragevariante = ObjectUtils.isEmpty(value.getGesamtanzahlWe())
                ? 0
                : value.getGesamtanzahlWe();
            final Integer sumWohneinheitenBaugebiete = baugebiete
                .stream()
                .map(baugebietDto ->
                    ObjectUtils.isEmpty(baugebietDto.getGesamtanzahlWe()) ? 0 : baugebietDto.getGesamtanzahlWe()
                )
                .reduce(0, Integer::sum);
            isValid = wohneinheitenAbfragevariante.equals(sumWohneinheitenBaugebiete);
        }
        return isValid;
    }
}
