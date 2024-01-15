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
@Constraint(validatedBy = FertigstellungsjahrValidator.class)
@Documented
public @interface FertigstellungsjahrValid {
    String message() default "Das Fertigstellungsjahr muss angegeben werden.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
