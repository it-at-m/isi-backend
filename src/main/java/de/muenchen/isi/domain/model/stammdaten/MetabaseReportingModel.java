package de.muenchen.isi.domain.model.stammdaten;

import lombok.Data;

@Data
public class MetabaseReportingModel {

    private String url;

    private String reportsGlobal;

    private String reportBedarfe;

    private String reportErgebnissePlanungsursaechlich;

    private String reportErgebnisseSobonUrsaechlich;

    private String reportSpitzenbedarfePlanungsursaechlich;

    private String reportSpitzenbedarfeSobonUrsaechlich;

    private String reportWohneinheiten;
}
