package de.muenchen.isi.api.dto.stammdaten;

import lombok.Data;

@Data
public class MetabaseReportingDto {

    private String url;

    private String reportBedarfe;

    private String reportSpitzenbedarfePlanungsursaechlich;

    private String reportSpitzenbedarfeSobonUrsaechlich;

    private String reportWohneinheiten;
}
