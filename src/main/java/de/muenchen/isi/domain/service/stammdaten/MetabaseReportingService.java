package de.muenchen.isi.domain.service.stammdaten;

import de.muenchen.isi.domain.model.stammdaten.MetabaseReportingModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MetabaseReportingService {

    private String url;

    private String reportBedarfe;

    private String reportSpitzenbedarfePlanungsursaechlich;

    private String reportSpitzenbedarfeSobonUrsaechlich;

    private String reportWohneinheiten;

    public MetabaseReportingService(
        @Value("${metabase.reporting.url}") final String url,
        @Value("${metabase.reporting.reports.report.bedarfe}") final String reportBedarfe,
        @Value(
            "${metabase.reporting.report.spitzenbedarfe.planungsursaechlich}"
        ) final String reportSpitzenbedarfePlanungsursaechlich,
        @Value(
            "${metabase.reporting.report.spitzenbedarfe.sobon.ursaechlich}"
        ) final String reportSpitzenbedarfeSobonUrsaechlich,
        @Value("${metabase.reporting.report.wohneinheiten}") final String reportWohneinheiten
    ) {
        this.url = url;
        this.reportBedarfe = reportBedarfe;
        this.reportSpitzenbedarfePlanungsursaechlich = reportSpitzenbedarfePlanungsursaechlich;
        this.reportSpitzenbedarfeSobonUrsaechlich = reportSpitzenbedarfeSobonUrsaechlich;
        this.reportWohneinheiten = reportWohneinheiten;
    }

    /**
     * @return Informationen über den Aufruf von Metabase und die aufrufbaren Reports
     */
    public MetabaseReportingModel getMetabaseReporting() {
        final var metabaseReportingModel = new MetabaseReportingModel();
        metabaseReportingModel.setReportBedarfe(this.reportBedarfe);
        metabaseReportingModel.setReportSpitzenbedarfePlanungsursaechlich(this.reportSpitzenbedarfePlanungsursaechlich);
        metabaseReportingModel.setReportSpitzenbedarfeSobonUrsaechlich(this.reportSpitzenbedarfeSobonUrsaechlich);
        metabaseReportingModel.setReportWohneinheiten(this.reportWohneinheiten);
        return metabaseReportingModel;
    }
}
