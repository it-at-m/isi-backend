package de.muenchen.isi.domain.service.stammdaten;

import de.muenchen.isi.domain.model.stammdaten.MetabaseReportingModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MetabaseReportingService {

    private final String url;

    private final String reportBedarfe;

    private final String reportErgebnissePlanungsursaechlich;

    private final String reportErgebnisseSobonUrsaechlich;

    private final String reportSpitzenbedarfePlanungsursaechlich;

    private final String reportSpitzenbedarfeSobonUrsaechlich;

    private final String reportWohneinheiten;

    public MetabaseReportingService(
        @Value("${metabase.reporting.url}") final String url,
        @Value("${metabase.reporting.reports.report.bedarfe}") final String reportBedarfe,
        @Value(
            "${metabase.reporting.reports.report.ergebnisse.planungsursaechlich}"
        ) final String reportErgebnissePlanungsursaechlich,
        @Value(
            "${metabase.reporting.reports.report.ergebnisse.sobonursaechlich}"
        ) final String reportErgebnisseSobonUrsaechlich,
        @Value(
            "${metabase.reporting.reports.report.spitzenbedarfe.planungsursaechlich}"
        ) final String reportSpitzenbedarfePlanungsursaechlich,
        @Value(
            "${metabase.reporting.reports.report.spitzenbedarfe.sobon.ursaechlich}"
        ) final String reportSpitzenbedarfeSobonUrsaechlich,
        @Value("${metabase.reporting.reports.report.wohneinheiten}") final String reportWohneinheiten
    ) {
        this.url = url;
        this.reportBedarfe = reportBedarfe;
        this.reportSpitzenbedarfePlanungsursaechlich = reportSpitzenbedarfePlanungsursaechlich;
        this.reportSpitzenbedarfeSobonUrsaechlich = reportSpitzenbedarfeSobonUrsaechlich;
        this.reportWohneinheiten = reportWohneinheiten;
        this.reportErgebnissePlanungsursaechlich = reportErgebnissePlanungsursaechlich;
        this.reportErgebnisseSobonUrsaechlich = reportErgebnisseSobonUrsaechlich;
    }

    /**
     * @return Informationen über den Aufruf von Metabase und die aufrufbaren Reports
     */
    public MetabaseReportingModel getMetabaseReporting() {
        final var metabaseReportingModel = new MetabaseReportingModel();
        metabaseReportingModel.setUrl(this.url);
        metabaseReportingModel.setReportBedarfe(this.reportBedarfe);
        metabaseReportingModel.setReportErgebnissePlanungsursaechlich(this.reportErgebnissePlanungsursaechlich);
        metabaseReportingModel.setReportErgebnisseSobonUrsaechlich(this.reportErgebnisseSobonUrsaechlich);
        metabaseReportingModel.setReportSpitzenbedarfePlanungsursaechlich(this.reportSpitzenbedarfePlanungsursaechlich);
        metabaseReportingModel.setReportSpitzenbedarfeSobonUrsaechlich(this.reportSpitzenbedarfeSobonUrsaechlich);
        metabaseReportingModel.setReportWohneinheiten(this.reportWohneinheiten);
        return metabaseReportingModel;
    }
}
