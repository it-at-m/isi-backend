/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.InfrastrukturabfrageDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.InfrastrukturabfrageAngelegtDto;
import de.muenchen.isi.api.dto.abfrageBedarfsmeldungInBearbeitungFachreferate.InfrastrukturabfrageInBearbeitungFachreferateDto;
import de.muenchen.isi.api.dto.abfrageSachbearbeitungInBearbeitungSachbearbeitung.InfrastrukturabfrageInBearbeitungSachbearbeitungDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.AbfrageApiMapper;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.mapper.AbfrageAltDomainMapper;
import de.muenchen.isi.domain.model.InfrastrukturabfrageModel;
import de.muenchen.isi.domain.service.AbfrageAltService;
import de.muenchen.isi.domain.service.BauvorhabenService;
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
@RequestMapping("/infrastruktur-abfragen")
@Tag(name = "Abfrage", description = "API to interact with the Abfragen")
@Validated
public class AbfrageAltController {

    private final AbfrageAltService abfrageService;

    private final BauvorhabenService bauvorhabenService;

    private final AbfrageApiMapper abfrageApiMapper;

    private final AbfrageAltDomainMapper abfrageDomainMapper;

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Lesen einer Infrastrukturabfrage")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Abfrage mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_ABFRAGE.name())")
    public ResponseEntity<InfrastrukturabfrageDto> getInfrastrukturabfrageById(@PathVariable @NotNull final UUID id)
        throws EntityNotFoundException {
        final var model = this.abfrageService.getInfrastrukturabfrageById(id);
        final var dto = this.abfrageApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional(rollbackFor = { OptimisticLockingException.class, UniqueViolationException.class })
    @Operation(summary = "Anlegen einer neuen Infrastrukturabfrage")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Abfrage wurde erfolgreich erstellt."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Abfrage konnte nicht erstellt werden, überprüfen sie die Eingabe.",
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
    public ResponseEntity<InfrastrukturabfrageDto> createInfrastrukturabfrage(
        @RequestBody @Valid @NotNull final InfrastrukturabfrageAngelegtDto abfrageDto
    ) throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        var requestModel = this.abfrageApiMapper.dto2Model(abfrageDto);
        final var abfrage =
            this.bauvorhabenService.assignBauvorhabenToAbfrage(
                    abfrageDto.getAbfrage().getBauvorhaben(),
                    requestModel.getAbfrage()
                );
        requestModel.setAbfrage(abfrage);
        InfrastrukturabfrageModel model = new InfrastrukturabfrageModel();
        model = this.abfrageDomainMapper.request2Model(requestModel, model);
        model = this.abfrageService.saveInfrastrukturabfrage(model);
        final var saved = this.abfrageApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PatchMapping("/abfrage-angelegt/{id}")
    @Transactional(rollbackFor = { OptimisticLockingException.class, UniqueViolationException.class })
    @Operation(summary = "Aktualisierung einer Infrastrukturabfrage im Status ANGELEGT.")
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
                description = "CONFLICT -> Abfrage konnte nicht erstellt werden, da der Name der Abfrage oder Abfragevariante bereits existiert.",
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
    public ResponseEntity<InfrastrukturabfrageDto> patchAbfrageAngelegt(
        @RequestBody @Valid @NotNull final InfrastrukturabfrageAngelegtDto abfrageDto,
        @PathVariable @NotNull final UUID id
    )
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, AbfrageStatusNotAllowedException, FileHandlingFailedException, FileHandlingWithS3FailedException {
        var model = this.abfrageApiMapper.dto2Model(abfrageDto);
        final var abfrage =
            this.bauvorhabenService.assignBauvorhabenToAbfrage(
                    abfrageDto.getAbfrage().getBauvorhaben(),
                    model.getAbfrage()
                );
        model.setAbfrage(abfrage);
        final var responseModel = this.abfrageService.patchAbfrageAngelegt(model, id);
        final var saved = this.abfrageApiMapper.model2Dto(responseModel);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/abfrage-in-bearbeitung-sachbearbeitung/{id}")
    @Transactional(rollbackFor = { OptimisticLockingException.class, UniqueViolationException.class })
    @Operation(summary = "Aktualisierung einer Infrastrukturabfrage im Status IN_BEARBEITUNG_SACHBEARBEITUNG.")
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
                responseCode = "412",
                description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_PATCH_ABFRAGE_IN_BEARBEITUNG_SACHBEARBEITUNG.name())"
    )
    public ResponseEntity<InfrastrukturabfrageDto> patchAbfrageInBearbeitungSachbearbeitung(
        @RequestBody @Valid @NotNull final InfrastrukturabfrageInBearbeitungSachbearbeitungDto abfrageDto,
        @PathVariable @NotNull final UUID id
    )
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException {
        var model = this.abfrageApiMapper.dto2Model(abfrageDto);
        final var responseModel = this.abfrageService.patchAbfrageInBearbeitungSachbearbeitung(model, id);
        final var saved = this.abfrageApiMapper.model2Dto(responseModel);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/abfrage-in-bearbeitung-fachreferate/{id}")
    @Transactional(rollbackFor = { OptimisticLockingException.class, UniqueViolationException.class })
    @Operation(summary = "Aktualisierung einer Infrastrukturabfrage im Status IN_BEARBEITUNG_FACHREFERATE.")
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
                responseCode = "412",
                description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_PATCH_ABFRAGE_IN_BEARBEITUNG_FACHREFERATE.name())"
    )
    public ResponseEntity<InfrastrukturabfrageDto> patchAbfrageInBearbeitungFachreferate(
        @RequestBody @Valid @NotNull final InfrastrukturabfrageInBearbeitungFachreferateDto abfrageDto,
        @PathVariable @NotNull final UUID id
    )
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, AbfrageStatusNotAllowedException {
        var model = this.abfrageApiMapper.dto2Model(abfrageDto);
        final var responseModel = this.abfrageService.patchAbfrageInBearbeitungFachreferate(model, id);
        final var saved = this.abfrageApiMapper.model2Dto(responseModel);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Löschen einer Infrastrukturabfrage")
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
                description = "CONFLICT -> Die Abfrage referenziert ein Bauvorhaben.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @Transactional
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_ABFRAGE.name())")
    public ResponseEntity<Void> deleteInfrastrukturabfrageById(@PathVariable @NotNull final UUID id)
        throws EntityNotFoundException, EntityIsReferencedException, UserRoleNotAllowedException, AbfrageStatusNotAllowedException {
        this.abfrageService.deleteInfrasturkturabfrageById(id);
        return ResponseEntity.noContent().build();
    }
}