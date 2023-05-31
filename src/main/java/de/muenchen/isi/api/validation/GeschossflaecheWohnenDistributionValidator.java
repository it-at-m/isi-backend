package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import java.math.BigDecimal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class GeschossflaecheWohnenDistributionValidator
    implements ConstraintValidator<GeschossflaecheWohnenDistributionValid, AbfragevarianteDto> {

    /**
     * Prüft, ob die Summe der über die Baugebiete verteilten Geschossfläche Wohnen der Geschossfläche Wohnen in der Abfragevariante entspricht.
     *
     * @param value als AbfragevarianteDto
     * @param context in welchem die Validierung stattfindet
     * @return true falls die Geschossfläche Wohnen in der Abfragevariante der Summe der Geschossfläche Wohnen in den Baugebieten entspricht oder falls keine Baugebiete vorhanden sind. Andernfalls false.
     */
    @Override
    public boolean isValid(final AbfragevarianteDto value, final ConstraintValidatorContext context) {
        final BigDecimal geschossflaecheWohnenAbfragevariante = ObjectUtils.isEmpty(value.getGeschossflaecheWohnen())
            ? BigDecimal.ZERO
            : value.getGeschossflaecheWohnen();
        final BigDecimal sumGeschossflaecheWohnenBaugebiete = CollectionUtils
            .emptyIfNull(value.getBauabschnitte())
            .stream()
            .flatMap(bauabschnittDto -> CollectionUtils.emptyIfNull(bauabschnittDto.getBaugebiete()).stream())
            .map(baugebietDto ->
                ObjectUtils.isEmpty(baugebietDto.getGeschossflaecheWohnen())
                    ? BigDecimal.ZERO
                    : baugebietDto.getGeschossflaecheWohnen()
            )
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return geschossflaecheWohnenAbfragevariante.compareTo(sumGeschossflaecheWohnenBaugebiete) == 0;
    }
}
