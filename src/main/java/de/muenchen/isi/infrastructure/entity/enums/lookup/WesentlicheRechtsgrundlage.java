package de.muenchen.isi.infrastructure.entity.enums.lookup;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WesentlicheRechtsgrundlage implements ILookup {
    BEPLANTER_BEREICH_PARAGRAPH_30("Beplanter Bereich (§ 30 BauGB)"),

    INNENBEREICH("Innenbereich (§34 BauGB)"),

    AUSSENBEREICH("Außenbereich (§35 BauGB)"),

    BEBAUUNGSPLAN_ZUR_WOHNRAUMVERSORGUNG_PARAGRAPH_9_IVM_34(
        "Bebauungsplan zur Wohnraumversorgung (§ 9 Abs. 2d i.V.m. § 34 BauGB)"
    ),

    BEPLANTER_BEREICH_PARAGRAPH_30_MIT_BEFREIUNG_PARAGRAPH_31(
        "Beplanter Bereich (§30 BauGB) mit Befreiung (§31 BauGB)"
    ),

    FREIE_EINGABE("freie Eingabe"),

    /* ab hier ISI-280) */

    QUALIFIZIERTER_BEBAUUNGSPLAN("Qualifizierter Bebauungsplan (§30 Abs. 1 BauGB)"),

    VORHABENSBEZOGENER_BEBAUUNGSPLAN("Vorhabenbezogener Bebauungsplan (§12 BauGB, § 30 Abs. 2 BauGB)"),

    EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30("Einfacher Bebauungsplan (§30 Abs. 3 BauGB)"),
    EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35(
        "Einfacher Bebauungsplan (§30 Abs. 3 BauGB i.V.m. §34 BauGB oder i.V.m. §35 BauGB)"
    ),

    SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9("Sektoraler Bebauungsplan (§9 Abs. 2a-d BauGB)"),

    SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35(
        "Sektoraler Bebauungsplan (§30 Abs. 3 BauGB i.V.m. §34 BauGB oder i.V.m. §35 BauGB)"
    ),

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
}
