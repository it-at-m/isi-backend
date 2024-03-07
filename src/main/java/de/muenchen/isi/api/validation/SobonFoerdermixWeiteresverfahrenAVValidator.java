package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
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
        boolean isSobonBerechnung =
            abfragevarianteDto.getIsASobonBerechnung() != null && abfragevarianteDto.getIsASobonBerechnung();
        boolean isFoerdermixEmtpy = abfragevarianteDto.getSobonFoerdermix() == null;
        if (isSobonBerechnung) {
            return !isFoerdermixEmtpy;
        } else {
            return isFoerdermixEmtpy;
        }
    }
}
