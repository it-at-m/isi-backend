package de.muenchen.isi.api.controller.filehandling;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.dto.filehandling.FilepathDto;
import de.muenchen.isi.api.dto.filehandling.PresignedUrlDto;
import de.muenchen.isi.api.mapper.PresignedUrlApiMapper;
import de.muenchen.isi.api.validation.HasAllowedFileExtension;
import de.muenchen.isi.api.validation.IsFilepathWithoutLeadingPathdivider;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.model.filehandling.FilepathModel;
import de.muenchen.isi.domain.service.filehandling.PresignedUrlCreationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
@Tag(name = "Dateihandling", description = "API um Presigned-Urls bezüglich des Dateihandlings mittels des S3-Storage zu holen.")
@Validated
public class PresignedUrlCreationController {

    private final PresignedUrlCreationService presignedUrlCreationService;

    private final PresignedUrlApiMapper presignedUrlApiMapper;

    @GetMapping("presigned-url")
    @Operation(
            summary = "Stellt die Presigned-Url zum Holen einer Datei zur Verfügung.",
            description = "Die Presigned-Url ist vom Aufrufer mit der Http-Methode GET zu verwenden."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST -> Der JSON-Body des Requests ist fehlerhaft oder nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Die Datei ist im S3-Storage nicht verfügbar.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "555", description = "CUSTOM INTERNAL SERVER ERROR -> Die Presigned-Url konnte nicht erzeugt werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_PRESIGNED_URL_GET_FILE.name())")
    public ResponseEntity<PresignedUrlDto> getFile(@RequestParam
                                                   @Schema(description = "Der Dateipfad muss absolut, ohne Angabe des Buckets und ohne führenden Pfadtrenner angegeben werden. Beispiel: outerFolder/innerFolder/thefile.pdf")
                                                   @NotBlank
                                                   @IsFilepathWithoutLeadingPathdivider
                                                   @HasAllowedFileExtension final String pathToFile) throws FileHandlingWithS3FailedException, FileHandlingFailedException {
        final var filepathModel = new FilepathModel();
        filepathModel.setPathToFile(pathToFile);
        final var presignedUrlModel = this.presignedUrlCreationService.getFile(filepathModel);
        final var presignedUrlDto = this.presignedUrlApiMapper.model2Dto(presignedUrlModel);
        return ResponseEntity.ok(presignedUrlDto);
    }

    @PostMapping("presigned-url")
    @Operation(
            summary = "Stellt die Presigned-Url zum Initialen Speichern einer Datei zur Verfügung.",
            description = "Die Presigned-Url ist vom Aufrufer mit der Http-Methode PUT zu verwenden."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST -> Der Request ist fehlerhaft.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Datei existiert bereits im S3-Storage unter dem Dateipfad.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "555", description = "CUSTOM INTERNAL SERVER ERROR -> Die Presigned-Url konnte nicht erzeugt werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_PRESIGNED_URL_SAVE_FILE.name())")
    public ResponseEntity<PresignedUrlDto> saveFile(@RequestBody @NotNull @Valid final FilepathDto filepathDto) throws FileHandlingWithS3FailedException, FileHandlingFailedException {
        final var presignedUrlModel = this.presignedUrlCreationService.saveFile(this.presignedUrlApiMapper.dto2Model(filepathDto));
        final var presignedUrlDto = this.presignedUrlApiMapper.model2Dto(presignedUrlModel);
        return ResponseEntity.ok(presignedUrlDto);
    }

    @DeleteMapping("presigned-url")
    @Operation(
            summary = "Stellt die Presigned-Url zum Löschen einer Datei zur Verfügung.",
            description = "Die Presigned-Url ist vom Aufrufer mit der Http-Methode DELETE zu verwenden."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST -> Der Request ist fehlerhaft.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Die Datei ist im S3-Storage nicht verfügbar.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "555", description = "CUSTOM INTERNAL SERVER ERROR -> Die Presigned-Url konnte nicht erzeugt werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_PRESIGNED_URL_DELETE_FILE.name())")
    public ResponseEntity<PresignedUrlDto> deleteFile(@RequestBody @NotNull @Valid final FilepathDto filepathDto) throws FileHandlingWithS3FailedException, FileHandlingFailedException {
        final var presignedUrlModel = this.presignedUrlCreationService.deleteFile(this.presignedUrlApiMapper.dto2Model(filepathDto));
        final var presignedUrlDto = this.presignedUrlApiMapper.model2Dto(presignedUrlModel);
        return ResponseEntity.ok(presignedUrlDto);
    }

}
