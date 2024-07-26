package de.muenchen.isi.api.dto.stammdaten;

import lombok.Data;

@Data
public class MetabaseReportingDto {

    private String url;

    private String reportsGlobal;

    private String reportErgebnissePlanungsursaechlich;

    private String reportErgebnisseSobonUrsaechlich;

    private String reportWohneinheiten;
}
