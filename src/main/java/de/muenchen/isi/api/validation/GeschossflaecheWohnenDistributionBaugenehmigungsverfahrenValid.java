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
@Constraint(validatedBy = GeschossflaecheWohnenDistributionBaugenehmigungsverfahrenValidator.class)
@Documented
public @interface GeschossflaecheWohnenDistributionBaugenehmigungsverfahrenValid {
    String message() default "Die Summe der über die Baugebiete verteilten Geschossfläche Wohnen" +
    " entspricht nicht der Geschossfläche Wohnen in der Abfragevariante.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
