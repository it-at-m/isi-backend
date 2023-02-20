/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.controller.stammdaten;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.dto.stammdaten.FoerdermixStammDto;
import de.muenchen.isi.api.mapper.StammdatenApiMapper;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.service.stammdaten.FoerdermixStammService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@Tag(name = "FoerdermixStamm", description = "API zum Interagieren mit FoerdermixStamm")
@Validated
public class FoerdermixStammController {

    private final FoerdermixStammService foerdermixStammService;

    private final StammdatenApiMapper stammdatenApiMapper;

    @Transactional(readOnly = true)
    @GetMapping("stammdaten/foerdermix")
    @Operation(summary = "Lade alle Fördermix Stammdaten", description = "Das Ergebnis wird nach der Bezeichnung aufsteigend sortiert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_STAMMDATEN_FOERDERMIX.name())")
    public ResponseEntity<List<FoerdermixStammDto>> getFoerdermixStaemme() {
        final List<FoerdermixStammDto> foerdermixStammList = this.foerdermixStammService.getFoerdermixStaemme()
                .stream()
                .map(this.stammdatenApiMapper::model2Dto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(foerdermixStammList, HttpStatus.OK);
    }

    @GetMapping("stammdaten/foerdermix/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Lesen eines FoerdermixStamm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> FoerdermixStamm mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_STAMMDATEN_FOERDERMIX.name())")
    public ResponseEntity<FoerdermixStammDto> getFoerdermixStammById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException {
        final var model = this.foerdermixStammService.getFoerdermixStammById(id);
        final var dto = this.stammdatenApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("stammdaten/foerdermix")
    @Transactional
    @Operation(summary = "Anlegen eines FoerdermixStamm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> FoerdermixStamm wurde erfolgreich erstellt."),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST -> FoerdermixStamm konnte nicht erstellt werden, überprüfen sie die Eingabe.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Fördermix konnte nicht erstellt werden, da die Bezeichnung im angegebenen Jahr bereits existiert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "412", description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_STAMMDATEN_FOERDERMIX.name())")
    public ResponseEntity<FoerdermixStammDto> saveFoerdermixStamm(@RequestBody @Valid @NotNull final FoerdermixStammDto foerdermixStammDto) throws UniqueViolationException, OptimisticLockingException {
        var model = this.stammdatenApiMapper.dto2Model(foerdermixStammDto);
        model = this.foerdermixStammService.saveFoerdermixStamm(model);
        final var saved = this.stammdatenApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("stammdaten/foerdermix")
    @Transactional
    @Operation(summary = "Aktualisierung eines FoerdermixStamm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> FoerdermixStamm wurde erfolgreich aktualisiert."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine FoerdermixStamm mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Fördermix konnte nicht erstellt werden, da die Bezeichnung im angegebenen Jahr bereits existiert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "412", description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_STAMMDATEN_FOERDERMIX.name())")
    public ResponseEntity<FoerdermixStammDto> updateFoerdermixStamm(@RequestBody @Valid @NotNull final FoerdermixStammDto foerdermixStammDto) throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException {
        var model = this.stammdatenApiMapper.dto2Model(foerdermixStammDto);
        model = this.foerdermixStammService.updateFoerdermixStamm(model);
        final var saved = this.stammdatenApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("stammdaten/foerdermix/{id}")
    @Operation(summary = "Löschen eines FoerdermixStamm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> FoerdermixStamm mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_STAMMDATEN_FOERDERMIX.name())")
    @Transactional
    public ResponseEntity<Void> deleteFoerdermixStammById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException {
        this.foerdermixStammService.deleteFoerdermixStammById(id);
        return ResponseEntity.noContent().build();
    }

}
