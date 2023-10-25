package de.muenchen.isi.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TechnicalAttributesValidator.class)
@Documented
public @interface TechnicalAttributesValid {
    String message() default "Die Bauratenzuordnung bei Bauabschnitten oder Baugebieten ist fehlgeschlagen";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
