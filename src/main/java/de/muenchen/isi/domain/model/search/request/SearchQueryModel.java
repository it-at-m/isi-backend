package de.muenchen.isi.domain.model.search.request;

import lombok.Data;

@Data
public class SearchQueryModel {

    private String searchQuery;

    private Boolean selectBauleitplanverfahren;

    private Boolean selectWeiteresVerfahren;

    private Boolean selectBaugenehmigungsverfahren;

    private Boolean selectBauvorhaben;

    private Boolean selectGrundschule;

    private Boolean selectGsNachmittagBetreuung;

    private Boolean selectHausFuerKinder;

    private Boolean selectKindergarten;

    private Boolean selectKinderkrippe;

    private Boolean selectMittelschule;

    private Integer page;

    private Integer pageSize;
}
