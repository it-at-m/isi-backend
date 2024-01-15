package de.muenchen.isi.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsFilepathWithoutLeadingPathdividerValidator.class)
@Documented
public @interface IsFilepathWithoutLeadingPathdivider {
    String message() default "Der Dateipfad muss absolut, ohne Angabe des Buckets und ohne führenden Pfadtrenner / angegeben werden. Beispiel: outerFolder/innerFolder/thefile.pdf";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
