package de.muenchen.isi.infrastructure.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EntityType {
    ABFRAGE,
    BAUVORHABEN,
    INFRASTRUKTUREINRICHTUNG;

    public static class Values {

        public static final String ABFRAGE = "ABFRAGE";

        public static final String BAUVORHABEN = "BAUVORHABEN";

        public static final String INFRASTRUKTUREINRICHTUNG = "INFRASTRUKTUREINRICHTUNG";
    }
}
