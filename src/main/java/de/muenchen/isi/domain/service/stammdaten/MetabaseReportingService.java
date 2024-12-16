package de.muenchen.isi.domain.service.stammdaten;

import de.muenchen.isi.domain.model.stammdaten.MetabaseReportingModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MetabaseReportingService {

    private final String url;

    private final String reportErgebnissePlanungsursaechlich;

    private final String reportErgebnisseSobonUrsaechlich;

    private final String reportWohneinheiten;

    private final String reportBauratendatei;

    private final String reportKitaplanungsbereichKrippe;

    private final String reportKitaplanungsbereichKiga;

    private final String reportAndere;

    public MetabaseReportingService(
        @Value("${metabase.reporting.url}") final String url,
        @Value(
            "${metabase.reporting.reports.report.ergebnisse.planungsursaechlich}"
        ) final String reportErgebnissePlanungsursaechlich,
        @Value(
            "${metabase.reporting.reports.report.ergebnisse.sobonursaechlich}"
        ) final String reportErgebnisseSobonUrsaechlich,
        @Value("${metabase.reporting.reports.report.wohneinheiten}") final String reportWohneinheiten,
        @Value("${metabase.reporting.reports.report.bauratendatei}") final String reportBauratendatei,
        @Value(
            "${metabase.reporting.reports.report.kitaplanungsbereichKrippe}"
        ) final String reportKitaplanungsbereichKrippe,
        @Value(
            "${metabase.reporting.reports.report.kitaplanungsbereichKiga}"
        ) final String getReportKitaplanungsbereichKiga,
        @Value("${metabase.reporting.reports.report.andere}") final String reportAndere
    ) {
        this.url = url;
        this.reportWohneinheiten = reportWohneinheiten;
        this.reportErgebnissePlanungsursaechlich = reportErgebnissePlanungsursaechlich;
        this.reportErgebnisseSobonUrsaechlich = reportErgebnisseSobonUrsaechlich;
        this.reportBauratendatei = reportBauratendatei;
        this.reportKitaplanungsbereichKrippe = reportKitaplanungsbereichKrippe;
        this.reportKitaplanungsbereichKiga = getReportKitaplanungsbereichKiga;
        this.reportAndere = reportAndere;
    }

    /**
     * @return Informationen über den Aufruf von Metabase und die aufrufbaren Reports
     */
    public MetabaseReportingModel getMetabaseReporting() {
        final var metabaseReportingModel = new MetabaseReportingModel();
        metabaseReportingModel.setUrl(this.url);
        metabaseReportingModel.setReportErgebnissePlanungsursaechlich(this.reportErgebnissePlanungsursaechlich);
        metabaseReportingModel.setReportErgebnisseSobonUrsaechlich(this.reportErgebnisseSobonUrsaechlich);
        metabaseReportingModel.setReportWohneinheiten(this.reportWohneinheiten);
        metabaseReportingModel.setReportBauratendatei(this.reportBauratendatei);
        metabaseReportingModel.setReportKitaplanungsbereichKrippe(this.reportKitaplanungsbereichKrippe);
        metabaseReportingModel.setReportKitaplanungsbereichKiga(this.reportKitaplanungsbereichKiga);
        metabaseReportingModel.setReportAndere(this.reportAndere);
        return metabaseReportingModel;
    }
}
