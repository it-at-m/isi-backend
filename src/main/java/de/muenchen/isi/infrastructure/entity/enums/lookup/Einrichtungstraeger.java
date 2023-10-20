/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Einrichtungstraeger implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),
    STAEDTISCHE_EINRICHTUNG("Städtische Einrichtung"),
    EINRICHTUNG_BETRIEBSTRAEGERSCHAFT("Einrichtung in Betriebsträgerschaft"),
    FREIE_GEMEINNUETZIGE_SONSTIGE("Freie / gemeinnützige / sonstige Einrichtungen"),
    EINRICHTUNG_GESAMTSTAEDTISCH("Einrichtung, deren Plätze nur gesamtstädtisch zur Verfügung stehen"),
    ELTERN_KIND_INITIATIVE("Eltern-Kind-Initiative"),
    STAATLICHE_EINRICHTUNG("Staatliche Einrichtung"),
    PRIVATE_TRAEGERSCHAFT("Private Trägerschaft"),
    KIRCHLICHE_TRAEGERSCHAFT("Kirchliche Trägerschaft");

    @Getter
    private final String bezeichnung;

    public static List<Einrichtungstraeger> getEinrichtungstraegerSchulen() {
        return List.of(
            UNSPECIFIED,
            STAEDTISCHE_EINRICHTUNG,
            FREIE_GEMEINNUETZIGE_SONSTIGE,
            STAATLICHE_EINRICHTUNG,
            PRIVATE_TRAEGERSCHAFT,
            KIRCHLICHE_TRAEGERSCHAFT
        );
    }

    public static List<Einrichtungstraeger> getEinrichtungstraeger() {
        return List.of(
            UNSPECIFIED,
            STAEDTISCHE_EINRICHTUNG,
            EINRICHTUNG_BETRIEBSTRAEGERSCHAFT,
            FREIE_GEMEINNUETZIGE_SONSTIGE,
            EINRICHTUNG_GESAMTSTAEDTISCH,
            ELTERN_KIND_INITIATIVE,
            STAATLICHE_EINRICHTUNG
        );
    }
}
