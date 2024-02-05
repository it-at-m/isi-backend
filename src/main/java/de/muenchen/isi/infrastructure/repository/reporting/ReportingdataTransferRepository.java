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

    /**
     * Speichert die Abfrage mit den reportingrelevanten Informationen über die Reportingschnittstelle.
     *
     * @param abfrage {@link AbfrageDto} mit den reportingrelevanten Informationen.
     */
    public void save(final AbfrageDto abfrage) {
        abfrageReportingEaiApi.save(abfrage).block();
    }

    /**
     * Löscht die Abfrage mit den reportingrelevanten über die Reportingschnittstelle.
     *
     * @param id der zu löschenden Abfrage.
     */
    public void deleteByIdAndArtAbfrage(final UUID id, final AbfrageDto.ArtAbfrageEnum artAbfrage) {
        abfrageReportingEaiApi.delete(id, artAbfrage.name()).block();
    }
}
