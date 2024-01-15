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
@Constraint(validatedBy = RangeIdealtypischeBaurateValidator.class)
@Documented
public @interface RangeIdealtypischeBaurateValid {
    String message() default "Das von-Attribut der idealtypischen Baurate muss kleiner als das exklusive bis-Attribut sein.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
