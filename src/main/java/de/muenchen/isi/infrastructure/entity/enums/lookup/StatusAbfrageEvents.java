package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StatusAbfrageEvents {

    FREIGABE("freigabe", "FREIGABE", 1),

    IN_BEARBEITUNG_SETZEN("in-bearbeitung-setzten", "BEARBEITEN", 2),

    ABBRECHEN("abbrechen", "STONIEREN", 3),

    ANGABEN_ANPASSEN("angabe-anpassen", "", 4),

    KORRIGIEREN("korrigieren", "", 5),

    VERSCHICKEN_DER_STELLUNGNAHME("verschicken-der-stellungnahme", "", 6),


    BEDARFSMELDUNG_ERFOLGTE("bedarfsmeldung-erfolgt", "", 7),

    SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG("speicher-von-soz-infrastruktur-versorgung", "", 8),

    KEINE_BEARBEITUNG_NOETIG("keine-bearbeitung-noetig", "", 9);
    @Getter
    private final String url;

    @Getter
    private final String buttonName;

    @Getter
    private final int index;
}
