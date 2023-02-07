/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.InfrastrukturabfrageDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.AbfrageApiMapper;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.service.AbfrageService;
import de.muenchen.isi.domain.service.BauvorhabenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Abfrage", description = "API to interact with the Abfragen")
@Validated
public class AbfrageController {

    private final AbfrageService abfrageService;

    private final BauvorhabenService bauvorhabenService;

    private final AbfrageApiMapper abfrageApiMapper;

    @Transactional(readOnly = true)
    @GetMapping("infrastruktur-abfragen")
    @Operation(summary = "Lade alle Infrastrukturabfragen", description = "Das Ergebnis wird nach Frist Stellungnahme absteigend sortiert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_ABFRAGE.name())")
    public ResponseEntity<List<InfrastrukturabfrageDto>> getInfrastrukturabfragen() {
        final List<InfrastrukturabfrageDto> abfrageList = this.abfrageService.getInfrastrukturabfragen()
                .stream()
                .map(this.abfrageApiMapper::model2Dto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(abfrageList, HttpStatus.OK);
    }

    @GetMapping("infrastruktur-abfrage/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Lesen einer Infrastrukturabfrage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Abfrage mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_ABFRAGE.name())")
    public ResponseEntity<InfrastrukturabfrageDto> getInfrastrukturabfrageById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException {
        final var model = this.abfrageService.getInfrastrukturabfrageById(id);
        final var dto = this.abfrageApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("infrastruktur-abfrage")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Anlegen einer neuen Infrastrukturabfrage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Abfrage wurde erfolgreich erstellt."),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST -> Abfrage konnte nicht erstellt werden, überprüfen sie die Eingabe.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Abfrage konnte nicht erstellt werden, da der Abfragename bereits existiert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "412", description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_ABFRAGE.name())")
    public ResponseEntity<InfrastrukturabfrageDto> createInfrastrukturabfrage(@RequestBody @Valid @NotNull final InfrastrukturabfrageDto abfrageDto) throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        var model = this.abfrageApiMapper.dto2Model(abfrageDto);
        final var abfrage = this.bauvorhabenService.assignBauvorhabenToAbfrage(abfrageDto.getAbfrage().getBauvorhaben(), model.getAbfrage());
        model.setAbfrage(abfrage);
        model = this.abfrageService.saveInfrastrukturabfrage(model);
        final var saved = this.abfrageApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("infrastruktur-abfrage")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Aktualisierung einer Infrastrukturabfrage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich aktualisiert."),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST -> Abfrage konnte nicht aktualisiert werden, überprüfen sie die Eingabe.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Abfrage konnte nicht erstellt werden, da der Abfragename bereits existiert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "412", description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_ABFRAGE.name())")
    public ResponseEntity<InfrastrukturabfrageDto> updateInfrastrukturabfrage(@RequestBody @Valid @NotNull final InfrastrukturabfrageDto abfrageDto) throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        var model = this.abfrageApiMapper.dto2Model(abfrageDto);
        final var abfrage = this.bauvorhabenService.assignBauvorhabenToAbfrage(abfrageDto.getAbfrage().getBauvorhaben(), model.getAbfrage());
        model.setAbfrage(abfrage);
        model = this.abfrageService.updateInfrastrukturabfrage(model);
        final var saved = this.abfrageApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("infrastruktur-abfrage/{id}")
    @Operation(summary = "Löschen einer Infrastrukturabfrage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Abfrage mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage referenziert ein Bauvorhaben.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @Transactional
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_ABFRAGE.name())")
    public ResponseEntity<Void> deleteInfrastrukturabfrageById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        this.abfrageService.deleteInfrasturkturabfrageById(id);
        return ResponseEntity.noContent().build();
    }

}
