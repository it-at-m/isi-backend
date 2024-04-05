package de.muenchen.isi.domain.service.etlInterface;

import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.infrastructure.repository.etlInterface.EtlInterfaceRepository;
import de.muenchen.isi.reporting.client.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EtlInterfaceService {

    private final EtlInterfaceRepository etlInterfaceRepository;

    /**
     * Ruft die Reporting-EAI Schnittstelle auf, damit diese einen Job im ETL-System (Pentaho) triggert
     *
     * @param etlTriggerJobDto die Job-Informationen
     * @throws ReportingException falls der Aufruf fehlgeschlagen ist.
     */
    public void etlInterfaceTriggerJob(final EtlTriggerJobDto etlTriggerJobDto) throws ReportingException {
        if (etlTriggerJobDto != null && !etlTriggerJobDto.getJobname().isEmpty()) {
            try {
                etlInterfaceRepository.etlTriggerJob(etlTriggerJobDto);
            } catch (final Exception exception) {
                final var error = "Beim Aufruf des ETL-Systems (Pentaho) ist ein Fehler aufgetreten.";
                log.error(error, exception);
                throw new ReportingException(error, exception);
            }
        }
    }
}
