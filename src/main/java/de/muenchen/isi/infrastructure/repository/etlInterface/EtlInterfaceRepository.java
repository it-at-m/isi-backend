package de.muenchen.isi.infrastructure.repository.etlInterface;

import de.muenchen.isi.reporting.client.api.EtlInterfaceEaiApi;
import de.muenchen.isi.reporting.client.model.EtlTriggerJobDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EtlInterfaceRepository {

    private final EtlInterfaceEaiApi etlInterfaceEaiApi;

    /**
     * Triggert die Ausführung eines Jobs im ETL-System
     *
     * @param etlTriggerJobDto {@link EtlTriggerJobDto} mit den Job-Informationen.
     */
    public void etlTriggerJob(final EtlTriggerJobDto etlTriggerJobDto) {
        etlInterfaceEaiApi.triggerJob(etlTriggerJobDto).block();
    }
}
