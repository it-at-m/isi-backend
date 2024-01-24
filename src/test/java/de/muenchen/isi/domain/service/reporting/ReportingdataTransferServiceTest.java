package de.muenchen.isi.domain.service.reporting;

import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.mapper.ReportingApiDomainMapper;
import de.muenchen.isi.domain.mapper.ReportingApiDomainMapperImpl;
import de.muenchen.isi.infrastructure.repository.reporting.ReportingdataTransferRepository;
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
    void deleteTransferedAbfrag() {
        final var id = UUID.randomUUID();
        reportingdataTransferRepository.deleteById(id);
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
