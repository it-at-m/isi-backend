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
    GESICHERTE_PLANUNG_NEUE_EINR(
        "gesicherte Planung – neue Einrichtung",
        new String[] { "gesicherte", "Planung", "neue", "Einrichtung" }
    ),
    GESICHERTE_PLANUNG_ERW_PLAETZE_BEST_EINR(
        "gesicherte Planung – Erweiterung Plätze bei bestehenden Einrichtungen",
        new String[] { "gesicherte", "Planung", "Erweiterung", "Plätze", "bei", "bestehenden", "Einrichtungen" }
    ),
    GESICHERTE_PLANUNG_TF_KITA_STANDORT(
        "gesicherte Planung – TF Kita Standort",
        new String[] { "gesicherte", "Planung", "TF", "Kita", "Standort" }
    ),
    GESICHERTE_PLANUNG_REDUZIERUNG_PLAETZE(
        "gesicherte Planung – Reduzierung Plätze bei bestehenden Einrichtungen",
        new String[] { "gesicherte", "Planung", "Reduzierung", "Plätze", "bei", "bestehenden", "Einrichtungen" }
    ),
    GESICHERTE_PLANUNG_INTERIMSSTANDORT(
        "gesicherte Planung – Interimsstandort",
        new String[] { "gesicherte", "Planung", "Interimsstandort" }
    ),
    UNGESICHERTE_PLANUNG_TF_KITA_STANDORT(
        "ungesicherte Planung – TF Kita Standort",
        new String[] { "ungesicherte", "Planung", "TF", "Kita", "Standort" }
    ),
    BESTAND("Bestand", new String[] { "Bestand" });

    @Getter
    private final String bezeichnung;

    @Getter
    private final String[] suggestions;
}
