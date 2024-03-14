package de.muenchen.isi.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BauratendateiInputsValidator.class)
@Documented
public @interface BauratendateiInputsValid {
    String message() default "Bei den Daten für die Bauratendatei sind die räumlichen Daten oder die Summe der Wohneinheiten nicht korrekt.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
