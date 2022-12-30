package de.muenchen.isi.api.controller.stammdaten;

import de.muenchen.isi.api.dto.stammdaten.FileInformationDto;
import de.muenchen.isi.api.mapper.StammdatenApiMapper;
import de.muenchen.isi.domain.service.stammdaten.FileInformationStammService;
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
@Tag(name = "FileInfoStamm", description = "API zum Erhalten von dateibezogenen Stammdaten.")
public class FileInformationStammController {

    private final StammdatenApiMapper stammdatenApiMapper;

    private final FileInformationStammService fileInformationStammService;

    @GetMapping("stammdaten/file-information")
    @Operation(description = "Gibt die in der Anwendung erlaubten Dateiendungen, maximalen Dateigrößen, ... zurück (z.B. .pdf).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_STAMMDATEN_FILEINFORMATION.name())")
    public ResponseEntity<FileInformationDto> getFileInformation() {
        final var model = this.fileInformationStammService.getFileInformation();
        final var dto = this.stammdatenApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

}
