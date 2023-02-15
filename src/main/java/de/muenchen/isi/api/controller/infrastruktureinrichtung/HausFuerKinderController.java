/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.controller.infrastruktureinrichtung;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.HausFuerKinderDto;
import de.muenchen.isi.api.mapper.InfrastruktureinrichtungApiMapper;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.service.BauvorhabenService;
import de.muenchen.isi.domain.service.infrastruktureinrichtung.HausFuerKinderService;
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
@Tag(name = "HausFuerKinder", description = "API to interact with the HausFuerKinder")
@Validated
public class HausFuerKinderController {

    private final HausFuerKinderService hausFuerKinderService;

    private final BauvorhabenService bauvorhabenService;

    private final InfrastruktureinrichtungApiMapper infrastruktureinrichtungApiMapper;

    @Transactional(readOnly = true)
    @GetMapping("haeuser-fuer-kinder")
    @Operation(summary = "Lade alle Häuser für Kinder", description = "Das Ergebnis wird nach Name der Einrichtung aufsteigend sortiert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_HAUS_FUER_KINDER.name())")
    public ResponseEntity<List<HausFuerKinderDto>> getHaeuserFuerKinder() {
        final List<HausFuerKinderDto> hausFuerKinderList = this.hausFuerKinderService.getHaeuserFuerKinder()
                .stream()
                .map(this.infrastruktureinrichtungApiMapper::model2Dto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(hausFuerKinderList, HttpStatus.OK);
    }

    @GetMapping("haus-fuer-kinder/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Lesen eines Hauses für Kinder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Haus für Kinder mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_HAUS_FUER_KINDER.name())")
    public ResponseEntity<HausFuerKinderDto> getHausFuerKinderById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException {
        final var model = this.hausFuerKinderService.getHausFuerKinderById(id);
        final var dto = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("haus-fuer-kinder")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Anlegen eines neuen Hauses für Kinder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Haus für Kinder wurde erfolgreich erstellt."),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST -> Haus für Kinder konnte nicht erstellt werden, überprüfen sie die Eingabe.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "412", description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_HAUS_FUER_KINDER.name())")
    public ResponseEntity<HausFuerKinderDto> createHausFuerKinder(@RequestBody @Valid @NotNull final HausFuerKinderDto hausFuerKinderDto) throws EntityNotFoundException, OptimisticLockingException {
        var model = this.infrastruktureinrichtungApiMapper.dto2Model(hausFuerKinderDto);
        final var infrastruktureinrichtung = this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(
                hausFuerKinderDto.getInfrastruktureinrichtung().getBauvorhaben(),
                model.getInfrastruktureinrichtung()
        );
        model.setInfrastruktureinrichtung(infrastruktureinrichtung);
        model = this.hausFuerKinderService.saveHausFuerKinder(model);
        final var saved = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("haus-fuer-kinder")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Aktualisierung einer Hauses für Kinder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Haus für Kinder wurde erfolgreich aktualisiert."),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST -> Haus für Kinder konnte nicht aktualisiert werden, überprüfen sie die Eingabe.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt kein Haus für Kinder mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "412", description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_HAUS_FUER_KINDER.name())")
    public ResponseEntity<HausFuerKinderDto> updateHausFuerKinder(@RequestBody @Valid @NotNull final HausFuerKinderDto hausFuerKinderDto) throws EntityNotFoundException, OptimisticLockingException {
        var model = this.infrastruktureinrichtungApiMapper.dto2Model(hausFuerKinderDto);
        final var infrastruktureinrichtung = this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(
                hausFuerKinderDto.getInfrastruktureinrichtung().getBauvorhaben(),
                model.getInfrastruktureinrichtung()
        );
        model.setInfrastruktureinrichtung(infrastruktureinrichtung);
        model = this.hausFuerKinderService.updateHausFuerKinder(model);
        final var saved = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("haus-fuer-kinder/{id}")
    @Operation(summary = "Löschen eines Hauses für Kinder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Haus für Kinder mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Das Haus für Kinder referenziert ein Bauvorhaben.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @Transactional
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_HAUS_FUER_KINDER.name())")
    public ResponseEntity<Void> deleteHausFuerKinderById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        this.hausFuerKinderService.deleteHausFuerKinderById(id);
        return ResponseEntity.noContent().build();
    }

}
