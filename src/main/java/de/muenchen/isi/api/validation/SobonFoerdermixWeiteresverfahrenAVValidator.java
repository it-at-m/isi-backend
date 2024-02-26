package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class SobonFoerdermixWeiteresverfahrenAVValidator
    implements
        ConstraintValidator<
            SobonFoerdermixWeiteresverfahrenAVValid,
            AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungDto
        > {

    @Override
    public boolean isValid(
        AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungDto abfragevarianteDto,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        boolean isBerechnung = abfragevarianteDto.getIsASobonBerechnung();
        boolean isFoerdermixNotEmpty = ObjectUtils.isNotEmpty(abfragevarianteDto.getSobonFoerdermix());
        boolean isFoerdermixEmpty = ObjectUtils.isEmpty(abfragevarianteDto.getSobonFoerdermix());

        return (isBerechnung && isFoerdermixNotEmpty) || (!isBerechnung && isFoerdermixEmpty);
    }
}
