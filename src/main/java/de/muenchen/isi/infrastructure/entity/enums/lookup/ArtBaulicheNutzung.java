package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
