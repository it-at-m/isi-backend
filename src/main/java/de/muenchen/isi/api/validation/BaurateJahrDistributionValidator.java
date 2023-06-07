package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import java.util.Objects;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class BaurateJahrDistributionValidator
    implements ConstraintValidator<BaurateJahrDistributionValid, BaugebietDto> {

    /**
     *
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     * @return true falls das Realisierungsjahr des  nicht-technischen Baugebiets vor oder gleich der Realisierungsjahre der Bauraten ist.
     */
    @Override
    public boolean isValid(final BaugebietDto value, final ConstraintValidatorContext context) {
        final Optional<Integer> minJahrBauraten = CollectionUtils
            .emptyIfNull(value.getBauraten())
            .stream()
            .map(BaurateDto::getJahr)
            .filter(Objects::nonNull)
            .min(Integer::compareTo);
        return (
            BooleanUtils.isNotFalse(value.getTechnical()) ||
            minJahrBauraten.isEmpty() ||
            value.getRealisierungVon() <= minJahrBauraten.get()
        );
    }
}
