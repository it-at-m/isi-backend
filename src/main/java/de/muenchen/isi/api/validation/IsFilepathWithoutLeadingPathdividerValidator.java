package de.muenchen.isi.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class IsFilepathWithoutLeadingPathdividerValidator
    implements ConstraintValidator<IsFilepathWithoutLeadingPathdivider, String> {

    public static final String PATH_SEPARATOR = "/";

    /**
     * Prüft das String-Attribut welches einen Dateipfad repräsentiert auf vorhandensein eines führenden Pfadtrenner.
     * Der Dateipfad muss absolut, ohne Angabe des Buckets und ohne führenden Pfadtrenner angegeben werden.
     * Beispiel:
     * - Datei im S3-Bucket: "BUCKET/outerFolder/innerFolder/thefile.csv"
     * - Korrekte Angabe im String-Attribut: "outerFolder/innerFolder/thefile.csv"
     *
     * @param value   des String-Attributs welches einen Dateipfad repräsentiert.
     * @param context in welchem die Validierung stattfindet.
     * @return true, falls kein führender Pfadtrenner existiert. Andernfalls false.
     */
    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return !StringUtils.startsWith(value, PATH_SEPARATOR);
    }
}
