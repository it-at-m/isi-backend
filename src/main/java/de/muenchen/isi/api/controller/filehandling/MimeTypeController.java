package de.muenchen.isi.api.controller.filehandling;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.dto.filehandling.FilepathDto;
import de.muenchen.isi.api.dto.filehandling.MimeTypeInformationDto;
import de.muenchen.isi.api.mapper.FilehandlingApiMapper;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.MimeTypeExtractionFailedException;
import de.muenchen.isi.domain.exception.MimeTypeNotAllowedException;
import de.muenchen.isi.domain.model.filehandling.MimeTypeInformationModel;
import de.muenchen.isi.domain.service.filehandling.MimeTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "MimeType", description = "API zur MimeType-Prüfung")
@Validated
public class MimeTypeController {

    private final MimeTypeService mimeTypeService;

    private final FilehandlingApiMapper filehandlingApiMapper;

    @PostMapping("mime-type")
    @Operation(
        summary = "Stellt die Mime-Type-Information für die im Parameter referenzierte und im S3-Storage befindliche Datei zur Verfügung.",
        description = "Handelt es sich um einen ungültigen Mime-Type wird die referenzierte Datei vom S3-Storage gelöscht."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "406",
                description = "NOT ACCEPTABLE -> Die referenzierte und im S3-Storage befindliche Datei besitzt keinen zulässigen Mime-Type.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "555",
                description = "CUSTOM INTERNAL SERVER ERROR -> Bei der Ermittlung des Mime-Types ist ein Fehler aufgetreten.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_PRESIGNED_URL_SAVE_FILE.name())"
    )
    public ResponseEntity<MimeTypeInformationDto> extractMediaTypeInformationForAllowedMediaType(
        @RequestBody @NotNull @Valid final FilepathDto filepathDto
    )
        throws FileHandlingWithS3FailedException, FileHandlingFailedException, MimeTypeExtractionFailedException, MimeTypeNotAllowedException {
        final MimeTypeInformationModel model =
            this.mimeTypeService.extractMediaTypeInformationForAllowedMediaType(
                    this.filehandlingApiMapper.dto2Model(filepathDto)
                );
        final MimeTypeInformationDto dto = this.filehandlingApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }
}
