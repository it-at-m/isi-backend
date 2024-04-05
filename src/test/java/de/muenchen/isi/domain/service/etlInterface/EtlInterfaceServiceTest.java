package de.muenchen.isi.domain.service.etlInterface;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.mapper.ReportingApiDomainMapper;
import de.muenchen.isi.domain.mapper.ReportingApiDomainMapperImpl;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.WeiteresVerfahrenModel;
import de.muenchen.isi.domain.model.calculation.*;
import de.muenchen.isi.domain.service.reporting.ReportingdataTransferService;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.repository.etlInterface.EtlInterfaceRepository;
import de.muenchen.isi.infrastructure.repository.reporting.ReportingdataTransferRepository;
import de.muenchen.isi.reporting.client.model.*;
import java.math.BigDecimal;
import java.util.HashMap;
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
class EtlInterfaceServiceTest {

    private EtlInterfaceService etlInterfaceService;

    @Mock
    private EtlInterfaceRepository etlInterfaceRepository;

    @BeforeEach
    public void beforeEach() {
        this.etlInterfaceService = Mockito.spy(new EtlInterfaceService(etlInterfaceRepository));
        Mockito.reset(etlInterfaceRepository);
    }

    @Test
    void triggerJob() throws ReportingException {
        var etlTriggerJobDto = new EtlTriggerJobDto();
        etlTriggerJobDto.setJobname(
            "jobs/bevoelkerungsdatenFuerKitaPlBs/Job_Bevoelkerungsdaten_fuer_KitaPlanungsbereiche.kjb"
        );
        etlInterfaceService.etlInterfaceTriggerJob(etlTriggerJobDto);
        Mockito.verify(etlInterfaceService, Mockito.times(1)).etlInterfaceTriggerJob(etlTriggerJobDto);
    }
}
