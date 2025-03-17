package de.muenchen.isi.infrastructure.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EntityType {
    BAULEITPLANVERFAHREN,
    BAUGENEHMIGUNGSVERFAHREN,
    WEITERES_VERFAHREN,
    KINDERKRIPPE,
    KINDERGARTEN,
    GS_NACHMITTAG_BETREUUNG,
    HAUS_FUER_KINDER,
    GRUNDSCHULE,
    MITTELSCHULE,
    BAUVORHABEN;

    public static class Values {

        public static final String BAULEITPLANVERFAHREN = "BAULEITPLANVERFAHREN";

        public static final String BAUGENEHMIGUNGSVERFAHREN = "BAUGENEHMIGUNGSVERFAHREN";

        public static final String WEITERES_VERFAHREN = "WEITERES_VERFAHREN";

        public static final String KINDERKRIPPE = "KINDERKRIPPE";

        public static final String KINDERGARTEN = "KINDERGARTEN";

        public static final String GS_NACHMITTAG_BETREUUNG = "GS_NACHMITTAG_BETREUUNG";

        public static final String HAUS_FUER_KINDER = "HAUS_FUER_KINDER";

        public static final String GRUNDSCHULE = "GRUNDSCHULE";

        public static final String MITTELSCHULE = "MITTELSCHULE";

        public static final String BAUVORHABEN = "BAUVORHABEN";
    }
}
