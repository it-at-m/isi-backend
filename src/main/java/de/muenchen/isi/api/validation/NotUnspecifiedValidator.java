package de.muenchen.isi.api.validation;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ILookup;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@NoArgsConstructor
public class NotUnspecifiedValidator implements ConstraintValidator<NotUnspecified, ILookup> {

    /**
     * Prüft, ob ein Objekt mit {@link ILookup#getBezeichnung()} NICHT den invaliden Wert {@link ILookup#UNSPECIFIED} zurückgibt.
     * Wenn das Objekt null ist oder der Aufruf von {@link ILookup#getBezeichnung()} ein null zurückgibt, gilt das ebenfalls als invalide.
     *
     * @param value Ein Objekt, welches {@link ILookup} implementiert.
     * @param context Kontext, in welchem die Validierung stattfindet.
     * @return True wenn nicht valide, ansonsten false.
     */
    @Override
    public boolean isValid(final ILookup value, final ConstraintValidatorContext context) {
        return value != null && value.getBezeichnung() != null && !value.getBezeichnung().equals(ILookup.UNSPECIFIED);
    }

}
