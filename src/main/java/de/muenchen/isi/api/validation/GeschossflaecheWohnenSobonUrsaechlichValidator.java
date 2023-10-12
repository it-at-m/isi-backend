package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfragevarianteBauleitplanverfahrenAngelegtDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.BauleitplanverfahrenAngelegtDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class GeschossflaecheWohnenSobonUrsaechlichValidator
    implements ConstraintValidator<GeschossflaecheWohnenSobonUrsaechlichValid, BauleitplanverfahrenAngelegtDto> {

    /**
     * Prüft, ob das Feld Geschossfläche Wohnen SoBoN-ursächlich {@link AbfragevarianteAngelegtDto#getGeschossflaecheWohnenSoBoNursaechlich()} einen numerischen Wert hat.
     * Das Feld ist bei folgenden Vorbedingungen ein Mussfeld und wird auf numerischen Inhalt geprüft:
     * - die Abfrage ist SoBoN-relevant {@link BauleitplanverfahrenAngelegtDto#getSobonRelevant()}
     * - und {@link AbfragevarianteBauleitplanverfahrenAngelegtDto#getWesentlicheRechtsgrundlage()} beinhaltet {@link WesentlicheRechtsgrundlage#VORHABENSBEZOGENER_BEBAUUNGSPLAN}
     *
     * @param value   {@link BauleitplanverfahrenAngelegtDto} zum Validieren.
     * @param context in welchem die Validierung stattfindet.
     * @return true, falls das Attribut einen Wert hat, andernfalls false.
     */
    @Override
    public boolean isValid(final BauleitplanverfahrenAngelegtDto value, final ConstraintValidatorContext context) {
        if (
            value == null || value.getAbfragevarianten() == null || value.getSobonRelevant() == UncertainBoolean.FALSE
        ) {
            return true;
        }
        return !value
            .getAbfragevarianten()
            .stream()
            .anyMatch(abfragevariante -> {
                final var containsRelevantRechtsgrundlagen = CollectionUtils.containsAny(
                    abfragevariante.getWesentlicheRechtsgrundlage(),
                    WesentlicheRechtsgrundlage.VORHABENSBEZOGENER_BEBAUUNGSPLAN
                );
                return containsRelevantRechtsgrundlagen && abfragevariante.getGfWohnenSobonUrsaechlich() == null;
            });
    }
}
