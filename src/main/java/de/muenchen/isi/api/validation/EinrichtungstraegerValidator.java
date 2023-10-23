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
import org.apache.commons.lang3.ObjectUtils;
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
        return (value != null && einrichtungstraegerValid(value));
    }

    private boolean einrichtungstraegerValid(final InfrastruktureinrichtungDto value) {
        if (isStatusBestandOrGesicherteERW(value)) {
            if (value instanceof GrundschuleDto) {
                var einrichtungstraeger = ((GrundschuleDto) value).getSchule().getEinrichtungstraeger();
                return this.isValidEinrichtungstraegerSchule(einrichtungstraeger);
            } else if (value instanceof MittelschuleDto) {
                var einrichtungstraeger = ((MittelschuleDto) value).getSchule().getEinrichtungstraeger();
                return this.isValidEinrichtungstraegerSchule(einrichtungstraeger);
            } else if (value instanceof GsNachmittagBetreuungDto) {
                var einrichtungstraeger = ((GsNachmittagBetreuungDto) value).getEinrichtungstraeger();
                return this.isValidEinrichtungstraeger(einrichtungstraeger);
            } else if (value instanceof HausFuerKinderDto) {
                var einrichtungstraeger = ((HausFuerKinderDto) value).getEinrichtungstraeger();
                return this.isValidEinrichtungstraeger(einrichtungstraeger);
            } else if (value instanceof KindergartenDto) {
                var einrichtungstraeger = ((KindergartenDto) value).getEinrichtungstraeger();
                return this.isValidEinrichtungstraeger(einrichtungstraeger);
            } else {
                var einrichtungstraeger = ((KinderkrippeDto) value).getEinrichtungstraeger();
                return this.isValidEinrichtungstraeger(einrichtungstraeger);
            }
        } else {
            return true;
        }
    }

    /**
     * Überprüft, ob ein {@link InfrastruktureinrichtungDto} nicht den Status {@link StatusInfrastruktureinrichtung#BESTAND}
     * oder {@link StatusInfrastruktureinrichtung#GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR} hat.
     *
     * @param value Die zu überprüfende {@link InfrastruktureinrichtungDto}.
     * @return True, wenn der Status nicht {@link StatusInfrastruktureinrichtung#BESTAND} oder {@link StatusInfrastruktureinrichtung#GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR} ist, ansonsten false.
     */
    public boolean isStatusBestandOrGesicherteERW(final InfrastruktureinrichtungDto value) {
        return (
            value.getStatus() == StatusInfrastruktureinrichtung.BESTAND ||
            value.getStatus() == StatusInfrastruktureinrichtung.GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR
        );
    }

    /**
     * Überprüft, ob der {@link Einrichtungstraeger} der gegebenen Schule gültig ist.
     * Gültig bedeutet, dass der Einrichtungsträger in der Liste der Schulen enthalten ist.
     *
     * @param einrichtungstraeger Der zu überprüfende {@link Einrichtungstraeger}.
     * @return True, wenn der Einrichtungsträger gültig ist, ansonsten false.
     */
    public boolean isValidEinrichtungstraegerSchule(final Einrichtungstraeger einrichtungstraeger) {
        return ObjectUtils.isNotEmpty(einrichtungstraeger)
            ? Einrichtungstraeger.getEinrichtungstraegerSchulen().contains(einrichtungstraeger)
            : false;
    }

    /**
     * Überprüft, ob der gegebene {@link Einrichtungstraeger} gültig ist.
     * Gültig bedeutet, dass der Einrichtungsträger in der Liste der Einrichtungsträger enthalten ist.
     *
     * @param einrichtungstraeger Der zu überprüfende {@link Einrichtungstraeger}.
     * @return True, wenn der Einrichtungsträger gültig ist, ansonsten false.
     */
    public boolean isValidEinrichtungstraeger(final Einrichtungstraeger einrichtungstraeger) {
        return ObjectUtils.isNotEmpty(einrichtungstraeger)
            ? Einrichtungstraeger.getEinrichtungstraeger().contains(einrichtungstraeger)
            : false;
    }
}
