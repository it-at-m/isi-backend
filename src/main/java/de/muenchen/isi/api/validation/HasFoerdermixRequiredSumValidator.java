package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.FoerderartDto;
import de.muenchen.isi.api.dto.FoerdermixDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.List;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class HasFoerdermixRequiredSumValidator implements ConstraintValidator<HasFoerdermixRequiredSum, FoerdermixDto> {

    public static final BigDecimal REQUIRED_SUM = BigDecimal.valueOf(100);

    /**
     * Prüft ob die Summe der Klassenattribute genau den Wert {@link HasFoerdermixRequiredSumValidator#REQUIRED_SUM} ergibt.
     * Attribute mit Wert "null" werden als {@link BigDecimal#ZERO} interpretiert.
     *
     * @param value   {@link FoerdermixDto} zum Validieren.
     * @param context in welchem die Validierung statt findet.
     * @return true falls die Summe den Wert {@link HasFoerdermixRequiredSumValidator#REQUIRED_SUM} entspricht, andernfalls false.
     */
    @Override
    public boolean isValid(final FoerdermixDto value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        BigDecimal sumFoerdermix = BigDecimal.ZERO;

        List<FoerderartDto> foerderarten = value.getFoerderarten();
        for (FoerderartDto foederart : foerderarten) {
            var summand = ObjectUtils.defaultIfNull(foederart.getAnteilProzent(), BigDecimal.ZERO);
            sumFoerdermix = sumFoerdermix.add(summand);
        }

        return sumFoerdermix.compareTo(REQUIRED_SUM) == 0;
    }
}
