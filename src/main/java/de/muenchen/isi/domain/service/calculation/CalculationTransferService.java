package de.muenchen.isi.domain.service.calculation;

import de.muenchen.isi.domain.mapper.ReportingApiMapper;
import de.muenchen.isi.reporting.eai.client.api.AbfrageReportingEaiApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalculationTransferService {

    private final AbfrageReportingEaiApi abfrageReportingEaiApi;

    private final ReportingApiMapper reportingApiMapper;
}
