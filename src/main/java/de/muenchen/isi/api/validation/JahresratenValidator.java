package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.stammdaten.baurate.JahresrateDto;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@NoArgsConstructor
public class JahresratenValidator implements ConstraintValidator<JahresratenValid, List<JahresrateDto>> {

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
