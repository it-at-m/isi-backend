/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.controller.infrastruktureinrichtung;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.dto.infrastruktureinrichtung.KinderkrippeDto;
import de.muenchen.isi.api.mapper.InfrastruktureinrichtungApiMapper;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.service.BauvorhabenService;
import de.muenchen.isi.domain.service.infrastruktureinrichtung.KinderkrippeService;
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
@Tag(name = "Kinderkrippe", description = "API to interact with the Kinderkrippe")
@Validated
public class KinderkrippeController {

    private final KinderkrippeService kinderkrippeService;

    private final BauvorhabenService bauvorhabenService;

    private final InfrastruktureinrichtungApiMapper infrastruktureinrichtungApiMapper;

    @Transactional(readOnly = true)
    @GetMapping("kinderkrippen")
    @Operation(summary = "Lade alle Kinderkrippen", description = "Das Ergebnis wird nach Name der Einrichtung aufsteigend sortiert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_KINDERKRIPPE.name())")
    public ResponseEntity<List<KinderkrippeDto>> getKinderkrippen() {
        final List<KinderkrippeDto> kinderkrippeList = this.kinderkrippeService.getKinderkrippen()
                .stream()
                .map(this.infrastruktureinrichtungApiMapper::model2Dto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(kinderkrippeList, HttpStatus.OK);
    }

    @GetMapping("kinderkrippe/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Lesen einer Kinderkrippe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Kinderkrippe mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_KINDERKRIPPE.name())")
    public ResponseEntity<KinderkrippeDto> getKinderkrippeById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException {
        final var model = this.kinderkrippeService.getKinderkrippeById(id);
        final var dto = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("kinderkrippe")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Anlegen einer neuen Kinderkrippe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Kinderkrippe wurde erfolgreich erstellt."),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST -> Kinderkrippe konnte nicht erstellt werden, überprüfen sie die Eingabe.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "412", description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_KINDERKRIPPE.name())")
    public ResponseEntity<KinderkrippeDto> createKinderkrippe(@RequestBody @Valid @NotNull final KinderkrippeDto kinderkrippeDto) throws EntityNotFoundException, OptimisticLockingException {
        var model = this.infrastruktureinrichtungApiMapper.dto2Model(kinderkrippeDto);
        final var infrastruktureinrichtung = this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(
                kinderkrippeDto.getInfrastruktureinrichtung().getBauvorhaben(),
                model.getInfrastruktureinrichtung()
        );
        model.setInfrastruktureinrichtung(infrastruktureinrichtung);
        model = this.kinderkrippeService.saveKinderkrippe(model);
        final var saved = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("kinderkrippe")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Aktualisierung einer Kinderkrippe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Kinderkrippe wurde erfolgreich aktualisiert."),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST -> Kinderkrippe konnte nicht aktualisiert werden, überprüfen sie die Eingabe.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Kinderkrippe mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "412", description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_KINDERKRIPPE.name())")
    public ResponseEntity<KinderkrippeDto> updateKinderkrippe(@RequestBody @Valid @NotNull final KinderkrippeDto kinderkrippeDto) throws EntityNotFoundException, OptimisticLockingException {
        var model = this.infrastruktureinrichtungApiMapper.dto2Model(kinderkrippeDto);
        final var infrastruktureinrichtung = this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(
                kinderkrippeDto.getInfrastruktureinrichtung().getBauvorhaben(),
                model.getInfrastruktureinrichtung()
        );
        model.setInfrastruktureinrichtung(infrastruktureinrichtung);
        model = this.kinderkrippeService.updateKinderkrippe(model);
        final var saved = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("kinderkrippe/{id}")
    @Operation(summary = "Löschen einer Kinderkrippe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Kinderkrippe mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Kinderkrippe referenziert ein Bauvorhaben.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @Transactional
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_KINDERKRIPPE.name())")
    public ResponseEntity<Void> deleteKinderkrippeById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        this.kinderkrippeService.deleteKinderkrippeById(id);
        return ResponseEntity.noContent().build();
    }

}
