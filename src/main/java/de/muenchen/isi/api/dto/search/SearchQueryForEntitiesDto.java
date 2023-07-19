package de.muenchen.isi.api.dto.search;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchQueryForEntitiesDto {

    @NotNull
    private String searchQuery;

    @NotNull
    private Boolean selectInfrastrukturabfrage;

    @NotNull
    private Boolean selectBauvorhaben;

    @NotNull
    private Boolean selectGrundschule;

    @NotNull
    private Boolean selectGsNachmittagBetreuung;

    @NotNull
    private Boolean selectHausFuerKinder;

    @NotNull
    private Boolean selectKindergarten;

    @NotNull
    private Boolean selectKinderkrippe;

    @NotNull
    private Boolean selectMittelschule;
}