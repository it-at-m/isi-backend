package de.muenchen.isi.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EarliestBaurateIsRealisierungVonValidator.class)
@Documented
public @interface EarliestBaurateIsRealisierungVonValid {
    String message() default "Das Jahr der frühesten Baurate der Abfragevariante muss mit dem Attribut 'Realisierung von' der Abfragevariante übereinstimmen.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
