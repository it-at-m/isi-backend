/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.AbfrageDto;
import de.muenchen.isi.api.dto.abfrageAngelegt.AbfrageAngelegtDto;
import de.muenchen.isi.api.dto.abfrageBedarfsmeldungErfolgt.AbfrageBedarfsmeldungErfolgtDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat.AbfrageInBearbeitungFachreferatDto;
import de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung.AbfrageInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.AbfrageApiMapper;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.ReportingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.AbfrageDomainMapper;
import de.muenchen.isi.domain.service.AbfrageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/abfrage")
@Tag(name = "Abfragen", description = "API zum interagieren mit Abfragen")
@Validated
public class AbfrageController {

    private final AbfrageService abfrageService;

    private final AbfrageApiMapper abfrageApiMapper;

    private final AbfrageDomainMapper abfrageDomainMapper;

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Lesen einer Abfrage.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Keine Berechtigung um die Abfrage zu öffnen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Abfrage mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_ABFRAGE.name())")
    public ResponseEntity<AbfrageDto> getById(@PathVariable @NotNull final UUID id)
        throws EntityNotFoundException, UserRoleNotAllowedException {
        final var model = abfrageService.getById(id);
        final var dto = abfrageApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional(
        rollbackFor = {
            OptimisticLockingException.class,
            UniqueViolationException.class,
            CalculationException.class,
            ReportingException.class,
        }
    )
    @Operation(summary = "Anlegen einer neuen Abfrage")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Abfrage wurde erfolgreich erstellt."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Abfrage konnte nicht erstellt werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Das referenzierte Bauvorhaben existiert nicht.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Abfrage konnte nicht erstellt werden, da der Name der Abfrage oder Abfragevariante bereits existiert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "412",
                description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_POST_ABFRAGE.name())")
    public ResponseEntity<AbfrageDto> save(@RequestBody @Valid @NotNull AbfrageAngelegtDto abfrage)
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, CalculationException, ReportingException {
        final var requestModel = abfrageApiMapper.dto2Model(abfrage);
        var model = abfrageDomainMapper.request2NewModel(requestModel);
        model = abfrageService.save(model);
        final var dto = abfrageApiMapper.model2Dto(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PatchMapping("/angelegt/{id}")
    @Transactional(
        rollbackFor = {
            OptimisticLockingException.class,
            UniqueViolationException.class,
            CalculationException.class,
            ReportingException.class,
        }
    )
    @Operation(summary = "Aktualisierung einer Abfrage im Status ANGELEGT.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich aktualisiert."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Abfrage konnte nicht aktualisiert werden, überprüfen sie die Eingabe oder die Abfrage befindet sich in einem unzulässigen Status",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Keine Berechtigung um die Abfrage zu bearbeiten.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID oder das referenzierte Bauvorhaben existiert nicht.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Abfrage konnte nicht aktualisiert werden, da der Name der Abfrage oder Abfragevariante bereits existiert oder die Abfrage nicht im korrekten Status ist.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "412",
                description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "555",
                description = "CUSTOM INTERNAL SERVER ERROR -> Die Dateien konnten nicht gelöscht werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_PATCH_ABFRAGE_ANGELEGT.name())")
    public ResponseEntity<AbfrageDto> patchAngelegt(
        @RequestBody @Valid @NotNull final AbfrageAngelegtDto abfrage,
        @PathVariable @NotNull final UUID id
    )
        throws UniqueViolationException, FileHandlingFailedException, FileHandlingWithS3FailedException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException, CalculationException, ReportingException {
        final var requestModel = abfrageApiMapper.dto2Model(abfrage);
        final var responseModel = abfrageService.patchAngelegt(requestModel, id);
        final var dto = abfrageApiMapper.model2Dto(responseModel);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/in-bearbeitung-sachbearbeitung/{id}")
    @Transactional(
        rollbackFor = {
            OptimisticLockingException.class,
            UniqueViolationException.class,
            CalculationException.class,
            ReportingException.class,
        }
    )
    @Operation(summary = "Aktualisierung einer Abfrage im Status IN_BEARBEITUNG_SACHBEARBEITUNG.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich aktualisiert."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Abfrage konnte nicht aktualisiert werden, überprüfen sie die Eingabe oder die Abfrage befindet sich in einem unzulässigen Status",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Keine Berechtigung um die Abfrage zu bearbeiten.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Abfrage konnte nicht aktualisiert werden, da der Name der Abfrage oder Abfragevariante bereits existiert oder die Abfrage nicht im korrekten Status ist.",
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
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_PATCH_ABFRAGE_IN_BEARBEITUNG_SACHBEARBEITUNG.name())"
    )
    public ResponseEntity<AbfrageDto> patchInBearbeitungSachbearbeitung(
        @RequestBody @Valid @NotNull final AbfrageInBearbeitungSachbearbeitungDto abfrage,
        @PathVariable @NotNull final UUID id
    )
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, UserRoleNotAllowedException, ReportingException {
        final var requestModel = abfrageApiMapper.dto2Model(abfrage);
        final var responseModel = abfrageService.patchInBearbeitungSachbearbeitung(requestModel, id);
        final var dto = abfrageApiMapper.model2Dto(responseModel);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/in-bearbeitung-fachreferate/{id}")
    @Transactional(
        rollbackFor = {
            OptimisticLockingException.class,
            UniqueViolationException.class,
            CalculationException.class,
            ReportingException.class,
        }
    )
    @Operation(summary = "Aktualisierung einer Abfrage im Status IN_BEARBEITUNG_FACHREFERATE.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich aktualisiert."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Abfrage konnte nicht aktualisiert werden, überprüfen sie die Eingabe oder die Abfrage befindet sich in einem unzulässigen Status",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Keine Berechtigung um die Abfrage zu bearbeiten.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Abfrage konnte nicht aktualisiert werden, da der Name der Abfrage oder Abfragevariante bereits existiert oder die Abfrage nicht im korrekten Status ist.",
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
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_PATCH_ABFRAGE_IN_BEARBEITUNG_FACHREFERATE.name())"
    )
    public ResponseEntity<AbfrageDto> patchInBearbeitungFachreferat(
        @RequestBody @Valid @NotNull final AbfrageInBearbeitungFachreferatDto abfrage,
        @PathVariable @NotNull final UUID id
    )
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, CalculationException, UserRoleNotAllowedException, ReportingException {
        final var requestModel = abfrageApiMapper.dto2Model(abfrage);
        final var responseModel = abfrageService.patchInBearbeitungFachreferat(requestModel, id);
        final var dto = abfrageApiMapper.model2Dto(responseModel);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/bedarfsmeldung-erfolgt/{id}")
    @Transactional(
        rollbackFor = {
            OptimisticLockingException.class,
            UniqueViolationException.class,
            CalculationException.class,
            ReportingException.class,
        }
    )
    @Operation(summary = "Aktualisierung einer Abfrage im Status BEDARFSMELDUNG_ERFOLGT.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich aktualisiert."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Abfrage konnte nicht aktualisiert werden, überprüfen sie die Eingabe oder die Abfrage befindet sich in einem unzulässigen Status",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Abfrage konnte nicht aktualisiert werden, da der Name der Abfrage oder Abfragevariante bereits existiert oder die Abfrage nicht im korrekten Status ist.",
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
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_PATCH_ABFRAGE_BEDARFSMELDUNG_ERFOLGT.name())"
    )
    public ResponseEntity<AbfrageDto> patchBedarfsmeldungErfolgt(
        @RequestBody @Valid @NotNull final AbfrageBedarfsmeldungErfolgtDto abfrage,
        @PathVariable @NotNull final UUID id
    )
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException, UserRoleNotAllowedException, CalculationException, ReportingException {
        final var requestModel = abfrageApiMapper.dto2Model(abfrage);
        final var responseModel = abfrageService.patchBedarfsmeldungErfolgt(requestModel, id);
        final var dto = abfrageApiMapper.model2Dto(responseModel);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Löschen einer Abfrage")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Abfrage mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Abfrage referenziert ein Bauvorhaben oder die Abfrage nicht im korrekten Status ist.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "CONFLICT -> Der Nutzer besitzt nicht die Berechtigung zum Löschen der Abfrage.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @Transactional(rollbackFor = { ReportingException.class })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_ABFRAGE.name())")
    public ResponseEntity<Void> deleteById(@PathVariable @NotNull final UUID id)
        throws UserRoleNotAllowedException, EntityIsReferencedException, EntityNotFoundException, AbfrageStatusNotAllowedException, ReportingException {
        this.abfrageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
