package de.muenchen.isi.infrastructure.repository.reporting;

import de.muenchen.isi.reporting.client.api.AbfrageReportingEaiApi;
import de.muenchen.isi.reporting.client.model.AbfrageDto;
import de.muenchen.isi.reporting.client.model.SaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReportingRepository {

    private final AbfrageReportingEaiApi abfrageReportingEaiApi;

    public void save(final AbfrageDto abfrage) {
        final var saveRequest = new SaveRequest();
        abfrageReportingEaiApi.save(saveRequest);
    }
}
