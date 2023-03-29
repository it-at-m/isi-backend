package de.muenchen.isi.infrastructure.entity.enums.lookup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Ein generisches Enum zum Abbilden eines Wertes, welcher neben den üblichen wahr-/falsch-Zuständen
 * einen dritten, "undefinierten" Zustand abbilden kann.
 */
@Schema(enumAsRef = true)
@AllArgsConstructor
public enum UncertainBoolean implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),
    TRUE("Ja"),
    FALSE("Nein");

    @Getter
    private final String bezeichnung;
}
