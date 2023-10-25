package de.muenchen.isi.api.validation;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ILookup;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotUnspecifiedValidator.class)
@Documented
public @interface NotUnspecified {
    String message() default "Der Wert darf nicht '" + ILookup.UNSPECIFIED + "' sein.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
