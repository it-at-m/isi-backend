package de.muenchen.isi.infrastructure.entity.enums.lookup;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Verfahrensstand implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED, new String[] {}),

    SIMULIERT_VORBEREITUNG_AUFSTELLUNGSBESCHLUSS(
        "Simuliert (Vorbereitung Aufstellungsbeschluss)",
        new String[] { "Vorbereitung", "Aufstellungsbeschluss" }
    ),

    SIMULIERT_VORBEREITUNG_WETTBEWERBAUSLOBUNG(
        "Simuliert (Aufstellungsbeschluss / Vorbereitung Auslobung Wettbewerb)",
        new String[] { "Vorbereitung", "Auslobung", "Wettbewerb" }
    ),

    VORBEREITUNG_FRUEHZEITIGE_BETEILIGUNG(
        "Im Verfahren (Vorentwurf / Vorbereitung frühzeitige Beteiligung)",
        new String[] { "Vorbereitung", "Beteiligung" }
    ),

    VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG(
        "Im Verfahren (Entwurf / Vorbereitung Billigungsbeschluss)",
        new String[] { "Vorbereitung", "Billigungsbeschluss" }
    ),

    VORBEREITUNG_SATZUNGSBESCHLUSS(
        "Im Verfahren (Vorbereitung Plan für Beschlussfassung / Vorbereitung Satzungsbeschluss)",
        new String[] { "Vorbereitung", "Satzungsbeschluss" }
    ),

    INKRAFTGETRETEN_VEROEFFENTLICHUNG_AMTSBLATT(
        "Inkraftgetreten (Veröffentlichung Amtsblatt)",
        new String[] { "Inkrafttreten", "Amtsblatt" }
    ),

    INKRAFTGETRETEN_FOERDERMIXPLAN("Fördermixplan", new String[] { "Fördermixplan" }),

    /* ab hier To-Do: ISI-273, ISI-273 */

    VORBEREITUNG_ECKDATENBESCHLUSS(
        "Vorbereitung Eckdatenbeschluss",
        new String[] { "Vorbereitung", "Eckdatenbeschluss" }
    ),

    VORBEREITUNG_WETTBEWERBAUSLOBUNG(
        "Vorbereitung Wettbewerbsauslobung",
        new String[] { "Vorbereitung", "Wettbewerbsauslobung" }
    ),

    VORLIEGENDER_SATZUNGSBESCHLUSS(
        "Vorliegender Satzungsbeschluss",
        new String[] { "vorliegender", "Satzungsbeschluss" }
    ),

    VORBEREITUNG_AUFSTELLUNGSBESCHLUSS(
        "Vorbereitung Aufstellungsbeschluss (ggf. + Eckdatenbeschluss)",
        new String[] { "Vorbereitung", "Aufstellungsbeschluss", "Eckdatenbeschluss" }
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

    FREIE_EINGABE("freie Eingabe", new String[] { "freie", "Eingabe" }),

    STANDORTABFRAGE("Standortabfrage", new String[] { "Standortabfrage" });

    @Getter
    private final String bezeichnung;

    @Getter
    private final String[] suggestions;

    public static List<Verfahrensstand> getVerfahrensstandForBauleitplanverfahren() {
        return List.of(
            UNSPECIFIED,
            SIMULIERT_VORBEREITUNG_AUFSTELLUNGSBESCHLUSS,
            SIMULIERT_VORBEREITUNG_WETTBEWERBAUSLOBUNG,
            VORBEREITUNG_FRUEHZEITIGE_BETEILIGUNG,
            VORBEREITUNG_BILLIGUNGSBESCHLUSS_STAEDTEBAULICHER_VERTRAG,
            VORBEREITUNG_SATZUNGSBESCHLUSS,
            INKRAFTGETRETEN_VEROEFFENTLICHUNG_AMTSBLATT,
            INKRAFTGETRETEN_FOERDERMIXPLAN,
            FREIE_EINGABE
        );
    }

    public static List<Verfahrensstand> getVerfahrensstandForBaugenehmigungsverfahren() {
        return List.of(UNSPECIFIED, VORBEREITUNG_VORBESCHEID, VORBEREITUNG_BAUGENEHMIGUNG, FREIE_EINGABE);
    }

    public static List<Verfahrensstand> getVerfahrensstandForWeiteresVerfahren() {
        return List.of(
            UNSPECIFIED,
            VORABFRAGE_OHNE_KONKRETEN_STAND,
            STRUKTURKONZEPT,
            RAHMENPLANUNG,
            POTENTIALUNTERSUCHUNG,
            STAEDTEBAULICHE_SANIERUNGSMASSNAHME,
            STAEDTEBAULICHE_ENTWICKLUNGSMASSNAHME,
            FREIE_EINGABE,
            STANDORTABFRAGE
        );
    }
}
