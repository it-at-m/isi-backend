package de.muenchen.isi.api.validation;

import de.muenchen.isi.infrastructure.entity.stammdaten.baurate.IdealtypischeBaurate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@NoArgsConstructor
public class RangeIdealtypischeBaurateValidator
    implements ConstraintValidator<RangeIdealtypischeBaurateValid, IdealtypischeBaurate> {

    @Override
    public boolean isValid(final IdealtypischeBaurate value, final ConstraintValidatorContext context) {
        if (ObjectUtils.isEmpty(value.getVon()) || ObjectUtils.isEmpty(value.getBisExklusiv())) {
            return false;
        } else {
            return value.getVon().compareTo(value.getBisExklusiv()) < 0;
        }
    }
}
