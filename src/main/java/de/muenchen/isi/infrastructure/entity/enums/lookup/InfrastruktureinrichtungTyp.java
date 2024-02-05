package de.muenchen.isi.infrastructure.entity.enums.lookup;

import de.muenchen.isi.infrastructure.entity.Bedarfsmeldung;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import jakarta.persistence.DiscriminatorValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * In {@link Bedarfsmeldung#getInfrastruktureinrichtungTyp()} wird der Enum
 * nicht mittels @Enumerated(EnumType.STRING) als String repräsentiert.
 * Somit sind neue Enumausprägungen am Ende der Enumration anzufügen.
 */
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

    /**
     * Werden in den Entitäten der {@link Infrastruktureinrichtung} als {@link DiscriminatorValue} verwendet.
     */
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
