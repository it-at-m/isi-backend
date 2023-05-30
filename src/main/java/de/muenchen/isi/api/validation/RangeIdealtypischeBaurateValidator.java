package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.stammdaten.baurate.IdealtypischeBaurateDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@NoArgsConstructor
public class RangeIdealtypischeBaurateValidator
    implements ConstraintValidator<RangeIdealtypischeBaurateValid, IdealtypischeBaurateDto> {

    /**
     * Prüft ob die Attribute {@link IdealtypischeBaurateDto#von} und {@link IdealtypischeBaurateDto#bisExklusiv}
     * definiert sind und ob der Attributwert für {@link IdealtypischeBaurateDto#von}
     * kleiner als der Attributwert für {@link IdealtypischeBaurateDto#bisExklusiv} ist.
     *
     * @param value der idealtypischen Baurate
     * @param context in welchem die Validierung stattfindet
     *
     * @return true falls der Attributwert für {@link IdealtypischeBaurateDto#von}
     * kleiner als der Attributwert für {@link IdealtypischeBaurateDto#bisExklusiv} ist.
     * Andernfalls wird false zurückgegeben.
     */
    @Override
    public boolean isValid(final IdealtypischeBaurateDto value, final ConstraintValidatorContext context) {
        if (ObjectUtils.isEmpty(value.getVon()) || ObjectUtils.isEmpty(value.getBisExklusiv())) {
            return false;
        } else {
            return value.getVon().compareTo(value.getBisExklusiv()) < 0;
        }
    }
}
