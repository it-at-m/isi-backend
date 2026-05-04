package de.muenchen.isi.api.validation;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class VerfahrensstandBauleitplanverfahrenValidator
    implements ConstraintValidator<VerfahrensstandBauleitplanverfahrenValid, Verfahrensstand>
{

    @Override
    public boolean isValid(final Verfahrensstand value, final ConstraintValidatorContext context) {
        if (ObjectUtils.isEmpty(value)) {
            return true;
        }
        return Verfahrensstand.getVerfahrensstandForBauleitplanverfahren().contains(value);
    }
}
