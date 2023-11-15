package de.muenchen.isi.api.validation;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class CustomNotNullValidator implements ConstraintValidator<CustomNotNull, Object> {

    /**
     * Diese Validierung wird benötigt um frontendseitig die Swagger-Sensitive-Validierung {@link NotNull} vermeiden zu können.
     * Die {@link NotNull}-Validierung führt im frontendseitig generierten Code zu fehlerhaften Verhalten bei Vererbung.
     * Besitzen die in einer Vererbungshierarchie befindliche Klassen unterschiedliche Collection-Attribute annotiert mit {@link NotNull},
     * so werden in der generierten frontendseitigen JSON-Serialisierung die Collection-Elemente (backendseitig einen Collection)
     * JSON-serialisiert obwohl der Array undefined ist.
     *
     * @param value   zum Validieren.
     * @param context in welchem die Validierung stattfindet.
     *
     * @return true falls der Parameter value nicht null ist, ansonsten false.
     */
    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        return !Objects.isNull(value);
    }
}
