package de.muenchen.isi.infrastructure.entity.enums.lookup;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum InfrastruktureinrichtungArt implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),
    GANZTAGSBETREUUNG_GRUNDSCHULKINDER("Ganztagsbetreuung Grundschulkinder"),
    SCHULEN("SCHULEN"),
    SOZIALE_EINRICHTUNGEN("Soziale Einrichtungen");

    @Getter
    private final String bezeichnung;
}
