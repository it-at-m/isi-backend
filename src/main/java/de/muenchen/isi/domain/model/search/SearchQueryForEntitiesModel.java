package de.muenchen.isi.domain.model.search;

import lombok.Data;

@Data
public class SearchQueryForEntitiesModel {

    private String searchQuery;

    private Boolean selectInfrastrukturabfrage;

    private Boolean selectBauvorhaben;

    private Boolean selectGrundschule;

    private Boolean selectGsNachmittagBetreuung;

    private Boolean selectHausFuerKinder;

    private Boolean selectKindergarten;

    private Boolean selectKinderkrippe;

    private Boolean selectMittelschule;
}
