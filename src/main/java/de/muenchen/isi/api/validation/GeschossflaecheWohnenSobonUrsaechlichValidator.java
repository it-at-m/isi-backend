package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.InfrastrukturabfrageDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class GeschossflaecheWohnenSobonUrsaechlichValidator
    implements ConstraintValidator<GeschossflaecheWohnenSobonUrsaechlichValid, InfrastrukturabfrageDto> {

    /**
     * Prüft, ob das Feld Geschossfläche Wohnen SoBoN-ursächlich {@link AbfragevarianteDto#getGeschossflaecheWohnenSoBoNursaechlich()} einen numerischen Wert hat.
     * Das Feld ist bei folgenden Vorbedingunen ein Mussfeld und wird auf numerischen Inhalt geprüft:
     * - die Abfrage ist SoBoN-relevant {@link InfrastrukturabfrageDto#getSobonRelevant()}
     * - und {@link AbfragevarianteDto#getPlanungsrecht()} ist {@link Planungsrecht#BPLAN_PARAG_12} oder {{@link Planungsrecht#BPLAN_PARAG_11}}
     *
     * @param value   {@link InfrastrukturabfrageDto} zum Validieren.
     * @param context in welchem die Validierung stattfindet.
     * @return true, falls das Attribut einen Wert hat, andernfalls false.
     */
    @Override
    public boolean isValid(final InfrastrukturabfrageDto value, final ConstraintValidatorContext context) {
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
