package de.muenchen.isi.infrastructure.entity.enums.lookup;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WesentlicheRechtsgrundlage implements ILookup {
    BEPLANTER_BEREICH_PARAGRAPH_30("Beplanter Bereich (§ 30 BauGB)"),

    INNENBEREICH("Innenbereich (§ 34 BauGB)"),

    AUSSENBEREICH("Außenbereich (§ 35 BauGB)"),

    BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG_PARAGRAPH_9_IVM_34(
        "Bebauungsplan zur Wohnraumversorgung (§ 9 Abs. 2d i.V.m. § 34 BauGB)"
    ),

    BEPLANTER_BEREICH_PARAGRAPH_30_MIT_BEFREIUNG_PARAGRAPH_31(
        "Beplanter Bereich (§ 30 BauGB) mit Befreiung (§ 31 BauGB)"
    ),

    FREIE_EINGABE("Freie Eingabe"),

    EINFACHER_BEBAUUNGSPLAN("Einfacher Bebauungsplan"),

    QUALIFIZIERTER_BEBAUUNGSPLAN("Qualifizierter Bebauungsplan"),

    VORHABENSBEZOGENER_BEBAUUNGSPLAN("Vorhabenbezogener Bebauungsplan"),

    BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG("Bebauungsplan zur Wohnraumversorgung"),

    BEFREIUNG("Befreiung (§ 31 BauGB)");

    @Getter
    private final String bezeichnung;

    public static List<WesentlicheRechtsgrundlage> getWesentlicheRechtsgrundlageForBaugenehmigungsverfahren() {
        return List.of(
            BEPLANTER_BEREICH_PARAGRAPH_30,
            INNENBEREICH,
            AUSSENBEREICH,
            BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG_PARAGRAPH_9_IVM_34,
            BEPLANTER_BEREICH_PARAGRAPH_30_MIT_BEFREIUNG_PARAGRAPH_31,
            FREIE_EINGABE
        );
    }

    public static List<WesentlicheRechtsgrundlage> getWesentlicheRechtsgrundlageForWeiteresVerfahren() {
        return List.of(
            EINFACHER_BEBAUUNGSPLAN,
            QUALIFIZIERTER_BEBAUUNGSPLAN,
            VORHABENSBEZOGENER_BEBAUUNGSPLAN,
            BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG,
            INNENBEREICH,
            AUSSENBEREICH,
            BEFREIUNG,
            FREIE_EINGABE
        );
    }

    public static List<WesentlicheRechtsgrundlage> getWesentlicheRechtsgrundlageForBauvorhaben() {
        return List.of(
            EINFACHER_BEBAUUNGSPLAN,
            QUALIFIZIERTER_BEBAUUNGSPLAN,
            VORHABENSBEZOGENER_BEBAUUNGSPLAN,
            BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG,
            BEPLANTER_BEREICH_PARAGRAPH_30,
            INNENBEREICH,
            AUSSENBEREICH,
            BEFREIUNG,
            FREIE_EINGABE
        );
    }
}
