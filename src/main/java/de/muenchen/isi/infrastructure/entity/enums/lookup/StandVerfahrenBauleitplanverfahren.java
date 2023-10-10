package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StandVerfahrenBauleitplanverfahren {
    UNSPECIFIED(ILookup.UNSPECIFIED, new String[] {}),

    VORBEREITUNG_ECKDATENBESCHLUSS(
        "Vorbereitung Eckdatenbeschluss",
        new String[] { "Vorbereitung", "Eckdatenbeschluss" }
    ),

    VORBEREITUNG_WETTBEWERBAUSLOBUNG(
        "Vorbereitung Wettbewerbsauslobung",
        new String[] { "Vorbereitung", "Wettbewerbsauslobung" }
    ),

    VORBEREITUNG_AUFSTELLUNGSBESCHLUSS(
        "Vorbereitung Aufstellungsbeschluss",
        new String[] { "Vorbereitung", "Aufstellungsbeschluss" }
    ),

    VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG(
        "Vorbereitung Billigungsbeschluss / Städtebaulicher Vertrag",
        new String[] { "Vorbereitung", "Billigungsbeschluss", "Städtebaulicher", "Vertrag" }
    ),

    VORLIEGENDER_SATZUNGSBESCHLUSS(
        "vorliegender Satzungsbeschluss",
        new String[] { "vorliegender", "Satzungsbeschluss" }
    ),

    RECHTSVERBINDLICHKEIT_AMTSBLATT(
        "Rechtsverbindlichkeit (Amtsblatt)",
        new String[] { "Rechtsverbindlichkeit", "Amtsblatt" }
    ),

    AUFTEILUNGSPLAN("Aufteilungsplan", new String[] { "Aufteilungsplan" }),

    INFO_FEHLT("Info fehlt", new String[] { "Info", "fehlt" }),

    FREIE_EINGABE("freie Eingabe", new String[] { "freie", "Eingabe" });

    @Getter
    private final String bezeichnung;

    @Getter
    private final String[] suggestions;
}
