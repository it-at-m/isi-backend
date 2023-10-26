package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.infrastruktureinrichtung.HausFuerKinderDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.InfrastruktureinrichtungDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KindergartenDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KinderkrippeDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class WohnungsnahePlaetzeValidator
    implements ConstraintValidator<WohnungsnahePlaetzeValid, InfrastruktureinrichtungDto> {

    /**
     * Prüft, ob das Feld einrichtungstraeger nicht null ist.
     * null ist jedoch erlaubt, wenn {@link InfrastruktureinrichtungDto#getStatus()} weder {@link StatusInfrastruktureinrichtung#BESTAND} noch {@link StatusInfrastruktureinrichtung#GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR} ist.
     *
     * @param value   {@link InfrastruktureinrichtungDto} zum Validieren.
     * @param context in welchem die Validierung stattfindet.
     * @return true, falls das Fertigstellungsjahr einen gültigen Wert hat, andernfalls false.
     */
    @Override
    public boolean isValid(final InfrastruktureinrichtungDto value, final ConstraintValidatorContext context) {
        if (value != null) {
            if (value instanceof HausFuerKinderDto) {
                final var hausFuerKinder = ((HausFuerKinderDto) value);
                return (
                    this.areWohnungsnahePlatzeValid(
                            hausFuerKinder.getAnzahlKinderkrippePlaetze(),
                            hausFuerKinder.getWohnungsnaheKinderkrippePlaetze()
                        ) &&
                    this.areWohnungsnahePlatzeValid(
                            hausFuerKinder.getAnzahlKindergartenPlaetze(),
                            hausFuerKinder.getWohnungsnaheKindergartenPlaetze()
                        ) &&
                    this.areWohnungsnahePlatzeValid(
                            hausFuerKinder.getAnzahlHortPlaetze(),
                            hausFuerKinder.getWohnungsnaheHortPlaetze()
                        )
                );
            } else if (value instanceof KindergartenDto) {
                final var kindergarten = ((KindergartenDto) value);
                return this.areWohnungsnahePlatzeValid(
                        kindergarten.getAnzahlKindergartenPlaetze(),
                        kindergarten.getWohnungsnaheKindergartenPlaetze()
                    );
            } else if (value instanceof KinderkrippeDto) {
                final var kinderkrippe = ((KinderkrippeDto) value);
                return this.areWohnungsnahePlatzeValid(
                        kinderkrippe.getAnzahlKinderkrippePlaetze(),
                        kinderkrippe.getWohnungsnaheKinderkrippePlaetze()
                    );
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean areWohnungsnahePlatzeValid(final Integer plaetzeGesamt, final Integer plaetzeWohnungsnah) {
        return ObjectUtils.defaultIfNull(plaetzeWohnungsnah, 0) <= ObjectUtils.defaultIfNull(plaetzeGesamt, 0);
    }
}
