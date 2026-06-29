package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageAngelegt.BauleitplanverfahrenAngelegtDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class Start42VerfahrenValidator
    implements ConstraintValidator<Start42VerfahrenValid, BauleitplanverfahrenAngelegtDto>
{

    /**
     * Prüft, dass entweder {@link BauleitplanverfahrenAngelegtDto#getStart42Verfahren()} gesetzt
     * oder {@link BauleitplanverfahrenAngelegtDto#getStart42VerfahrenDatumUnbekannt()} ausgewählt ist,
     * jedoch nicht beides oder keines von beiden.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return true falls genau eines von beiden gesetzt ist, ansonsten false.
     */
    @Override
    public boolean isValid(final BauleitplanverfahrenAngelegtDto value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        final boolean hasDatum = value.getStart42Verfahren() != null;
        final boolean datumUnbekannt = Boolean.TRUE.equals(value.getStart42VerfahrenDatumUnbekannt());
        return hasDatum ^ datumUnbekannt;
    }
}
