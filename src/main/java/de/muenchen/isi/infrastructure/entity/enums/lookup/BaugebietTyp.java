package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BaugebietTyp implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),
    MI("Mischgebiet (MI)"),
    WA("Allgemeines Wohngebiet (WA)"),
    MU("Urbanes Gebiet (MU)"),
    MK("Kerngebiet (MK)"),
    WR("Reines Wohngebiet (WR)"),
    GE("Gewerbegebiet (GE)");

    @Getter
    private final String bezeichnung;
}
