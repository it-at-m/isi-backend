package de.muenchen.isi.domain.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Die auf der Startseite im Bereich "Meine Vorgänge" auswählbaren Schnellfilter.
 */
@Schema(enumAsRef = true)
public enum SchnellfilterVorgaenge {
    ALLE,
    ENTWUERFE,
    ZUR_BEARBEITUNG,
    ZUR_KENNTNIS,
    ABGESCHLOSSEN,
}
