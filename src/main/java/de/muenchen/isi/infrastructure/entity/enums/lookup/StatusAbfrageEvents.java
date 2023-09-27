package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StatusAbfrageEvents {
    FREIGABE("freigabe", "FREIGABE", 1, "Die Abfrage wird zur weiteren Bearbeitung freigegeben."),

    IN_BEARBEITUNG_SETZEN(
        "in-bearbeitung-setzen",
        "IN BEARBEITUNG SETZEN",
        2,
        "Die Abfrage befindet sich in der Bearbeitung."
    ),

    ABBRECHEN("abbrechen", "STORNIEREN", 3, "Die Abfrage wird widerrufen und dadurch die Bearbeitung beendet."),

    ZURUECK_AN_ABFRAGEERSTELLUNG(
        "zurueck-an-abfrageerstellung",
        "ZURÜCK AN ABFRAGEERSTELLUNG",
        4,
        "Die Abfrage wird an die Abfrageerstellung zurückgegeben."
    ),

    ABFRAGE_SCHLIESSEN(
        "abfrage-schliessen",
        "ABFRAGE ABSCHLIESSEN",
        5,
        "Die Abfrage wird abgeschlossen. Sie können eine Anmerkung hinzufügen."
    ),
    VERSCHICKEN_DER_STELLUNGNAHME(
        "verschicken-der-stellungnahme",
        "AN FACHREFERATE",
        6,
        "Die Abfrage wird zur weiteren Bearbeitung weitergeleitet."
    ),

    ZURUECK_AN_SACHBEARBEITUNG(
        "zurueck-an-sachbearbeitung",
        "ZURÜCK AN SACHBEARBEITUNG",
        7,
        "Die Abfrage wird an die Sachbearbeitung zurückgegeben."
    ),

    BEDARFSMELDUNG_ERFOLGTE(
        "bedarfsmeldung-erfolgt",
        "BEDARF MELDEN",
        8,
        "Die Bedarfsmeldung der Abfrage ist erfolgt."
    ),

    SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG(
        "speicher-von-soz-infrastruktur-versorgung",
        "ABFRAGE ABSCHLIESSEN",
        9,
        "Die Abfrage wird abgeschlossen."
    ),

    ERNEUTE_BEARBEITUNG(
        "erneute-bearbeitung",
        "ERNEUTE BEARBEITUNG",
        10,
        "Die Abfrage wird wird an die Sachbearbeitung zur Bearbeitung zurückgesendet."
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
