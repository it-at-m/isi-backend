package de.muenchen.isi.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GeschossflaecheWohnenDistributionBaugenehmigungsverfahrenValidator.class)
@Documented
public @interface GeschossflaecheWohnenDistributionBaugenehmigungsverfahrenValid {
    String message() default "Die Summe der über die Baugebiete verteilten Geschossfläche Wohnen" +
    " entspricht nicht der Geschossfläche Wohnen in der Abfragevariante.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
