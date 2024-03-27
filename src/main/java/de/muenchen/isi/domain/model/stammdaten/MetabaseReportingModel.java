package de.muenchen.isi.domain.model.stammdaten;

import lombok.Data;

@Data
public class MetabaseReportingModel {

    private String url;

    private String reportBedarfe;

    private String reportSpitzenbedarfePlanungsursaechlich;

    private String reportSpitzenbedarfeSobonUrsaechlich;

    private String reportWohneinheiten;
}
