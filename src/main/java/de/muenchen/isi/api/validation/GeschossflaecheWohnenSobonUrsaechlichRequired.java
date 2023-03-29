package de.muenchen.isi.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GeschossflaecheWohnenSobonUrsaechlichRequiredValidator.class)
@Documented
public @interface GeschossflaecheWohnenSobonUrsaechlichRequired {
    String message() default "Die Geschossfläche SoBoN-ursächlich muss angegeben werden.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
