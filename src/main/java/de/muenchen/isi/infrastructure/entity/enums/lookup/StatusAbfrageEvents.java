package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StatusAbfrageEvents {
    FREIGABE(
        "freigabe",
        "FREIGABE",
        1,
        "Die Abfrage wird zur Bearbeitung weitergeleitet und kann nicht mehr geändert werden."
    ),

    IN_BEARBEITUNG_SETZEN(
        "in-bearbeitung-setzen",
        "IN BEARBEITUNG SETZEN",
        2,
        "Die Abfrage wird nun in Bearbeitung gesetzt."
    ),

    ABBRECHEN("abbrechen", "STORNIEREN", 3, "Die Abfrage wird abbgebrochen."),

    ZURUECK_AN_ABFRAGEERSTELLUNG(
        "zurueck-an-abfrageerstellung",
        "ZURÜCK AN ABFRAGEERSTELLUNG",
        4,
        "Die Abfrage wird an den Abfrageersteller zurückgegeben."
    ),

    ABFRAGE_SCHLIESSEN("abfrage-schliessen", "ABFRAGE SCHLIEßEN", 5, "Die Abfrage wird erfolgreich geschlossen."),
    VERSCHICKEN_DER_STELLUNGNAHME(
        "verschicken-der-stellungnahme",
        "AN FACHREFERATE",
        6,
        "Die Abfrage wird an die Fachreferate weitergeleitet."
    ),

    ZURUECK_AN_SACHBEARBEITUNG(
        "zurueck-an-sachbearbeitung",
        "ZURÜCK AN SACHBEARBEITUNG",
        7,
        "Die Abfrage wird an die Sacharbeiter weitergeleitet."
    ),

    BEDARFSMELDUNG_ERFOLGTE(
        "bedarfsmeldung-erfolgt",
        "BEDARF MELDEN",
        8,
        "Die Bedarfsmeldung der Abfrage ist erfolgreich."
    ),

    SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG(
        "speicher-von-soz-infrastruktur-versorgung",
        "ABFRAGE ABSCHLIEßEN",
        9,
        "Die Abfrage wird erfolgreich geschlossen."
    ),

    ERNEUTE_BEARBEITUNG(
        "erneute-bearbeitung",
        "ERNEUTE BEARBEITUNG",
        10,
        "Die Abfrage wird wird an die Sachbearbeiter zur Bearbeitung zurückgesendet."
    );

    @Getter
    private final String url;

    @Getter
    private final String buttonName;

    @Getter
    private final int index;

    @Getter
    private final String dialogText;
}
