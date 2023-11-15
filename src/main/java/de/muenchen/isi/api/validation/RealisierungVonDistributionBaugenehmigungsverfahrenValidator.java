package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteBaugenehmigungsverfahrenAngelegtDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class RealisierungVonDistributionBaugenehmigungsverfahrenValidator
    extends DistributionValidator
    implements
        ConstraintValidator<
            RealisierungVonDistributionBaugenehmigungsverfahrenValid,
            AbfragevarianteBaugenehmigungsverfahrenAngelegtDto
        > {

    /**
     * @param value object to validate
     * @param context context in which the constraint is evaluate
     * @return true falls das Realisierungsjahr der Abfragevariante vor oder gleich der Realisierungsjahre der Baugebiete oder einer Baurate.
     */
    @Override
    public boolean isValid(
        final AbfragevarianteBaugenehmigungsverfahrenAngelegtDto value,
        final ConstraintValidatorContext context
    ) {
        return this.isRealisierungVonDistributionValid(value.getBauabschnitte(), value.getRealisierungVon());
    }
}
