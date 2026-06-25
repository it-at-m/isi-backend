package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Bauratenmethodik implements ILookup {
    ALTE_BAURATENMETHODIK("Alte Bauratenmethodik (bis zu 1.000 WE pro Jahr)"),

    NEUE_BAURATENMETHODIK("Neue Bauratenmethodik (idealtypische Bauraten)");

    @Getter
    private final String bezeichnung;
}
