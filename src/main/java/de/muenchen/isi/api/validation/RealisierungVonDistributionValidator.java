package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.BaurateDto;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class RealisierungVonDistributionValidator
    implements ConstraintValidator<RealisierungVonDistributionValid, AbfragevarianteDto> {

    @Override
    public boolean isValid(final AbfragevarianteDto value, final ConstraintValidatorContext context) {
        final Optional<Integer> minJahr = CollectionUtils
            .emptyIfNull(value.getBauabschnitte())
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .flatMap(baugebiet -> CollectionUtils.emptyIfNull(baugebiet.getBauraten()).stream())
            .map(BaurateDto::getJahr)
            .min(Integer::compareTo);
        return minJahr.isEmpty() || value.getRealisierungVon() <= minJahr.get();
    }
}
