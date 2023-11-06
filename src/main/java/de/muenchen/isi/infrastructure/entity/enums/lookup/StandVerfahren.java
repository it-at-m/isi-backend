package de.muenchen.isi.infrastructure.entity.enums.lookup;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StandVerfahren implements ILookup {
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
        "Vorbereitung Aufstellungsbeschluss (ggf. + Eckdatenbeschluss)",
        new String[] { "Vorbereitung", "Aufstellungsbeschluss", "Eckdatenbeschluss" }
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

    VORBEREITUNG_VORBESCHEID("Vorbereitung Vorbescheid", new String[] { "Vorbereitung", "Vorbescheid" }),

    VORBEREITUNG_BAUGENEHMIGUNG("Vorbereitung Baugenehmigung", new String[] { "Vorbereitung", "Baugenehmigung" }),

    VORABFRAGE_OHNE_KONKRETEN_STAND(
        "Vorabfrage ohne konkreten Stand",
        new String[] { "Vorabfrage", "ohne", "konkreten", "Stand" }
    ),

    STRUKTURKONZEPT("Strukturkonzept", new String[] { "Strukturkonzept" }),

    RAHMENPLANUNG("Rahmenplanung", new String[] { "Rahmenplanung" }),

    POTENTIALUNTERSUCHUNG("Potentialuntersuchung", new String[] { "Potentialuntersuchung" }),

    STAEDTEBAULICHE_SANIERUNGSMASSNAHME(
        "Städtebauliche Sanierungsmaßnahme (Sanierungsgebiet)",
        new String[] { "Städtebauliche", "Sanierungsmaßnahme", "Sanierungsgebiet" }
    ),

    STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME(
        "Städtebauliche Entwicklungsmaßnahme (Entwicklungsgebiet)",
        new String[] { "Städtebauliche", "Entwicklungsmaßnahme", "Entwicklungsgebiet" }
    ),

    INFO_FEHLT("Info fehlt", new String[] { "Info", "fehlt" }),

    FREIE_EINGABE("freie Eingabe", new String[] { "freie", "Eingabe" });

    @Getter
    private final String bezeichnung;

    @Getter
    private final String[] suggestions;

    public static List<StandVerfahren> getStandVerfahrenForBauleitplanverfahren() {
        return List.of(
            UNSPECIFIED,
            VORBEREITUNG_ECKDATENBESCHLUSS,
            VORBEREITUNG_WETTBEWERBAUSLOBUNG,
            VORBEREITUNG_AUFSTELLUNGSBESCHLUSS,
            VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG,
            VORLIEGENDER_SATZUNGSBESCHLUSS,
            RECHTSVERBINDLICHKEIT_AMTSBLATT,
            AUFTEILUNGSPLAN,
            INFO_FEHLT,
            FREIE_EINGABE
        );
    }

    public static List<StandVerfahren> getStandVerfahrenForBaugenehmigungsverfahren() {
        return List.of(UNSPECIFIED, VORBEREITUNG_VORBESCHEID, VORBEREITUNG_BAUGENEHMIGUNG, INFO_FEHLT, FREIE_EINGABE);
    }

    public static List<StandVerfahren> getStandVerfahrenForWeiteresVerfahren() {
        return List.of(
            UNSPECIFIED,
            VORABFRAGE_OHNE_KONKRETEN_STAND,
            STRUKTURKONZEPT,
            RAHMENPLANUNG,
            POTENTIALUNTERSUCHUNG,
            STAEDTEBAULICHE_SANIERUNGSMASSNAHME,
            STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME,
            INFO_FEHLT,
            FREIE_EINGABE
        );
    }
}
