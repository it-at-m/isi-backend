package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StatusAbfrageEvents {

    FREIGABE("freigabe", "FREIGABE", 1),

    IN_BEARBEITUNG_SETZEN("in-bearbeitung-setzen", "IN BEARBEITUNG SETZEN", 2),

    ABBRECHEN("abbrechen", "STONIEREN", 3),

    ANGABEN_ANPASSEN("angabe-anpassen", "ANGABEN ANPASSEN", 4),

    KORRIGIEREN("korrigieren", "KORRIGIEREN", 5),

    VERSCHICKEN_DER_STELLUNGNAHME("verschicken-der-stellungnahme", "VERSCHICKEN STELLUNGNAHME", 6),


    BEDARFSMELDUNG_ERFOLGTE("bedarfsmeldung-erfolgt", "BEDARFSMELDUNG", 7),

    SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG("speicher-von-soz-infrastruktur-versorgung", "VERSORGUNG SPEICHERN", 8),

    KEINE_BEARBEITUNG_NOETIG("keine-bearbeitung-noetig", "KEINE BEARBEITUNG", 9);
    @Getter
    private final String url;

    @Getter
    private final String buttonName;

    @Getter
    private final int index;
}
