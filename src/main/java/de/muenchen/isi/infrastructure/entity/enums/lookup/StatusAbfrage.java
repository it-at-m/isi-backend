/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StatusAbfrage implements ILookup {

    ANGELEGT("angelegt"),
    OFFEN("offen"),
    IN_ARBEIT("in Arbeit"),
    IN_ERFASSUNG("in Erfassung"),
    IN_BEARBEITUNG("in Bearbeitung"),
    ERLEDIGT("erledigt"),
    ABBRUCH("Abbruch");

    @Getter
    private final String bezeichnung;

}