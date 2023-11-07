package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class WohneinheitenDistributionBauleitplanverfahrenValidator
    extends DistributionValidator
    implements
        ConstraintValidator<
            WohneinheitenDistributionBauleitplanverfahrenValid,
            AbfragevarianteBauleitplanverfahrenAngelegtDto
        > {

    /**
     * Validiert für die im Parameter gegebene Abfragevariante die über Baugebiete bzw. Bauraten verteilten Wohneinheiten.
     *
     * @param value als AbfragevarianteDto
     * @param context in welchem die Validierung stattfindet
     * @return true, falls die Anzahl der Wohneinheiten in der Abfragevariante der Summe der Wohneinheiten in den nicht technischen Baugebieten entspricht.
     * True, falls die Anzahl der Wohneinheiten in der Abfragevariante der Summe der Wohneinheiten in den Bauraten für technische Baugebiete entspricht.
     * True, falls keine Baugebiete vorhanden sind.
     * Andernfalls false.
     */
    @Override
    public boolean isValid(
        final AbfragevarianteBauleitplanverfahrenAngelegtDto value,
        final ConstraintValidatorContext context
    ) {
        return this.isWohneinheitenDistributionValid(value.getBauabschnitte(), value.getWeGesamt());
    }
}
