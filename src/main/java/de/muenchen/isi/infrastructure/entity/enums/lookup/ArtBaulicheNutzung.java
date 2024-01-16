package de.muenchen.isi.infrastructure.entity.enums.lookup;

import de.muenchen.isi.infrastructure.entity.Baugebiet;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * In {@link Baugebiet#getArtBaulicheNutzung()} wird der Enum
 * nicht mittels @Enumerated(EnumType.STRING) als String repräsentiert.
 * Somit sind neue Enumausprägungen am Ende der Enumration anzufügen.
 */
@AllArgsConstructor
public enum ArtBaulicheNutzung implements ILookup {
    WR("Reines Wohngebiet (WR)"),
    WA("Allgemeines Wohngebiet (WA)"),
    MU("Urbanes Gebiet (MU)"),
    MK("Kerngebiet (MK)"),
    MI("Mischgebiet (MI)"),
    GE("Gewerbegebiet (GE)");

    @Getter
    private final String bezeichnung;
}
