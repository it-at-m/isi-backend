package de.muenchen.isi.api.dto.stammdaten;

import lombok.Data;

@Data
public class MetabaseReportingDto {

    private String url;

    private String reportErgebnissePlanungsursaechlich;

    private String reportErgebnisseSobonUrsaechlich;

    private String reportWohneinheiten;

    private final String reportBauratendatei;

    private final String reportKitaplanungsbereichKrippe;

    private final String reportKitaplanungsbereichKiga;

    private final String reportAndere;
}
