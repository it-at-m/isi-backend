package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.InfrastruktureinrichtungDto;
import de.muenchen.isi.api.mapper.InfrastruktureinrichtungApiMapper;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.service.BauvorhabenService;
import de.muenchen.isi.domain.service.InfrastruktureinrichtungService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/infrastruktureinrichtung")
@Tag(name = "Infrastruktureinrichtung", description = "API to get Infrastruktureinrichtungen")
public class InfrastruktureinrichtungController {

    private final InfrastruktureinrichtungService infrastruktureinrichtungService;

    private final BauvorhabenService bauvorhabenService;

    private final InfrastruktureinrichtungApiMapper infrastruktureinrichtungApiMapper;

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Lesen einer Infrastruktureinrichtung")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Grundschule mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_INFRASTRUKTUREINRICHTUNG.name())"
    )
    public ResponseEntity<InfrastruktureinrichtungDto> getInfrastruktureinrichtungById(
        @PathVariable @NotNull final UUID id
    ) throws EntityNotFoundException {
        final var model = this.infrastruktureinrichtungService.getInfrastruktureinrichtungById(id);
        final var dto = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Anlegen einer neuen Infrastruktureinrichtung")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "CREATED -> Infrastruktureinrichtung wurde erfolgreich erstellt."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Infrastruktureinrichtung konnte nicht erstellt werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "412",
                description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_INFRASTRUKTUREINRICHTUNG.name())"
    )
    public ResponseEntity<InfrastruktureinrichtungDto> createInfrastruktureinrichtung(
        @RequestBody @Valid @NotNull final InfrastruktureinrichtungDto infrastruktureinrichtungDto
    ) throws EntityNotFoundException, OptimisticLockingException {
        var model = this.infrastruktureinrichtungApiMapper.dto2Model(infrastruktureinrichtungDto);
        model = this.infrastruktureinrichtungService.saveInfrastruktureinrichtung(model);
        final var saved = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Aktualisierung einer Infrastruktureinrichtung")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "OK -> Infrastruktureinrichtung wurde erfolgreich aktualisiert."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Infrastruktureinrichtung konnte nicht aktualisiert werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Infrastruktureinrichtung mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "412",
                description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_INFRASTRUKTUREINRICHTUNG.name())"
    )
    public ResponseEntity<InfrastruktureinrichtungDto> updateInfrastruktureinrichtung(
        @RequestBody @Valid @NotNull final InfrastruktureinrichtungDto infrastruktureinrichtungDto
    ) throws EntityNotFoundException, OptimisticLockingException {
        var model = this.infrastruktureinrichtungApiMapper.dto2Model(infrastruktureinrichtungDto);
        model = this.infrastruktureinrichtungService.updateInfrastruktureinrichtung(model);
        final var saved = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Löschen einer Infrastruktureinrichtung")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Infrastruktureinrichtung mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Infrastruktureinrichtung referenziert ein Bauvorhaben.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_INFRASTRUKTUREINRICHTUNG.name())"
    )
    public ResponseEntity<Void> deleteInfrastruktureinrichtungById(@PathVariable @NotNull final UUID id)
        throws EntityNotFoundException, EntityIsReferencedException {
        this.infrastruktureinrichtungService.deleteInfrastruktureinrichtungById(id);
        return ResponseEntity.noContent().build();
    }
}
