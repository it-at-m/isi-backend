package de.muenchen.isi.domain.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SearchResultType {
    ABFRAGE,
    BAUVORHABEN,
    INFRASTRUKTUREINRICHTUNG;

    public static class Values {

        public static final String ABFRAGE = "ABFRAGE";

        public static final String BAUVORHABEN = "BAUVORHABEN";

        public static final String INFRASTRUKTUREINRICHTUNG = "INFRASTRUKTUREINRICHTUNG";
    }
}
