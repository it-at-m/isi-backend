package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.AbfragevarianteResponseDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Component
@NoArgsConstructor
public class UniqueRelevantAbfragevarianteValidator
        implements ConstraintValidator<UniqueRelevantAbfragevarianteValid, List<AbfragevarianteResponseDto>> {

    /**
     * Prüft ob es nur eine oder keine relevante Abfragevariante pro Abfrage gibt.
     *
     * @param abfragevarianteDtos        {@link AbfragevarianteResponseDto } zum Validieren
     * @param constraintValidatorContext in welchem die Validierung stattfindet.
     * @return true wenn es nur eine Relevante Abfragevariante gibt. Bei mehreren Relevanten Abfragevarianten wird false zurückgegeben
     */
    @Override
    public boolean isValid(
            List<AbfragevarianteResponseDto> abfragevarianteDtos,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        int anzahlRelevanterAbfragevarianten = 0;
        for (AbfragevarianteResponseDto abfragevarianteDto : abfragevarianteDtos) {
            if (abfragevarianteDto.isRelevant()) anzahlRelevanterAbfragevarianten++;
        }
        return anzahlRelevanterAbfragevarianten <= 1 ? true : false;
    }
}
