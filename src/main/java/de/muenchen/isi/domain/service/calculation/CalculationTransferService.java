package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.mapper.ReportingApiDomainMapper;
import de.muenchen.isi.domain.model.calculation.LangfristigerPlanungsursaechlicherBedarfModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.repository.reporting.ReportingRepository;
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
public class CalculationTransferService {

    private final ReportingApiDomainMapper reportingApiDomainMapper;

    private final ReportingRepository reportingRepository;

    protected AbfrageDto addCalculationResultsToAbfrage(
        final AbfrageDto abfrage,
        final Map<UUID, LangfristigerPlanungsursaechlicherBedarfModel> bedarfForEachAbfragevariante
    ) throws CalculationException {
        if (ArtAbfrage.BAULEITPLANVERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var bauleitplanverfahren = (BauleitplanverfahrenDto) abfrage;
            Stream
                .concat(
                    bauleitplanverfahren.getAbfragevariantenBauleitplanverfahren().stream(),
                    bauleitplanverfahren.getAbfragevariantenSachbearbeitungBauleitplanverfahren().stream()
                )
                .forEach(abfragevariante -> {
                    final var bedarfModel = bedarfForEachAbfragevariante.get(abfragevariante.getId());
                    final var bedarfDto = reportingApiDomainMapper.model2ReportingDto(bedarfModel);
                    abfragevariante.setLangfristigerPlanungsursaechlicherBedarf(bedarfDto);
                });
        } else if (ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var baugenehmigungsverfahren = (BaugenehmigungsverfahrenDto) abfrage;
            Stream
                .concat(
                    baugenehmigungsverfahren.getAbfragevariantenBaugenehmigungsverfahren().stream(),
                    baugenehmigungsverfahren.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren().stream()
                )
                .forEach(abfragevariante -> {
                    final var bedarfModel = bedarfForEachAbfragevariante.get(abfragevariante.getId());
                    final var bedarfDto = reportingApiDomainMapper.model2ReportingDto(bedarfModel);
                    abfragevariante.setLangfristigerPlanungsursaechlicherBedarf(bedarfDto);
                });
        } else if (ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN.equals(abfrage.getArtAbfrage())) {
            final var weiteresVerfahren = (WeiteresVerfahrenDto) abfrage;
            Stream
                .concat(
                    weiteresVerfahren.getAbfragevariantenWeiteresVerfahren().stream(),
                    weiteresVerfahren.getAbfragevariantenSachbearbeitungWeiteresVerfahren().stream()
                )
                .forEach(abfragevariante -> {
                    final var bedarfModel = bedarfForEachAbfragevariante.get(abfragevariante.getId());
                    final var bedarfDto = reportingApiDomainMapper.model2ReportingDto(bedarfModel);
                    abfragevariante.setLangfristigerPlanungsursaechlicherBedarf(bedarfDto);
                });
        } else {
            throw new CalculationException("Die Berechnung kann für diese Art von Abfrage nicht durchgeführt werden.");
        }
        return abfrage;
    }
}
