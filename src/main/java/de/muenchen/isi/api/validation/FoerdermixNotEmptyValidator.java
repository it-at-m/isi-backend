package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.FoerdermixDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class FoerdermixNotEmptyValidator implements ConstraintValidator<FoerdermixNotEmptyValid, FoerdermixDto> {

    @Override
    public boolean isValid(FoerdermixDto foerdermixDto, ConstraintValidatorContext constraintValidatorContext) {
        return (
            foerdermixDto.getBezeichnung() != null &&
            !foerdermixDto.getBezeichnung().isEmpty() &&
            foerdermixDto.getBezeichnungJahr() != null &&
            !foerdermixDto.getBezeichnungJahr().isEmpty()
        );
    }
}
