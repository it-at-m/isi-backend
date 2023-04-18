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
@Constraint(validatedBy = GeschossflaecheWohnenSobonUrsaechlichValidator.class)
@Documented
public @interface GeschossflaecheWohnenSobonUrsaechlichValid {
    String message() default "Die Geschossfläche SoBoN-ursächlich muss angegeben werden.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
