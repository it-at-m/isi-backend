/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.BaurateApiMapper;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.service.BaurateService;
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
@Tag(name = "Baurate", description = "API zum Interagieren mit Bauraten")
@Validated
public class BaurateController {

    private final BaurateService baurateService;

    private final BaurateApiMapper baurateApiMapper;

    @GetMapping("bauraten")
    @Operation(summary = "Lesen aller Bauraten")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_BAURATE.name())")
    public ResponseEntity<List<BaurateDto>> getBauraten() {
        final List<BaurateDto> baurateDtoList = this.baurateService.getBauraten()
                .stream().map(this.baurateApiMapper::model2Dto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(baurateDtoList, HttpStatus.OK);
    }


    @GetMapping("baurate/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Lesen einer Baurate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Baurate mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_BAURATE.name())")
    public ResponseEntity<BaurateDto> getBaurateById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException {
        final var model = this.baurateService.getBaurateById(id);
        final var dto = this.baurateApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("baurate")
    @Transactional
    @Operation(summary = "Anlegen einer neuen Baurate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Baurate wurde erfolgreich erstellt."),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST -> Baurate konnte nicht erstellt werden, überprüfen sie die Eingabe.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "412", description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_BAURATE.name())")
    public ResponseEntity<BaurateDto> createBaurate(@RequestBody @Valid @NotNull final BaurateDto baurateDto) throws OptimisticLockingException {
        var model = this.baurateApiMapper.dto2Model(baurateDto);
        model = this.baurateService.saveBaurate(model);
        final var saved = this.baurateApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("baurate")
    @Transactional
    @Operation(summary = "Aktualisierung einer Baurate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Baurate wurde erfolgreich aktualisiert."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Baurate mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "412", description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_BAURATE.name())")
    public ResponseEntity<BaurateDto> updateBaurate(@RequestBody @Valid @NotNull final BaurateDto baurateDto) throws EntityNotFoundException, OptimisticLockingException {
        var model = this.baurateApiMapper.dto2Model(baurateDto);
        model = this.baurateService.updateBaurate(model);
        final var saved = this.baurateApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("baurate/{id}")
    @Operation(summary = "Löschen einer Baurate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Baurate mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @Transactional
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_BAURATE.name())")
    public ResponseEntity<Void> deleteBaurateById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException {
        this.baurateService.deleteBaurateById(id);
        return ResponseEntity.noContent().build();
    }

}
