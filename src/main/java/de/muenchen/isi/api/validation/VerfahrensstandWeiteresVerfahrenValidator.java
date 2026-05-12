package de.muenchen.isi.api.validation;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class VerfahrensstandWeiteresVerfahrenValidator
    implements ConstraintValidator<VerfahrensstandWeiteresVerfahrenValid, Verfahrensstand>
{

    @Override
    public boolean isValid(final Verfahrensstand value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return Verfahrensstand.getVerfahrensstandForWeiteresVerfahren().contains(value);
    }
}
