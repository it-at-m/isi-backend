package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.dto.infrastruktureinrichtung.GrundschuleDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.GsNachmittagBetreuungDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.HausFuerKinderDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.InfrastruktureinrichtungDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KindergartenDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KinderkrippeDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.MittelschuleDto;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
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
     * Prüft, ob das Feld einrichtungstraeger nicht null ist.
     * null ist jedoch erlaubt, wenn {@link InfrastruktureinrichtungDto#getStatus()} weder {@link StatusInfrastruktureinrichtung#BESTAND} noch {@link StatusInfrastruktureinrichtung#GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR} ist.
     *
     * @param value   {@link InfrastruktureinrichtungDto} zum Validieren.
     * @param context in welchem die Validierung stattfindet.
     * @return true, falls das Fertigstellungsjahr einen gültigen Wert hat, andernfalls false.
     */
    @Override
    public boolean isValid(final InfrastruktureinrichtungDto value, final ConstraintValidatorContext context) {
        return (value != null && einrichtungsTraegerValid(value));
    }

    private boolean einrichtungsTraegerValid(final InfrastruktureinrichtungDto value) {
        if (!invalidEinrichtungstraegerAllowed(value)) {
            if (value instanceof GrundschuleDto) {
                var einrichtungstraeger = ((GrundschuleDto) value).getSchule().getEinrichtungstraeger();
                return (
                    this.isNotNull(einrichtungstraeger) &&
                    Einrichtungstraeger.getEinrichtungstraegerSchulen().contains(einrichtungstraeger)
                );
            } else if (value instanceof MittelschuleDto) {
                var einrichtungstraeger = ((MittelschuleDto) value).getSchule().getEinrichtungstraeger();
                return (
                    this.isNotNull(einrichtungstraeger) &&
                    Einrichtungstraeger.getEinrichtungstraegerSchulen().contains(einrichtungstraeger)
                );
            } else if (value instanceof GsNachmittagBetreuungDto) {
                var einrichtungstraeger = ((GsNachmittagBetreuungDto) value).getEinrichtungstraeger();
                return (
                    this.isNotNull(einrichtungstraeger) &&
                    Einrichtungstraeger.getEinrichtungstraeger().contains(einrichtungstraeger)
                );
            } else if (value instanceof HausFuerKinderDto) {
                var einrichtungstraeger = ((HausFuerKinderDto) value).getEinrichtungstraeger();
                return (
                    this.isNotNull(einrichtungstraeger) &&
                    Einrichtungstraeger.getEinrichtungstraeger().contains(einrichtungstraeger)
                );
            } else if (value instanceof KindergartenDto) {
                var einrichtungstraeger = ((KindergartenDto) value).getEinrichtungstraeger();
                return (
                    this.isNotNull(einrichtungstraeger) &&
                    Einrichtungstraeger.getEinrichtungstraeger().contains(einrichtungstraeger)
                );
            } else {
                var einrichtungstraeger = ((KinderkrippeDto) value).getEinrichtungstraeger();
                return (
                    this.isNotNull(einrichtungstraeger) &&
                    Einrichtungstraeger.getEinrichtungstraeger().contains(einrichtungstraeger)
                );
            }
        } else {
            return true;
        }
    }

    private boolean invalidEinrichtungstraegerAllowed(final InfrastruktureinrichtungDto value) {
        return (
            value.getStatus() != StatusInfrastruktureinrichtung.BESTAND &&
            value.getStatus() != StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR
        );
    }

    private boolean isNotNull(final Einrichtungstraeger einrichtungstraeger) {
        return einrichtungstraeger != null && einrichtungstraeger.getBezeichnung() != null;
    }
}
