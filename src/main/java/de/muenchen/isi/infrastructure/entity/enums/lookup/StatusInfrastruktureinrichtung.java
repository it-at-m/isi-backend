/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StatusInfrastruktureinrichtung implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED, new String[] {}),
    UNGESICHERTE_PLANUNG("ungesicherte Planung", new String[] { "ungesicherte", "Planung" }),
    GESICHERTE_PLANUNG("gesicherte Planung", new String[] { "gesicherte", "Planung" }),
    PLANUNG_ZURUECKGEZOGEN("Planung zurückgezogen", new String[] { "Planung", "zurückgezogen" }),
    BESTAND("Bestand", new String[] { "Bestand" });

    @Getter
    private final String bezeichnung;

    @Getter
    private final String[] suggestions;
}
