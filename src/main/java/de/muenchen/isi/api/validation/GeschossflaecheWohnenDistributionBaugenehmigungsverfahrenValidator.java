package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class GeschossflaecheWohnenDistributionBaugenehmigungsverfahrenValidator
    extends DistributionValidator
    implements
        ConstraintValidator<
            GeschossflaecheWohnenDistributionBaugenehmigungsverfahrenValid,
            AbfragevarianteBaugenehmigungsverfahrenAngelegtDto
        > {

    /**
     * Validiert für die im Parameter gegebene Abfragevariante die über Baugebiete bzw. Bauraten verteilte Geschossfläche Wohnen.
     *
     * @param value als AbfragevarianteDto
     * @param context in welchem die Validierung stattfindet
     * @return true, falls die Geschossfläche Wohnen in der Abfragevariante der Summe der Geschossfläche Wohnen in den nicht technischen Baugebieten entspricht.
     * True, falls die Geschossfläche Wohnen in der Abfragevariante der Summe der Geschossfläche Wohnen in den Bauraten für technische Baugebiete entspricht.
     * True, falls keine Baugebiete vorhanden sind.
     * Andernfalls false.
     */
    @Override
    public boolean isValid(
        final AbfragevarianteBaugenehmigungsverfahrenAngelegtDto value,
        final ConstraintValidatorContext context
    ) {
        return (
            ObjectUtils.isNotEmpty(value.getWeGesamt()) ||
            isGeschossflaecheWohnenDistributionValid(value.getBauabschnitte(), value.getGfWohnenGesamt())
        );
    }
}
