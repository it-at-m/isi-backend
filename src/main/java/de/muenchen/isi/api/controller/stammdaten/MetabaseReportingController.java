package de.muenchen.isi.api.controller.stammdaten;

import de.muenchen.isi.api.dto.stammdaten.MetabaseReportingDto;
import de.muenchen.isi.api.mapper.StammdatenApiMapper;
import de.muenchen.isi.domain.service.stammdaten.MetabaseReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "MetabaseReportingInformation", description = "API zum Erhalten von Metabase Reporting Informationen")
public class MetabaseReportingController {

    private final StammdatenApiMapper stammdatenApiMapper;

    private final MetabaseReportingService metabaseReportingService;

    @GetMapping("stammdaten/metabase-reporting")
    @Operation(
        description = "Gibt für die Anwendung notwendigen Informationen über Metabase Reporting zurück (z.B. URL und aufrufbare Reports)"
    )
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_METABASE_REPORTING.name())"
    )
    public ResponseEntity<MetabaseReportingDto> getMetabaseReporting() {
        final var model = this.metabaseReportingService.getMetabaseReporting();
        final var dto = this.stammdatenApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }
}
