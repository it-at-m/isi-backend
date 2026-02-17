/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Verfahrensart implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),

    REGELVERFAHREN("Regelverfahren"),

    VEREINFACHTES_VERFAHREN_PARAGRAPH_13("Vereinfachtes Verfahren, § 13 BauGB"),

    BEBAUUNGSPLAN_DER_INNENENTWICKLUNG_PARAGRAPH_13A("Bebauungsplan der Innenentwicklung, § 13a BauGB");

    @Getter
    private final String bezeichnung;
}
