package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.infrastruktureinrichtung.InfrastruktureinrichtungDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class EinrichtungstraegerValidator
    implements ConstraintValidator<EinrichtungstraegerValid, InfrastruktureinrichtungDto> {

    /**
     * Prüft, ob das Feld {@link InfrastruktureinrichtungDto#getEinrichtungstraeger()} nicht null ist.
     * null ist jedoch erlaubt, wenn {@link InfrastruktureinrichtungDto#getStatus()} weder {@link StatusInfrastruktureinrichtung#BESTAND} noch {@link StatusInfrastruktureinrichtung#GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR} ist.
     *
     * @param value   {@link InfrastruktureinrichtungDto} zum Validieren.
     * @param context in welchem die Validierung stattfindet.
     * @return true, falls das Fertigstellungsjahr einen gültigen Wert hat, andernfalls false.
     */
    @Override
    public boolean isValid(final InfrastruktureinrichtungDto value, final ConstraintValidatorContext context) {
        return (
            value != null &&
            (
                (value.getEinrichtungstraeger() != null && value.getEinrichtungstraeger().getBezeichnung() != null) ||
                (
                    value.getStatus() != StatusInfrastruktureinrichtung.BESTAND &&
                    value.getStatus() != StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR
                )
            )
        );
    }
}
