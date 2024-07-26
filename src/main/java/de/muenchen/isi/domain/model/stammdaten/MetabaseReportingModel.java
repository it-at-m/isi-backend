package de.muenchen.isi.domain.model.stammdaten;

import lombok.Data;

@Data
public class MetabaseReportingModel {

    private String url;

    private String reportsGlobal;

    private String reportErgebnissePlanungsursaechlich;

    private String reportErgebnisseSobonUrsaechlich;

    private String reportWohneinheiten;
}
