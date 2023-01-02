package de.muenchen.isi.infrastructure.entity.enums.lookup;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ILookup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum InfrastruktureinrichtungTyp implements ILookup {
    UNSPECIFIED(ILookup.UNSPECIFIED),
    KINDERKRIPPE("Kinderkrippe"),
    KINDERGARTEN("Kindergarten"),
    GS_NACHMITTAG_BETREUUNG("Nachmittagsbetreuung für Grundschulkinder"),
    HAUS_FUER_KINDER("Haus für Kinder"),
    GRUNDSCHULE("Grundschule"),
    MITTELSCHULE("Mittelschule");

    @Getter
    private final String bezeichnung;

}
