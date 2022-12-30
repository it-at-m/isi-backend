package de.muenchen.isi.api.validation;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ILookup;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotUnspecifiedValidator.class)
@Documented
public @interface NotUnspecified {

    String message() default "Der Wert darf nicht '" + ILookup.UNSPECIFIED + "' sein.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
