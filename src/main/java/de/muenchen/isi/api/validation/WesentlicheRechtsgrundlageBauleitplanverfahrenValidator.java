package de.muenchen.isi.api.validation;

import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class WesentlicheRechtsgrundlageBauleitplanverfahrenValidator
    implements ConstraintValidator<WesentlicheRechtsgrundlageBauleitplanverfahrenValid, WesentlicheRechtsgrundlage> {

    @Override
    public boolean isValid(final WesentlicheRechtsgrundlage value, final ConstraintValidatorContext context) {
        if (ObjectUtils.isEmpty(value)) {
            return true;
        }
        return WesentlicheRechtsgrundlage.getWesentlicheRechtsgrundlageForBauleitplanverfahren().contains(value);
    }
}
