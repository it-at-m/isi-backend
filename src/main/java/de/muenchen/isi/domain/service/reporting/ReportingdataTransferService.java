package de.muenchen.isi.domain.service.reporting;

import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.mapper.ReportingApiDomainMapper;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.calculation.BedarfeForAbfragevarianteModel;
import de.muenchen.isi.infrastructure.repository.reporting.AbfrageReportingRepository;
import de.muenchen.isi.reporting.client.model.AbfrageDto;
import de.muenchen.isi.reporting.client.model.BaugenehmigungsverfahrenDto;
import de.muenchen.isi.reporting.client.model.BauleitplanverfahrenDto;
import de.muenchen.isi.reporting.client.model.WeiteresVerfahrenDto;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportingdataTransferService {

    private final ReportingApiDomainMapper reportingApiDomainMapper;

    private final AbfrageReportingRepository abfrageReportingRepository;

    /**
     *
     * @param model
     * @param bedarfForEachAbfragevariante
     * @throws ReportingException
     */
    public void addBedarfeToAbfrageAndTransferData(
        final AbfrageModel model,
        final Map<UUID, BedarfeForAbfragevarianteModel> bedarfForEachAbfragevariante
    ) throws ReportingException {
        var reportingDto = reportingApiDomainMapper.model2ReportingDto(model);
        reportingDto = this.addBedarfeToAbfrage(reportingDto, bedarfForEachAbfragevariante);
        this.transferAbfrage(reportingDto);
    }

    protected void transferAbfrage(final AbfrageDto abfrage) throws ReportingException {
        try {
            abfrageReportingRepository.save(abfrage);
        } catch (final Exception exception) {
            final var error = "Beim Transfer der zu reportenden Abfrage ist ein Fehler aufgetreten.";
            log.error(error, exception);
            throw new ReportingException(error, exception);
        }
    }

    protected AbfrageDto addBedarfeToAbfrage(
        final AbfrageDto abfrage,
        final Map<UUID, BedarfeForAbfragevarianteModel> bedarfForEachAbfragevariante
    ) throws ReportingException {
        if (AbfrageDto.ArtAbfrageEnum.BAULEITPLANVERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var bauleitplanverfahren = (BauleitplanverfahrenDto) abfrage;
            Stream
                .concat(
                    bauleitplanverfahren.getAbfragevariantenBauleitplanverfahren().stream(),
                    bauleitplanverfahren.getAbfragevariantenSachbearbeitungBauleitplanverfahren().stream()
                )
                .forEach(abfragevariante -> {
                    final var bedarfModel = bedarfForEachAbfragevariante.get(abfragevariante.getId());
                    reportingApiDomainMapper.reportingDtoAndBedarfe2ReportingDto(abfragevariante, bedarfModel);
                });
        } else if (AbfrageDto.ArtAbfrageEnum.BAUGENEHMIGUNGSVERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var baugenehmigungsverfahren = (BaugenehmigungsverfahrenDto) abfrage;
            Stream
                .concat(
                    baugenehmigungsverfahren.getAbfragevariantenBaugenehmigungsverfahren().stream(),
                    baugenehmigungsverfahren.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren().stream()
                )
                .forEach(abfragevariante -> {
                    final var bedarfModel = bedarfForEachAbfragevariante.get(abfragevariante.getId());
                    reportingApiDomainMapper.reportingDtoAndBedarfe2ReportingDto(abfragevariante, bedarfModel);
                });
        } else if (AbfrageDto.ArtAbfrageEnum.WEITERES_VERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var weiteresVerfahren = (WeiteresVerfahrenDto) abfrage;
            Stream
                .concat(
                    weiteresVerfahren.getAbfragevariantenWeiteresVerfahren().stream(),
                    weiteresVerfahren.getAbfragevariantenSachbearbeitungWeiteresVerfahren().stream()
                )
                .forEach(abfragevariante -> {
                    final var bedarfModel = bedarfForEachAbfragevariante.get(abfragevariante.getId());
                    reportingApiDomainMapper.reportingDtoAndBedarfe2ReportingDto(abfragevariante, bedarfModel);
                });
        } else {
            throw new ReportingException("Für diese Art der Abfrage kann kein Reporting durchgeführt werden.");
        }
        return abfrage;
    }

    public void deleteTransferedAbfrage(final UUID id) throws ReportingException {
        try {
            abfrageReportingRepository.deleteById(id);
        } catch (final Exception exception) {
            final var error = "Beim Löschen einer im Reporting vorhandenen Abfrage ist ein Fehler aufgetreten.";
            log.error(error, exception);
            throw new ReportingException(error, exception);
        }
    }
}
