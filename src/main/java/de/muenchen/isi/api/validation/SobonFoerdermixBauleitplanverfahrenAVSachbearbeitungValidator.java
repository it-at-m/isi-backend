package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class SobonFoerdermixBauleitplanverfahrenAVSachbearbeitungValidator
    implements
        ConstraintValidator<
            SobonFoerdermixBauleitplanverfahrenAVSachbearbeitungValid,
            AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungDto
        > {

    @Override
    public boolean isValid(
        final AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungDto abfragevarianteDto,
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
