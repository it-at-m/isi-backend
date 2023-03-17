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
    IN_BEARBEITUNG_PLAN("in Bearbeitung bei PlAN-HA I/2"),
    IN_BEARBEITUNG_FACHREFERATE("in Bearbeitung bei den Fachreferaten"),
    BEDARFSMELDUNG_ERFOLGT("Bedarfsmeldung der Fachreferate ist erfolgt"),
    ERLEDIGT("erledigt"),
    ABBRUCH("abbruch");

    @Getter
    private final String bezeichnung;

}