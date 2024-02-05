package de.muenchen.isi.api.validation;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class StandVerfahrenWeiteresVerfahrenValidator
    implements ConstraintValidator<StandVerfahrenWeiteresVerfahrenValid, StandVerfahren> {

    @Override
    public boolean isValid(final StandVerfahren value, final ConstraintValidatorContext context) {
        if (ObjectUtils.isEmpty(value)) {
            return true;
        }
        return StandVerfahren.getStandVerfahrenForWeiteresVerfahren().contains(value);
    }
}
