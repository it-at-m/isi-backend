package de.muenchen.isi.domain.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SearchResultType {
    INFRASTRUKTURABFRAGE,
    BAUVORHABEN,
    INFRASTRUKTUREINRICHTUNG;

    public static class Values {

        public static final String INFRASTRUKTURABFRAGE = "INFRASTRUKTURABFRAGE";

        public static final String BAUVORHABEN = "BAUVORHABEN";

        public static final String INFRASTRUKTUREINRICHTUNG = "INFRASTRUKTUREINRICHTUNG";
    }
}
