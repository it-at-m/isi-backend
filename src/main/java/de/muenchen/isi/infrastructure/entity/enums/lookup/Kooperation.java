/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Kooperation implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),
    SOZ("SOZ"),
    GSR("GSR"),
    KULT("KULT"),
    SONSTIGES("Sonstiges");

    @Getter
    private final String bezeichnung;
}
