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
@Constraint(validatedBy = JahresratenValidator.class)
@Documented
public @interface JahresratenValid {
    String message() default "Die Summe der einzelnen prozentzahligen Raten muss insgesamt den Wert 1 ergeben.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
