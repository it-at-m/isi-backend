package de.muenchen.isi.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HasAllowedNumberOfDocumentsValidator.class)
@Documented
public @interface HasAllowedNumberOfDocuments {
    String message() default "Die Anzahl der erlaubten Dokumente ist überschritten.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
