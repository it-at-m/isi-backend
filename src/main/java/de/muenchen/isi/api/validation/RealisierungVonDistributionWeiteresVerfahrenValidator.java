package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteWeiteresVerfahrenAngelegtDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class RealisierungVonDistributionWeiteresVerfahrenValidator
    extends DistributionValidator
    implements
        ConstraintValidator<
            RealisierungVonDistributionWeiteresVerfahrenValid,
            AbfragevarianteWeiteresVerfahrenAngelegtDto
        > {

    /**
     * @param value object to validate
     * @param context context in which the constraint is evaluate
     * @return true falls das Realisierungsjahr der Abfragevariante vor oder gleich der Realisierungsjahre der Baugebiete oder einer Baurate.
     */
    @Override
    public boolean isValid(
        final AbfragevarianteWeiteresVerfahrenAngelegtDto value,
        final ConstraintValidatorContext context
    ) {
        return this.isRealisierungVonDistributionValid(value.getBauabschnitte(), value.getRealisierungVon());
    }
}
