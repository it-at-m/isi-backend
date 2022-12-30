/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Planungsrecht implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),
    BPLAN_PARAG_30("Bebauungsplan: § 30"),
    BPLAN_PARAG_12("Bebauungsplan: § 12 vorhabenbezogen"),
    BPLAN_PARAG_11("Bebauungsplan: § 11 SoBoN"),
    BPLAN_AEND_BBPLAN("Bebauungsplan: Änderung"),
    NACHVERD_PARAG_34("Nachverdichtung: § 34 Innenbereich"),
    NACHVERD_PARAG_35("Nachverdichtung: § 35 Außenbereich"),
    NACHVERD_PARAG_31("Nachverdichtung: § 31 Befreiung Bplan"),
    NACHVERD_BAURECHTSAUSSCHOEPFUNG("Nachverdichtung: Baurechtsausschöpfung (z.B. sektoraler Bebauungsplan)"),
    SONSTIGES_UMSTRUKTURIERUNG("Sonstiges: Umstrukturierung"),
    SONSTIGES_PARAG_165("Sonstiges: § 165 SEM"),
    SONSTIGES_PARAG_246("Sonstiges: § 246 Flüchtlinge");

    @Getter
    private final String bezeichnung;

}