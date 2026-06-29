package de.muenchen.isi.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Start42VerfahrenValidator.class)
@Documented
public @interface Start42VerfahrenValid {
    String message() default "Es muss entweder 'Start 4.2-Verfahren' angegeben oder 'Datum unbekannt / nicht zutreffend' ausgewählt werden.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
