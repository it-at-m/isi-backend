package de.muenchen.isi.domain.service.etlInterface;

import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.infrastructure.repository.etlInterface.EtlInterfaceRepository;
import de.muenchen.isi.reporting.client.model.EtlTriggerJobDto;
import de.muenchen.isi.reporting.client.model.PairStringString;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EtlInterfaceService {

    private final EtlInterfaceRepository etlInterfaceRepository;

    /**
     * Ruft die Reporting-EAI Schnittstelle auf. Dieser Job löscht vor der Transponierung der Tabelle
     * wohneinheiten_pro_foerderart_pro_jahr_transponiert alle Einträge zu einer Abfrage.
     *
     * @param id ID der Abfrage
     * @throws ReportingException falls der Aufruf fehlgeschlagen ist.
     */
    public void etlInterfaceTriggerDeleteAbfrageTransponierungItemsJob(final UUID id) throws ReportingException {
        final EtlTriggerJobDto etlTriggerJobDto = this.prepareJob(
            "transponierung/wohneinheiten_pro_foerderart_pro_jahr/JobDeleteAll_wohneinheiten_pro_foerderart_pro_jahr_transponiert_OfAbfrage.kjb",
            id
        );
        this.etlInterfaceTriggerJob(etlTriggerJobDto);
    }

    /**
     * Ruft die Reporting-EAI Schnittstelle auf. Dieser Job führt eine Transponierung der Tabelle
     * wohneinheiten_pro_foerderart_pro_jahr zu wohneinheiten_pro_foerderart_pro_jahr_transponiert für eine Abfrage durch
     *
     * @param id ID der Abfrage
     * @throws ReportingException falls der Aufruf fehlgeschlagen ist.
     */
    public void etlInterfaceTriggerAbfrageTransponierungJob(final UUID id) throws ReportingException {
        final EtlTriggerJobDto etlTriggerJobDto = this.prepareJob(
            "transponierung/wohneinheiten_pro_foerderart_pro_jahr/Job_Transponierung_Abfrage.kjb",
            id
        );
        this.etlInterfaceTriggerJob(etlTriggerJobDto);
    }

    /**
     * Ruft die Reporting-EAI Schnittstelle auf, damit ein ETL-Job zur Übertragung einer Infrastruktureinrichtung
     * vom Backend zur Reporting DB aufgerufen wird
     *
     * @param id ID derInfrastruktureinrichtung
     * @throws ReportingException falls der Aufruf fehlgeschlagen ist.
     */
    public void etlInterfaceTriggerInfrastruktureinrichtungJob(final UUID id) throws ReportingException {
        final EtlTriggerJobDto etlTriggerJobDto = this.prepareJob(
            "importFromBackend/infrastruktureinrichtung/Job_Import_Infrastruktureinrichtung.kjb",
            id
        );
        this.etlInterfaceTriggerJob(etlTriggerJobDto);
    }

    /**
     * Ruft die Reporting-EAI Schnittstelle auf, damit ein ETL-Job zur Übertragung eine Bauvorhabens
     * vom Backend zur Reporting DB aufgerufen wird
     *
     * @param id ID derInfrastruktureinrichtung
     * @throws ReportingException falls der Aufruf fehlgeschlagen ist.
     */
    public void etlInterfaceTriggerBauvorhabenJob(final UUID id) throws ReportingException {
        final EtlTriggerJobDto etlTriggerJobDto = this.prepareJob(
            "importFromBackend/bauvorhaben/Job_Import_Bauvorhaben.kjb",
            id
        );
        this.etlInterfaceTriggerJob(etlTriggerJobDto);
    }

    private EtlTriggerJobDto prepareJob(final String jobname, final UUID id) {
        final EtlTriggerJobDto etlTriggerJobDto = new EtlTriggerJobDto();
        etlTriggerJobDto.setJobname(jobname);
        final var listParameter = new ArrayList<PairStringString>();
        final var idParameter = new PairStringString();
        idParameter.setFirst("id");
        idParameter.setSecond(id.toString());
        listParameter.add(idParameter);
        etlTriggerJobDto.setParameters(listParameter);
        return etlTriggerJobDto;
    }

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
                final var error =
                    "Beim Aufruf des ETL-Systems (Pentaho), Job: " +
                    etlTriggerJobDto.getJobname() +
                    ", ist ein Fehler aufgetreten";
                log.error(error, exception);
                throw new ReportingException(error, exception);
            }
        }
    }
}
