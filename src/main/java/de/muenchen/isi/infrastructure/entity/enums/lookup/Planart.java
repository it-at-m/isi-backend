package de.muenchen.isi.infrastructure.entity.enums.lookup;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Planart implements ILookup {
    EINFACHER_BEBAUUNGSPLAN("Einfacher Bebauungsplan"),

    QUALIFIZIERTER_BEBAUUNGSPLAN("Qualifizierter Bebauungsplan"),

    VORHABENSBEZOGENER_BEBAUUNGSPLAN("Vorhabenbezogener Bebauungsplan"),

    BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG("Bebauungsplan zur Wohnraumversorgung"),

    FREIE_EINGABE("freie Eingabe");

    @Getter
    private final String bezeichnung;

    public static List<Planart> getPlanart() {
        return List.of(
            EINFACHER_BEBAUUNGSPLAN,
            QUALIFIZIERTER_BEBAUUNGSPLAN,
            VORHABENSBEZOGENER_BEBAUUNGSPLAN,
            BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG,
            FREIE_EINGABE
        );
    }
}
