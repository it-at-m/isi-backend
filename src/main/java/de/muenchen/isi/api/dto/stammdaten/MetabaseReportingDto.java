package de.muenchen.isi.api.dto.stammdaten;

import lombok.Data;

@Data
public class MetabaseReportingDto {

    private String url;

    private String reportsGlobal;

    private String reportBedarfe;

    private String reportErgebnissePlanungsursaechlich;

    private String reportErgebnisseSobonUrsaechlich;

    private String reportSpitzenbedarfePlanungsursaechlich;

    private String reportSpitzenbedarfeSobonUrsaechlich;

    private String reportWohneinheiten;
}
