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
@Constraint(validatedBy = RealisierungVonDistributionValidator.class)
@Documented
public @interface RealisierungVonDistributionValid {
    String message() default "Das Realisierungsjahr eines Baugebiets ist vor dem Realisierungsjahr der Abfragevariante";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
