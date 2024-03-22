package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.common.SobonBerechnungDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class SobonBerechnungValidator implements ConstraintValidator<SobonBerechnungValid, SobonBerechnungDto> {

    @Override
    public boolean isValid(
        final SobonBerechnungDto sobonBerechnung,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        if (sobonBerechnung != null) {
            boolean isSobonBerechnung = BooleanUtils.toBoolean(sobonBerechnung.getIsASobonBerechnung());
            boolean isFoerdermixEmtpy =
                sobonBerechnung.getSobonFoerdermix() == null ||
                (StringUtils.isEmpty(sobonBerechnung.getSobonFoerdermix().getBezeichnungJahr()) &&
                    StringUtils.isEmpty(sobonBerechnung.getSobonFoerdermix().getBezeichnung()) &&
                    CollectionUtils.isEmpty(sobonBerechnung.getSobonFoerdermix().getFoerderarten()));
            return isSobonBerechnung != isFoerdermixEmtpy;
        }
        return true;
    }
}
