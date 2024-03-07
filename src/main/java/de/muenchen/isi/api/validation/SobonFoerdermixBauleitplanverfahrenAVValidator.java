package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class SobonFoerdermixBauleitplanverfahrenAVValidator
    implements
        ConstraintValidator<
            SobonFoerdermixBauleitplanverfahrenAVValid,
            AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungDto
        > {

    @Override
    public boolean isValid(
        final AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungDto abfragevarianteDto,
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
