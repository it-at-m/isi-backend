package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Planart implements ILookup {
    EINFACHER_BEBAUUNGSPLAN("Einfacher Bebauungsplan"),

    QUALIFIZIERTER_BEBAUUNGSPLAN("Qualifizierter Bebauungsplan"),

    VORHABENBEZOGENER_BEBAUUNGSPLAN("Vorhabenbezogener Bebauungsplan"),

    BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG("Bebauungsplan zur Wohnraumversorgung"),

    FREIE_EINGABE("freie Eingabe");

    @Getter
    private final String bezeichnung;
}
