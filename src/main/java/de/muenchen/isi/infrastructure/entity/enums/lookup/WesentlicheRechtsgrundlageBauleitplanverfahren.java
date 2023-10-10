package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WesentlicheRechtsgrundlageBauleitplanverfahren {
    UNSPECIFIED(ILookup.UNSPECIFIED),

    QUALIFIZIERTER_BEBAUUNGSPLAN("Qualifizierter Bebauungsplan (§30 Abs. 1 BauGB)"),

    VORHABENSBEZOGENER_BEBAUUNGSPLAN("Vorhabenbezogener Bebauungsplan (§12 BauGB, § 30 Abs. 2 BauGB)"),

    EINFACHER_BEBAUUNGSPLAN("Einfacher Bebauungsplan (§30 Abs. 3 BauGB)"),

    SEKTORALER_BEBAUUNGSPLAN_PARAGRAPH_9("Sektoraler Bebauungsplan (§9 Abs. 2a-d BauGB)"),

    INFO_FEHLT("Info fehlt"),

    FREIE_EINGABE("freie Eingabe");

    @Getter
    private final String bezeichnung;
}
