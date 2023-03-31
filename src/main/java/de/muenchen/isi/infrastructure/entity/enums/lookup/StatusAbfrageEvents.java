package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StatusAbfrageEvents {
    FREIGABE("freigabe", "FREIGABE", 1),

    IN_BEARBEITUNG_SETZEN("in-bearbeitung-setzen", "IN BEARBEITUNG SETZEN", 2),

    ABBRECHEN("abbrechen", "STORNIEREN", 3),

    ZURUECK_AN_ABFRAGEERSTELLER("zurueck-an-abfrageersteller", "ZURÜCK AN ABFRAGEERSTELLER", 4),

    ABFRAGE_SCHLIESSEN("abfrage-schliessen", "ABFRAGE SCHLIEßEN", 5),
    VERSCHICKEN_DER_STELLUNGNAHME("verschicken-der-stellungnahme", "AN FACHREFERATE", 6),

    ZURUECK_AN_PLAN("zurueck-an-plan", "ZURÜCK AN PLAN", 7),

    BEDARFSMELDUNG_ERFOLGTE("bedarfsmeldung-erfolgt", "BEDARF MELDEN", 8),

    SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG("speicher-von-soz-infrastruktur-versorgung", "ABFRAGE ABSCHLIEßEN", 9),

    ERNEUTE_BEARBEITUNG("erneute-bearbeitung", "ERNEUTE BEARBEITUNG", 10);

    @Getter
    private final String url;

    @Getter
    private final String buttonName;

    @Getter
    private final int index;
}
