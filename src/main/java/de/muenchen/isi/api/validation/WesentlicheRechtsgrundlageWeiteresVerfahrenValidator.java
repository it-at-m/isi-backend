package de.muenchen.isi.api.validation;

import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class WesentlicheRechtsgrundlageWeiteresVerfahrenValidator
    implements ConstraintValidator<WesentlicheRechtsgrundlageWeiteresVerfahrenValid, WesentlicheRechtsgrundlage>
{

    @Override
    public boolean isValid(final WesentlicheRechtsgrundlage value, final ConstraintValidatorContext context) {
        if (ObjectUtils.isEmpty(value)) {
            return true;
        }
        return WesentlicheRechtsgrundlage.getWesentlicheRechtsgrundlageForWeiteresVerfahren().contains(value);
    }
}
