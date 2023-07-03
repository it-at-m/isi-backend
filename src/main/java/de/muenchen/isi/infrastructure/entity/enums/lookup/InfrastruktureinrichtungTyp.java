package de.muenchen.isi.infrastructure.entity.enums.lookup;

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

    public static class Values {

        public static final String UNSPECIFIED = "UNSPECIFIED";

        public static final String KINDERKRIPPE = "KINDERKRIPPE";

        public static final String KINDERGARTEN = "KINDERGARTEN";

        public static final String GS_NACHMITTAG_BETREUUNG = "GS_NACHMITTAG_BETREUUNG";

        public static final String HAUS_FUER_KINDER = "HAUS_FUER_KINDER";

        public static final String GRUNDSCHULE = "GRUNDSCHULE";

        public static final String MITTELSCHULE = "MITTELSCHULE";
    }
}
