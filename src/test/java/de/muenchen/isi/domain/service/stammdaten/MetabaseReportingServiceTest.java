package de.muenchen.isi.domain.service.stammdaten;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.model.stammdaten.MetabaseReportingModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MetabaseReportingServiceTest {

    private MetabaseReportingService metabaseReportingService;

    @BeforeEach
    public void beforeEach() {
        this.metabaseReportingService = new MetabaseReportingService(
            "https://isi-metabase-test.muenchen.de",
            "collection/99-global-reports",
            " dashboard/36-ergebnisse-der-planungsursachlichen-bedarfsberechnung",
            "dashboard/44-ergebnisse-der-sobon-bedarfsberechnung",
            "dashboard/33-dashboard-wohneinheiten"
        );
    }

    @Test
    void getMetabaseReporting() {
        final var expected = new MetabaseReportingModel();
        expected.setUrl("https://isi-metabase-test.muenchen.de");
        expected.setReportsGlobal("collection/99-global-reports");
        expected.setReportErgebnissePlanungsursaechlich(
            " dashboard/36-ergebnisse-der-planungsursachlichen-bedarfsberechnung"
        );
        expected.setReportErgebnisseSobonUrsaechlich("dashboard/44-ergebnisse-der-sobon-bedarfsberechnung");
        expected.setReportWohneinheiten("dashboard/33-dashboard-wohneinheiten");
        assertThat(this.metabaseReportingService.getMetabaseReporting(), is(expected));
    }
}
