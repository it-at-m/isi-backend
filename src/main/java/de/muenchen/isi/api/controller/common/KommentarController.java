package de.muenchen.isi.api.controller.common;

import de.muenchen.isi.api.dto.common.KommentarDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.KommentarApiMapper;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.service.common.KommentarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/kommentar")
@Tag(name = "Kommentare", description = "API um Kommentare zu verwalten")
@Validated
public class KommentarController {

    private final KommentarService kommentarService;

    private final KommentarApiMapper kommentarApiMapper;

    @GetMapping("/all/bauvorhaben/{bauvorhabenId}")
    @Transactional(readOnly = true)
    @Operation(summary = "Holen der Kommentare eines Bauvorhabens")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_KOMMENTAR_BAUVORHABEN.name())"
    )
    public ResponseEntity<List<KommentarDto>> getKommentareForBauvorhaben(
        @PathVariable @NotNull final UUID bauvorhabenId
    ) {
        final var models = kommentarService.getKommentareForBauvorhaben(bauvorhabenId);
        final var dtos = models.stream().map(kommentarApiMapper::model2Dto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/all/infrastruktureinrichtung/{infrastruktureinrichtungId}")
    @Transactional(readOnly = true)
    @Operation(summary = "Holen der Kommentare einer Infrastruktureinrichtung")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_KOMMENTAR_INFRASTRUKTUREINRICHTUNG.name())"
    )
    public ResponseEntity<List<KommentarDto>> getKommentareForInfrastruktureinrichtung(
        @PathVariable @NotNull final UUID infrastruktureinrichtungId
    ) {
        final var models = kommentarService.getKommentareForInfrastruktureinrichtung(infrastruktureinrichtungId);
        final var dtos = models.stream().map(kommentarApiMapper::model2Dto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Anlegen eines neuen Kommentars")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Kommentar wurde erfolgreich erstellt."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Kommentar konnte nicht erstellt werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "412",
                description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_KOMMENTAR.name())")
    public ResponseEntity<KommentarDto> createKommentar(@RequestBody @Valid @NotNull final KommentarDto kommentarDto)
        throws EntityNotFoundException, OptimisticLockingException {
        var model = this.kommentarApiMapper.dto2Model(kommentarDto);
        model = this.kommentarService.saveKommentar(model);
        final var saved = this.kommentarApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Aktualisierung eines Kommentars")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Kommentar wurde erfolgreich aktualisiert."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Kommentar konnte nicht erstellt werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Kommentar mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "412",
                description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_KOMMENTAR.name())")
    public ResponseEntity<KommentarDto> updateKommentar(@RequestBody @Valid @NotNull final KommentarDto kommentarDto)
        throws EntityNotFoundException, OptimisticLockingException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        var model = this.kommentarApiMapper.dto2Model(kommentarDto);
        model = this.kommentarService.updateKommentar(model);
        final var saved = this.kommentarApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Löschen eines Kommentars")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "NO CONTENT") })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_KOMMENTAR.name())")
    public ResponseEntity<Void> deleteKommentar(@PathVariable @NotNull final UUID id) {
        this.kommentarService.deleteKommentarById(id);
        return ResponseEntity.noContent().build();
    }
}
