package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class WeGfDistributionBaugenehmigungsverfahrenValidator
    extends DistributionValidator
    implements
        ConstraintValidator<
            WeGfDistributionBaugenehmigungsverfahrenValid,
            AbfragevarianteBaugenehmigungsverfahrenAngelegtDto
        >
{

    /**
     * Siehe {@link DistributionValidator#isWeGfDistributionValid}.
     *
     * @param value als AbfragevarianteDto
     * @param context in welchem die Validierung stattfindet
     * @return ob die Verteilung valide ist
     */
    @Override
    public boolean isValid(
        final AbfragevarianteBaugenehmigungsverfahrenAngelegtDto value,
        final ConstraintValidatorContext context
    ) {
        return this.isWeGfDistributionValid(value.getBauabschnitte(), value.getWeGesamt(), value.getGfWohnenGesamt());
    }
}
