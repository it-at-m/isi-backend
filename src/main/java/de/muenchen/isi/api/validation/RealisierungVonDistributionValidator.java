package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.BaurateDto;
import java.util.Optional;
import java.util.function.Predicate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
        final Optional<Integer> minJahrBaugebiete = CollectionUtils
            .emptyIfNull(value.getBauabschnitte())
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .filter(Predicate.not(BaugebietDto::getTechnical))
            .map(BaugebietDto::getRealisierungVon)
            .min(Integer::compareTo);

        final Optional<Integer> minJahrBauraten = CollectionUtils
            .emptyIfNull(value.getBauabschnitte())
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .filter(BaugebietDto::getTechnical)
            .flatMap(baugebiet -> CollectionUtils.emptyIfNull(baugebiet.getBauraten()).stream())
            .map(BaurateDto::getJahr)
            .min(Integer::compareTo);

        final boolean isValid;
        if (minJahrBaugebiete.isEmpty() && minJahrBauraten.isEmpty()) {
            isValid = true;
        } else if (minJahrBaugebiete.isPresent() && minJahrBauraten.isEmpty()) {
            isValid = value.getRealisierungVon() <= minJahrBaugebiete.get();
        } else if (minJahrBaugebiete.isEmpty() && minJahrBauraten.isPresent()) {
            isValid = value.getRealisierungVon() <= minJahrBauraten.get();
        } else {
            isValid = value.getRealisierungVon() <= NumberUtils.min(minJahrBaugebiete.get(), minJahrBauraten.get());
        }
        return isValid;
    }
}
