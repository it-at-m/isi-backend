package de.muenchen.isi.domain.model.stammdaten;

import lombok.Data;

@Data
public class MetabaseReportingModel {

    private String url;

    private String reportErgebnissePlanungsursaechlich;

    private String reportErgebnisseSobonUrsaechlich;

    private String reportWohneinheiten;

    private String reportBauratendatei;

    private String reportKitaplanungsbereichKrippe;

    private String reportKitaplanungsbereichKiga;

    private String reportAndere;
}
