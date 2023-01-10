package de.muenchen.isi.api.controller.filehandling;

import de.muenchen.isi.api.dto.filehandling.DokumenteDto;
import de.muenchen.isi.api.mapper.DokumentApiMapper;
import de.muenchen.isi.domain.service.filehandling.DokumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Dokumente", description = "API zum Erhalt aller Dokumente")
@Validated
public class DokumentController {

    private final DokumentService dokumentService;

    private final DokumentApiMapper dokumentApiMapper;

    @Transactional(readOnly = true)
    @GetMapping("dokumente")
    @Operation(summary = "Holen aller in der Anwendung vorhandenen Dokumente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<DokumenteDto> getDokumente(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) final Integer pageNumber,
                                                     @RequestParam(value = "pageSize", defaultValue = "100", required = false) final Integer pageSize) {
        final var model = this.dokumentService.getDokumente(pageNumber, pageSize);
        final var dto = this.dokumentApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

}
