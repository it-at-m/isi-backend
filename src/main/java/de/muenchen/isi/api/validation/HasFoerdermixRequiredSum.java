package de.muenchen.isi.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HasFoerdermixRequiredSumValidator.class)
@Documented
public @interface HasFoerdermixRequiredSum {

    String message() default "Die Summe der Fördermix-Werte muss genau 100% ergeben.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
