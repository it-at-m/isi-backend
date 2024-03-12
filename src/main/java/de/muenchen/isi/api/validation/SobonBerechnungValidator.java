package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.common.SobonBerechnungDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class SobonBerechnungValidator implements ConstraintValidator<SobonBerechnungValid, SobonBerechnungDto> {

    @Override
    public boolean isValid(
        final SobonBerechnungDto sobonBerechnung,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        boolean isSobonBerechnung = BooleanUtils.toBoolean(sobonBerechnung.getIsASobonBerechnung());
        boolean isFoerdermixEmtpy =
            sobonBerechnung.getSobonFoerdermix() == null ||
            (sobonBerechnung.getSobonFoerdermix().getBezeichnungJahr() == null &&
                sobonBerechnung.getSobonFoerdermix().getBezeichnung() == null &&
                sobonBerechnung.getSobonFoerdermix().getFoerderarten() == null);
        if (isSobonBerechnung) {
            return !isFoerdermixEmtpy;
        } else {
            return isFoerdermixEmtpy;
        }
    }
}
