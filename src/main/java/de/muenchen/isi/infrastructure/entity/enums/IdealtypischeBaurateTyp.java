package de.muenchen.isi.infrastructure.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum IdealtypischeBaurateTyp {
    ANZAHL_WOHNEINHEITEN_GESAMT("Wohneinheiten"),
    GESCHOSSFLAECHE_WOHNEN_GESAMT("Geschoßfläche Wohnen");

    @Getter
    private final String bezeichnung;
}
