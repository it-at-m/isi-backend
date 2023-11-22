package de.muenchen.isi.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WesentlicheRechtsgrundlageBaugenehmigungsverfahrenValidator.class)
@Documented
public @interface WesentlicheRechtsgrundlageBaugenehmigungsverfahrenValid {
    String message() default "Die wesentliche Rechtsgrundlage ist für das Bauleitplanverfahren nicht gültig.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
