package de.muenchen.isi.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HasAllowedFileExtensionValidator.class)
@Documented
public @interface HasAllowedFileExtension {

    String message() default "Die Dateiendung der im Attribut referenzierten Datei ist nicht erlaubt.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
