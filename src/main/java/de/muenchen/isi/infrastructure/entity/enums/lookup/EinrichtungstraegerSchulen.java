package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EinrichtungstraegerSchulen implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),
    STAEDTISCHE_EINRICHTUNG("Städtische Einrichtung"),
    FREIE_GEMEINNUETZIGE_SONSTIGE("Freie / gemeinnützige / sonstige Einrichtungen"),
    STAATLICHE_EINRICHTUNG("Staatliche Einrichtung"),
    PRIVATE_TRAEGERSCHAFT("Private Trägerschaft"),
    KIRCHLICHE_TRAEGERSCHAFT("Kirchliche Trägerschaft");

    @Getter
    private final String bezeichnung;
}
