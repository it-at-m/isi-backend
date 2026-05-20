package de.muenchen.isi.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WesentlicheRechtsgrundlageWeiteresVerfahrenValidator.class)
@Documented
public @interface WesentlicheRechtsgrundlageWeiteresVerfahrenValid {
    String message() default "Die wesentliche Rechtsgrundlage ist für das Weitere Verfahren nicht gültig.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
