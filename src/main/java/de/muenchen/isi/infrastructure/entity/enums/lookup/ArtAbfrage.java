/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ArtAbfrage implements ILookup {
    BAULEITPLANVERFAHREN("Bauleitplanverfahren"),

    BAUGENEHMIGUNGSVERFAHREN("Baugenehmigungsverfahren"),

    WEITERE_ABFRAGEN("Weitere Abfragen");

    @Getter
    private final String bezeichnung;
}
