package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.stammdaten.baurate.JahresrateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@NoArgsConstructor
public class JahresratenValidator implements ConstraintValidator<JahresratenValid, List<JahresrateDto>> {

    /**
     * Prüft, ob die Summe der Jahresraten den Wert 1 ergibt.
     *
     * @param value als Liste der Jahresraten
     * @param context in welchem die Validierung stattfindet
     *
     * @return true falls die Summe der Jahresraten den Wert 1 ergibt, ansonsten false
     */
    @Override
    public boolean isValid(final List<JahresrateDto> value, final ConstraintValidatorContext context) {
        final var sum = value
            .stream()
            .map(JahresrateDto::getRate)
            .map(rate -> ObjectUtils.isEmpty(rate) ? BigDecimal.ZERO : rate)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.compareTo(BigDecimal.ONE) == 0;
    }
}
