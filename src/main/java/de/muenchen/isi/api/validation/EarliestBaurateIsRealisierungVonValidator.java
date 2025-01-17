package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteAngelegtDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class EarliestBaurateIsRealisierungVonValidator
    implements ConstraintValidator<EarliestBaurateIsRealisierungVonValid, AbfragevarianteAngelegtDto> {

    /**
     * Prüft ob das Jahr der frühesten Baurate mit dem Attribut {@link AbfragevarianteAngelegtDto#getRealisierungVon()} übereinstimmt.
     *
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return false falls das Attribut {@link AbfragevarianteAngelegtDto#getRealisierungVon()} nicht mit einer vorhandenen frühesten Baurate übereinstimmt, anderfalls true.
     */
    @Override
    public boolean isValid(final AbfragevarianteAngelegtDto value, final ConstraintValidatorContext context) {
        final var yearOfEarliestBaurate = CollectionUtils.emptyIfNull(value.getBauabschnitte())
            .stream()
            .flatMap(bauabschnitt -> CollectionUtils.emptyIfNull(bauabschnitt.getBaugebiete()).stream())
            .flatMap(baugebiet -> CollectionUtils.emptyIfNull(baugebiet.getBauraten()).stream())
            .map(BaurateDto::getJahr)
            .filter(Objects::nonNull)
            .min(Integer::compareTo);
        if (ObjectUtils.isNotEmpty(value.getRealisierungVon()) && yearOfEarliestBaurate.isPresent()) {
            return Objects.equals(yearOfEarliestBaurate.get(), value.getRealisierungVon());
        } else {
            return true;
        }
    }
}
