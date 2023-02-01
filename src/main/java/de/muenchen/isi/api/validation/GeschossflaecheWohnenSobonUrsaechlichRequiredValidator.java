package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.InfrastrukturabfrageDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@NoArgsConstructor
public class GeschossflaecheWohnenSobonUrsaechlichRequiredValidator implements ConstraintValidator<IsGeschossflaecheWohnenSobonUrsaechlichRequired, InfrastrukturabfrageDto> {

    /**
     * Prüft ob die Geschossfläche Wohnen SoBoN-ursächlich {@Link AbfragevarianteDto#geschossflaecheWohnenSoBoNursaechlich} einen Wert hat.
     * Das Feld muss bei folgenden Vorbedingunen gefüllt sein:
     * - die Abfrage ist SoBoN-relevant {@Link InfrastrukturabfrageDto#sobonRelevant.}
     * - und {@Link AbfragevarianteDto#planungsrecht} ist {@Link Planungsrecht#BPLAN_PARAG_12} oder {{@Link Planungsrecht#BPLAN_PARAG_11}}
     *
     * @param value   {@link InfrastrukturabfrageDto} zum Validieren.
     * @param context in welchem die Validierung statt findet.
     * @return true falls das Attribut einen Wert hat, andernfalls false.
     */
    @Override
    public boolean isValid(final InfrastrukturabfrageDto value, final ConstraintValidatorContext context) {
        if (value == null ||
                value.getAbfragevarianten() == null ||
                value.getSobonRelevant() == UncertainBoolean.FALSE) {
            return true;
        }
        return !value.getAbfragevarianten().stream()
                .anyMatch(abfragevariante ->
                        (abfragevariante.getPlanungsrecht() == Planungsrecht.BPLAN_PARAG_11 ||
                                abfragevariante.getPlanungsrecht() == Planungsrecht.BPLAN_PARAG_12) &&
                                abfragevariante.getGeschossflaecheWohnenSoBoNursaechlich() == null);
    }

}
