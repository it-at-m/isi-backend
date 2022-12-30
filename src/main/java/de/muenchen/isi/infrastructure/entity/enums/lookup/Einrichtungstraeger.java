/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Einrichtungstraeger implements ILookup {

    UNSPECIFIED(ILookup.UNSPECIFIED),
    KITA_STAEDTISCHE_EINRICHTUNG("Kita: Städtische Einrichtung"),
    KITA_EINRICHTUNG_BETRIEBSTRAEGERSCHAFT("Kita: Einrichtung in Betriebsträgerschaft"),
    KITA_FREIE_GEMEINNUETZIGE_SONSTIGE("Kita: Freie / gemeinnützige / sonstige Einrichtungen"),
    KITA_EINRICHTUNG_GESAMTSTAEDTISCH("Kita: Einrichtung, deren Plätze nur gesamtstädtisch zur Verfügung stehen"),
    GS_BETREUUNG_STÄDTISCHE_EINRICHTUNG("GS-Betreuung: Städtische Einrichtung"),
    GS_BETREUUNG_ELTERN_KIND_INITIATIVE("GS-Betreuung: Eltern-Kind-Initiative"),
    GS_BETREUUNG_STAATLICHE_EINRICHTUNG("GS-Betreuung: Staatliche Einrichtung"),
    GS_BETREUUNG_PRIVATE_TRAEGERSCHAFT("GS-Betreuung: Private Trägerschaft"),
    GS_BETREUUNG_KIRCHLICHE_TRAEGERSCHAFT("GS-Betreuung: Kirchliche Trägerschaft"),
    SCHULE_STAEDTISCHE_EINRICHTUNG("Schule: Städtische Einrichtung"),
    SCHULE_STAATLICHE_EINRICHTUNG("Schule: Staatliche Einrichtung"),
    SCHULE_PRIVATE_TRAEGERSCHAFT("Schule: Private Trägerschaft"),
    SCHULE_KIRCHLICHE_TRAEGERSCHAFT("Schule: Kirchliche Trägerschaft");

    @Getter
    private final String bezeichnung;

}