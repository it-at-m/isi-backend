package de.muenchen.isi.domain.model.search;

import lombok.Data;

@Data
public class SearchQueryForEntitiesModel {

    private String searchQuery;

    private Boolean selectInfrastrukturabfrage;

    private Boolean selectBauvorhaben;

    private Boolean selectInfrastruktureinrichtung;
}
