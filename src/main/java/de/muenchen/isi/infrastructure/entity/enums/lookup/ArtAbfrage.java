/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ArtAbfrage implements ILookup {
    
    ERMITTLUNG_SOZINFRA_BEDARF("Ermittlung des durch das Wohnbauvorhaben ausgelösten Infrastrukturbedarfs"),
    ERMITTLUNG_FLAECHE_SOZINFRA_BEDARF("Flächen- / Standortsuche"),
    ABSCHAETZUNG_FLAECHE_FUER_SOZINFRA("Infrastrukturbedarfsermittlung für Flächen ohne Wohnbauvorhaben"),
    STELLUNGNAHME_MITZEICHNUNGSKETTE_BESCHLUSSVORLAGE("Stellungnahme zu Beschlussvorlage");

    @Getter
    private final String bezeichnung;

}