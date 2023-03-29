package de.muenchen.isi.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HasAllowedNumberOfDocumentsValidator.class)
@Documented
public @interface HasAllowedNumberOfDocuments {
    String message() default "Die Anzahl der erlaubten Dokumente ist überschritten.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
