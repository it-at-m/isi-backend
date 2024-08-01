package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StatusAbfrageEvents {
    FREIGABE(
        "freigabe",
        "FREIGABE",
        1,
        "Die Abfrage wird zur weiteren Bearbeitung freigegeben.",
        "work-assignment-information.message.freigabe.text",
        "work-assignment-information.message.freigabe.subject"
    ),

    IN_BEARBEITUNG_SETZEN(
        "in-bearbeitung-setzen",
        "IN BEARBEITUNG SETZEN",
        2,
        "Die Abfrage befindet sich in der Bearbeitung.",
        null,
        null
    ),

    ABBRECHEN(
        "abbrechen",
        "STORNIEREN",
        3,
        "Die Abfrage wird widerrufen und dadurch die Bearbeitung beendet.",
        null,
        null
    ),

    ZURUECK_AN_ABFRAGEERSTELLUNG(
        "zurueck-an-abfrageerstellung",
        "ZURÜCK AN ABFRAGEERSTELLUNG",
        4,
        "Die Abfrage wird an die Abfrageerstellung zurückgegeben.",
        null,
        null
    ),

    KEINE_BEARBEITUNG_NOETIG(
        "erledigt-ohne-fachreferat",
        "ABFRAGE SCHLIEßEN",
        5,
        "Die Abfrage wird abgeschlossen.",
        null,
        null
    ),

    VERSCHICKEN_DER_STELLUNGNAHME(
        "verschicken-der-stellungnahme",
        "AN FACHREFERATE",
        6,
        "Die Abfrage wird zur weiteren Bearbeitung weitergeleitet.",
        "work-assignment-information.message.verschicken-der-stellungnahme.text",
        "work-assignment-information.message.verschicken-der-stellungnahme.subject"
    ),

    ZURUECK_AN_SACHBEARBEITUNG(
        "zurueck-an-sachbearbeitung",
        "ZURÜCK AN SACHBEARBEITUNG",
        7,
        "Die Abfrage wird an die Sachbearbeitung zurückgegeben.",
        null,
        null
    ),

    BEDARFSMELDUNG_ERFOLGTE(
        "bedarfsmeldung-erfolgt",
        "BEDARF MELDEN",
        8,
        "Die Bedarfsmeldung der Abfrage ist erfolgt.",
        "work-assignment-information.message.bedarfsmeldung-erfolgte.text",
        "work-assignment-information.message.bedarfsmeldung-erfolgte.subject"
    ),

    SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG(
        "erledigt-mit-fachreferat",
        "ABFRAGE ABSCHLIESSEN",
        9,
        "Die Abfrage wird abgeschlossen.",
        "work-assignment-information.message.speichern-von-sozialinfrastruktur-versorgung.test",
        "work-assignment-information.message.speichern-von-sozialinfrastruktur-versorgung.subject"
    ),

    ERNEUTE_BEARBEITUNG(
        "erneute-bearbeitung-sachbearbeitung",
        "ERNEUTE BEARBEITUNG",
        10,
        "Die Abfrage wird an die Sachbearbeitung zur Bearbeitung zurückgesendet.",
        "work-assignment-information.message.erneute-bearbeitung.text",
        "work-assignment-information.message.erneute-bearbeitung.subject"
    );

    @Getter
    private final String url;

    @Getter
    private final String buttonName;

    @Getter
    private final int index;

    @Getter
    private final String dialogText;

    @Getter
    private final String propertyWorkAssignmentInformationText;

    @Getter
    private final String propertyWorkAssignmentInformationSubject;
}
