package de.muenchen.isi.infrastructure.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Die Art einer Bearbeitung in der Bearbeitungshistorie.
 */
@Schema(enumAsRef = true)
public enum BearbeitungsArt {
    ERSTELLT,
    AKTUALISIERT,
    STATUSAENDERUNG,
}
