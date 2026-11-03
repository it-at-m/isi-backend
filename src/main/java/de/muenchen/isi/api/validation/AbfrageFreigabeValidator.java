package de.muenchen.isi.api.validation;

import de.muenchen.isi.api.mapper.AbfrageApiMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Prüft vor der Freigabe, ob eine als Entwurf gespeicherte Abfrage alle Pflichtangaben der Abfrageerstellung enthält.
 * <p>
 * Im Status {@link StatusAbfrage#ANGELEGT} wird beim Speichern nur gegen die Gruppe {@link Default} validiert.
 * Bei der Freigabe wird die gespeicherte Abfrage zusätzlich gegen die Gruppe {@link Vollstaendig} geprüft.
 */
@Component
@RequiredArgsConstructor
public class AbfrageFreigabeValidator {

    private final AbfrageApiMapper abfrageApiMapper;

    private final Validator validator;

    /**
     * @param abfrage die gespeicherte Abfrage.
     * @throws ConstraintViolationException falls sich die Abfrage im Status {@link StatusAbfrage#ANGELEGT} befindet
     *                                      und Pflichtangaben fehlen oder ungültig sind.
     */
    public void validate(final AbfrageModel abfrage) {
        if (abfrage.getStatusAbfrage() != StatusAbfrage.ANGELEGT) {
            // Die Prüfung des Status erfolgt bei der Statusänderung.
            return;
        }
        final var dto = abfrageApiMapper.model2AngelegtDto(abfrage);
        final var violations = validator.validate(dto, Default.class, Vollstaendig.class);
        if (!violations.isEmpty()) {
            final var message =
                "Die Abfrage kann nicht freigegeben werden, da Pflichtangaben fehlen oder ungültig sind: " +
                violations.stream().map(AbfrageFreigabeValidator::toMessage).sorted().collect(Collectors.joining(", "));
            throw new ConstraintViolationException(message, violations);
        }
    }

    private static String toMessage(final ConstraintViolation<?> violation) {
        return violation.getPropertyPath() + ": " + violation.getMessage();
    }
}
