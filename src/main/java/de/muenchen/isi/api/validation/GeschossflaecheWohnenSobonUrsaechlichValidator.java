package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.AbfragevarianteResponseDto;
import de.muenchen.isi.api.dto.InfrastrukturabfrageResponseDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@NoArgsConstructor
public class GeschossflaecheWohnenSobonUrsaechlichValidator
        implements ConstraintValidator<GeschossflaecheWohnenSobonUrsaechlichValid, InfrastrukturabfrageResponseDto> {

    /**
     * Prüft, ob das Feld Geschossfläche Wohnen SoBoN-ursächlich {@link AbfragevarianteResponseDto#getGeschossflaecheWohnenSoBoNursaechlich()} einen numerischen Wert hat.
     * Das Feld ist bei folgenden Vorbedingunen ein Mussfeld und wird auf numerischen Inhalt geprüft:
     * - die Abfrage ist SoBoN-relevant {@link InfrastrukturabfrageResponseDto#getSobonRelevant()}
     * - und {@link AbfragevarianteResponseDto#getPlanungsrecht()} ist {@link Planungsrecht#BPLAN_PARAG_12} oder {{@link Planungsrecht#BPLAN_PARAG_11}}
     *
     * @param value   {@link InfrastrukturabfrageResponseDto} zum Validieren.
     * @param context in welchem die Validierung stattfindet.
     * @return true, falls das Attribut einen Wert hat, andernfalls false.
     */
    @Override
    public boolean isValid(final InfrastrukturabfrageResponseDto value, final ConstraintValidatorContext context) {
        if (
                value == null || value.getAbfragevarianten() == null || value.getSobonRelevant() == UncertainBoolean.FALSE
        ) {
            return true;
        }
        return !value
                .getAbfragevarianten()
                .stream()
                .anyMatch(abfragevariante ->
                        (
                                abfragevariante.getPlanungsrecht() == Planungsrecht.BPLAN_PARAG_11 ||
                                        abfragevariante.getPlanungsrecht() == Planungsrecht.BPLAN_PARAG_12
                        ) &&
                                abfragevariante.getGeschossflaecheWohnenSoBoNursaechlich() == null
                );
    }
}
