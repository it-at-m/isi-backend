package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum VersorgungsquoteHortSobon implements ILookup {
    FUENFIZG_PROZENT("55 %", 0.550),
    NEUNZIG_PROZENT("90 %", 0.900);

    @Getter
    private final String bezeichnung;

    @Getter
    private final double wert;
}
