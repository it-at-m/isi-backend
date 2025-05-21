/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(enumAsRef = true)
@AllArgsConstructor
public enum StatusAbfrage implements ILookup {
    ANGELEGT("Angelegt"),
    UEBERMITTELT_ZUR_BEARBEITUNG("Übermittelt zur Bearbeitung"),
    START_BEARBEITUNG("Start Bearbeitung"),
    EINPFLEGEN_BEDARFSMELDUNG("Einpflegen Bedarfsmeldung"),
    EINPLANUNG_BEDARFE("Einplanung Bedarfe"),
    ERLEDIGT_MIT_FACHREFERAT("Erledigt"),
    ERLEDIGT_OHNE_FACHREFERAT("Erledigt ohne Einbindung Fachreferat"),
    ABBRUCH("Abbruch");

    @Getter
    private final String bezeichnung;

    public static StatusAbfrage fromString(String bezeichnung) {
        return Arrays.stream(values())
            .filter(status -> status.getBezeichnung().equalsIgnoreCase(bezeichnung))
            .findFirst()
            .orElse(ANGELEGT);
    }
}
