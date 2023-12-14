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
@Constraint(validatedBy = RealisierungVonDistributionWeiteresVerfahrenValidator.class)
@Documented
public @interface RealisierungVonDistributionWeiteresVerfahrenValid {
    String message() default "Das Realisierungsjahr eines Baugebiets bzw. einer Baurate ist vor dem Realisierungsjahr der Abfragevariante";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
