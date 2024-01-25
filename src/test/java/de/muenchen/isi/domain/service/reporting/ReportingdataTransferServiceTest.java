package de.muenchen.isi.domain.service.reporting;

import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.mapper.ReportingApiDomainMapper;
import de.muenchen.isi.domain.mapper.ReportingApiDomainMapperImpl;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.calculation.BedarfeForAbfragevarianteModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.repository.reporting.ReportingdataTransferRepository;
import de.muenchen.isi.reporting.client.model.AbfrageDto;
import de.muenchen.isi.reporting.client.model.BaugenehmigungsverfahrenDto;
import de.muenchen.isi.reporting.client.model.BauleitplanverfahrenDto;
import de.muenchen.isi.reporting.client.model.WeiteresVerfahrenDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReportingdataTransferServiceTest {

    private ReportingdataTransferService reportingdataTransferService;

    private ReportingApiDomainMapper reportingApiDomainMapper = new ReportingApiDomainMapperImpl();

    @Mock
    private ReportingdataTransferRepository reportingdataTransferRepository;

    @BeforeEach
    public void beforeEach() {
        this.reportingdataTransferService =
            Mockito.spy(new ReportingdataTransferService(reportingApiDomainMapper, reportingdataTransferRepository));
        Mockito.reset(reportingdataTransferRepository);
    }

    @Test
    void transferAbfrageAndBedarfeBauleitplanverfahren() throws ReportingException {
        var abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BauleitplanverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBauleitplanverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAULEITPLANVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante);
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtAddBedarfeToAbfrageBauleitplanverfahren() throws ReportingException {
        var abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BauleitplanverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBauleitplanverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAULEITPLANVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenThrow(new ReportingException("test"));
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(0)).transferAbfrage(Mockito.any());
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtTransferAbfrageBauleitplanverfahren() throws ReportingException {
        var abfrageModel = new BauleitplanverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAULEITPLANVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BauleitplanverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBauleitplanverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBauleitplanverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAULEITPLANVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        Mockito
            .doThrow(new ReportingException("test"))
            .when(reportingdataTransferService)
            .transferAbfrage(abfrageReportingDto);
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void transferAbfrageAndBedarfeBaugenehmigungsverfahren() throws ReportingException {
        var abfrageModel = new BaugenehmigungsverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BaugenehmigungsverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAUGENEHMIGUNGSVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante);
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtAddBedarfeToAbfrageBaugenehmigungsverfahren() throws ReportingException {
        var abfrageModel = new BaugenehmigungsverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BaugenehmigungsverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAUGENEHMIGUNGSVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenThrow(new ReportingException("test"));
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(0)).transferAbfrage(Mockito.any());
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtTransferAbfrageBaugenehmigungsverfahren() throws ReportingException {
        var abfrageModel = new BaugenehmigungsverfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.BAUGENEHMIGUNGSVERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new BaugenehmigungsverfahrenDto();
        abfrageReportingDto.setAbfragevariantenBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungBaugenehmigungsverfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.BAUGENEHMIGUNGSVERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        Mockito
            .doThrow(new ReportingException("test"))
            .when(reportingdataTransferService)
            .transferAbfrage(abfrageReportingDto);
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void transferAbfrageAndBedarfeWeitersVerfahren() throws ReportingException {
        var abfrageModel = new WeiteresVerfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new WeiteresVerfahrenDto();
        abfrageReportingDto.setAbfragevariantenWeiteresVerfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.WEITERES_VERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante);
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtAddBedarfeToAbfrageWeitersVerfahren() throws ReportingException {
        var abfrageModel = new WeiteresVerfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new WeiteresVerfahrenDto();
        abfrageReportingDto.setAbfragevariantenWeiteresVerfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.WEITERES_VERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenThrow(new ReportingException("test"));
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(0)).transferAbfrage(Mockito.any());
    }

    @Test
    void transferAbfrageAndBedarfeExceptionAtTransferAbfrageWeitersVerfahren() throws ReportingException {
        var abfrageModel = new WeiteresVerfahrenModel();
        abfrageModel.setArtAbfrage(ArtAbfrage.WEITERES_VERFAHREN);
        var bedarfForEachAbfragevariante = Map.of(UUID.randomUUID(), new BedarfeForAbfragevarianteModel());

        var abfrageReportingDto = new WeiteresVerfahrenDto();
        abfrageReportingDto.setAbfragevariantenWeiteresVerfahren(List.of());
        abfrageReportingDto.setAbfragevariantenSachbearbeitungWeiteresVerfahren(List.of());
        abfrageReportingDto.setArtAbfrage(AbfrageDto.ArtAbfrageEnum.WEITERES_VERFAHREN);
        Mockito
            .when(reportingdataTransferService.addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante))
            .thenReturn(abfrageReportingDto);
        Mockito
            .doThrow(new ReportingException("test"))
            .when(reportingdataTransferService)
            .transferAbfrage(abfrageReportingDto);
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.transferAbfrageAndBedarfe(abfrageModel, bedarfForEachAbfragevariante)
        );
        Mockito
            .verify(reportingdataTransferService, Mockito.times(1))
            .addBedarfeToAbfrage(abfrageReportingDto, bedarfForEachAbfragevariante);
        Mockito.verify(reportingdataTransferService, Mockito.times(1)).transferAbfrage(abfrageReportingDto);
    }

    @Test
    void transferAbfrage() throws ReportingException {
        final var abfrage = new AbfrageDto();
        reportingdataTransferService.transferAbfrage(abfrage);
        Mockito.verify(reportingdataTransferRepository, Mockito.times(1)).save(abfrage);
    }

    @Test
    void transferAbfrageException() throws ReportingException {
        final var abfrage = new AbfrageDto();
        Mockito
            .doThrow(new WebClientResponseException(500, "An error", null, null, null))
            .when(reportingdataTransferRepository)
            .save(abfrage);
        Assertions.assertThrows(ReportingException.class, () -> reportingdataTransferService.transferAbfrage(abfrage));
        Mockito.verify(reportingdataTransferRepository, Mockito.times(1)).save(abfrage);
    }

    @Test
    void deleteTransferedAbfrage() throws ReportingException {
        final var id = UUID.randomUUID();
        reportingdataTransferService.deleteTransferedAbfrage(id);
        Mockito.verify(reportingdataTransferRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void deleteTransferedAbfrageException() {
        final var id = UUID.randomUUID();
        Mockito
            .doThrow(new WebClientResponseException(500, "An error", null, null, null))
            .when(reportingdataTransferRepository)
            .deleteById(id);
        Assertions.assertThrows(
            ReportingException.class,
            () -> reportingdataTransferService.deleteTransferedAbfrage(id)
        );
        Mockito.verify(reportingdataTransferRepository, Mockito.times(1)).deleteById(id);
    }
}
