package de.muenchen.isi.infrastructure.repository.reporting;

import de.muenchen.isi.reporting.client.api.AbfrageReportingEaiApi;
import de.muenchen.isi.reporting.client.model.AbfrageDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReportingdataTransferRepository {

    private final AbfrageReportingEaiApi abfrageReportingEaiApi;

    public void save(final AbfrageDto abfrage) {
        abfrageReportingEaiApi.save(abfrage).block();
    }

    public void deleteById(final UUID id) {
        abfrageReportingEaiApi.delete(id).block();
    }
}
