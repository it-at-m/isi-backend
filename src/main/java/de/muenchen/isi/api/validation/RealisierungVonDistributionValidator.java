package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.BaugebietDto;
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

    /**
     * @param value object to validate
     * @param context context in which the constraint is evaluate
     * @return true falls das Realisierungsjahr der Abfragevariante vor oder gleich der Realisierungsjahre der Baugebiete.
     */
    @Override
    public boolean isValid(final AbfragevarianteDto value, final ConstraintValidatorContext context) {
        final Optional<Integer> minJahrBaugebiet = CollectionUtils
            .emptyIfNull(value.getBauabschnitte())
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .map(BaugebietDto::getRealisierungVon)
            .min(Integer::compareTo);
        return minJahrBaugebiet.isEmpty() || value.getRealisierungVon() <= minJahrBaugebiet.get();
    }
}
