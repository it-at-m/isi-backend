package de.muenchen.isi.infrastructure.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum IdealtypischeBaurateTyp {
    WOHNEINHEITEN("Wohneinheiten"),
    GESCHOSSFLAECHE_WOHNEN("Geschoßfläche Wohnen");

    @Getter
    private final String bezeichnung;
}
