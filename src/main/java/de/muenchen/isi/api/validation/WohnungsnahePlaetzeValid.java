package de.muenchen.isi.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WohnungsnahePlaetzeValidator.class)
@Documented
public @interface WohnungsnahePlaetzeValid {
    String message() default "Die Anzahl der wohnungsnahen Plätze darf nicht die Gesamtanzahl der verfügbaren Plätze übersteigen.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
