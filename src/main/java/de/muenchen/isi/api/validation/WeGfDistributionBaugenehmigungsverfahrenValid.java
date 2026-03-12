package de.muenchen.isi.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WeGfDistributionBaugenehmigungsverfahrenValidator.class)
@Documented
public @interface WeGfDistributionBaugenehmigungsverfahrenValid {
    String message() default "Die Summe der über die Baugebiete verteilten Wohneinheiten oder Geschossfläche Wohnen" +
        " entspricht nicht der Anzahl der Wohneinheiten oder Geschossfläche Wohnen in der Abfragevariante.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
