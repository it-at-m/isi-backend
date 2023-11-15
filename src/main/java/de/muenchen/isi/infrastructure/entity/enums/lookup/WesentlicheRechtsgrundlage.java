package de.muenchen.isi.infrastructure.entity.enums.lookup;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WesentlicheRechtsgrundlage implements ILookup {
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

    INNENBEREICH("Innenbereich (§34 BauGB)"),

    AUSSENBEREICH("Außenbereich (§35 BauGB)"),

    BEFREIUNG("Befreiung (§ 31 BauGB)"),

    INFO_FEHLT("Info fehlt"),

    FREIE_EINGABE("freie Eingabe");

    @Getter
    private final String bezeichnung;

    public static List<WesentlicheRechtsgrundlage> getWesentlicheRechtsgrundlageForBauleitplanverfahren() {
        return List.of(
            QUALIFIZIERTER_BEBAUUNGSPLAN,
            VORHABENSBEZOGENER_BEBAUUNGSPLAN,
            EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30,
            SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9,
            INFO_FEHLT,
            FREIE_EINGABE
        );
    }

    public static List<WesentlicheRechtsgrundlage> getWesentlicheRechtsgrundlageForBaugenehmigungsverfahren() {
        return List.of(
            QUALIFIZIERTER_BEBAUUNGSPLAN,
            VORHABENSBEZOGENER_BEBAUUNGSPLAN,
            EINFACHER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35,
            SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_30_IVM_34_35,
            INNENBEREICH,
            AUSSENBEREICH,
            BEFREIUNG,
            INFO_FEHLT,
            FREIE_EINGABE
        );
    }
}
