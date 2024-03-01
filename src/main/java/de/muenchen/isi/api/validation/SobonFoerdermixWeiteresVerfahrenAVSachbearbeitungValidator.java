package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class SobonFoerdermixWeiteresVerfahrenAVSachbearbeitungValidator
    implements
        ConstraintValidator<
            SobonFoerdermixWeiteresVerfahrenAVSachbearbeitungValid,
            AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungDto
        > {

    @Override
    public boolean isValid(
        AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungDto abfragevarianteDto,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        boolean isBerechnung = abfragevarianteDto.getIsASobonBerechnung();
        boolean isFoerdermixNotEmpty = ObjectUtils.isNotEmpty(abfragevarianteDto.getSobonFoerdermix());
        boolean isFoerdermixEmpty = ObjectUtils.isEmpty(abfragevarianteDto.getSobonFoerdermix());

        return (isBerechnung && isFoerdermixNotEmpty) || (!isBerechnung && isFoerdermixEmpty);
    }
}
