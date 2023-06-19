package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class RealisierungVonOrSatzungsbeschlussValidator
    implements
        ConstraintValidator<RealisierungVonOrSatzungsbeschlussValid, AbfrageerstellungAbfragevarianteAngelegtDto> {

    /**
     * Prüft, ob die Felder {@link AbfrageerstellungAbfragevarianteAngelegtDto#getRealisierungVon()}
     * und {@link AbfrageerstellungAbfragevarianteAngelegtDto#getSatzungsbeschluss()} nicht null sind.
     * Sind beide null, gilt das als invalide.
     *
     * @param value   {@link AbfrageerstellungAbfragevarianteAngelegtDto} zum Validieren.
     * @param context in welchem die Validierung stattfindet.
     * @return true, falls realisierungVon oder satzungsbeschluss einen gültigen Wert hat, andernfalls false.
     */
    @Override
    public boolean isValid(
        final AbfrageerstellungAbfragevarianteAngelegtDto value,
        final ConstraintValidatorContext context
    ) {
        return value != null && (value.getRealisierungVon() != null || value.getSatzungsbeschluss() != null);
    }
}
