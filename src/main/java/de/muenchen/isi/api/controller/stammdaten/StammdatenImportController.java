package de.muenchen.isi.api.controller.stammdaten;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.domain.exception.CsvAttributeErrorException;
import de.muenchen.isi.domain.exception.FileImportFailedException;
import de.muenchen.isi.domain.service.stammdaten.StammdatenImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Tag(name = "Stammdaten", description = "API to import Stammdaten")
@Validated
public class StammdatenImportController {

    private final StammdatenImportService stammdatenImportService;

    @Transactional
    @PostMapping(
        path = "stammdaten/staedtebauliche-orientierungswerte/import",
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    @Operation(summary = "Importiert die CSV-Datei und persistiert die Einträge in der Datenbank.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Import konnte erfolgreich durchgeführt werden."),
            @ApiResponse(
                responseCode = "555",
                description = "CUSTOM INTERNAL SERVER ERROR -> Beim importieren der Datei ist ein serverseitiger Fehler aufgetreten.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "422",
                description = "UNPROCESSABLE_ENTITY -> Die CSV-Datei konnte nicht korrekt verarbeitet werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_STAMMDATEN_ORIENTIERUNGSWERTE.name())"
    )
    public ResponseEntity<Void> importStaedtebaulicheOrientierungswerte(
        @RequestParam("file") @NotNull final MultipartFile csvImportFile
    ) throws CsvAttributeErrorException, FileImportFailedException {
        this.stammdatenImportService.importStaedtebaulicheOrientierungswerte(csvImportFile);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping(
        path = "stammdaten/sobon-orientierungswerte-soziale-infrastruktur/import",
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    @Operation(summary = "Importiert die CSV-Datei und persistiert die Einträge in der Datenbank.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Import konnte erfolgreich durchgeführt werden."),
            @ApiResponse(
                responseCode = "555",
                description = "CUSTOM INTERNAL SERVER ERROR -> Beim importieren der Datei ist ein serverseitiger Fehler aufgetreten.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "422",
                description = "UNPROCESSABLE_ENTITY -> Die CSV-Datei konnte nicht korrekt verarbeitet werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_STAMMDATEN_ORIENTIERUNGSWERTE.name())"
    )
    public ResponseEntity<Void> importSoBoNOrientierungswerteSozialeInfrastruktur(
        @RequestParam("file") @NotNull final MultipartFile csvImportFile
    ) throws CsvAttributeErrorException, FileImportFailedException {
        this.stammdatenImportService.importSobonOrientierungswerteSozialeInfrastruktur(csvImportFile);
        return ResponseEntity.ok().build();
    }
}
