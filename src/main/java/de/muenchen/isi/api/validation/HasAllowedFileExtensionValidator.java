package de.muenchen.isi.api.validation;

import de.muenchen.isi.domain.service.stammdaten.FileInformationStammService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class HasAllowedFileExtensionValidator implements ConstraintValidator<HasAllowedFileExtension, String> {

    private final String[] allowedFileExtensionsLowerCase;

    public HasAllowedFileExtensionValidator(final FileInformationStammService fileInformationStammService) {
        this.allowedFileExtensionsLowerCase = fileInformationStammService
                .getFileInformation()
                .getAllowedFileExtensions()
                .stream()
                .map(StringUtils::lowerCase)
                .toArray(String[]::new);
    }

    /**
     * Prüft das String-Attribut welches einen Dateipfad repräsentiert auf die erlaubte Dateiendung.
     *
     * @param value   des String-Attributs welches einen Dateipfad repräsentiert.
     * @param context in welchem die Validierung stattfindet.
     * @return true, falls das Attribut eine erlaubte Dateiendung besitzt. Andernfalls false.
     */
    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return StringUtils.endsWithAny(StringUtils.lowerCase(value), this.allowedFileExtensionsLowerCase);
    }

}
