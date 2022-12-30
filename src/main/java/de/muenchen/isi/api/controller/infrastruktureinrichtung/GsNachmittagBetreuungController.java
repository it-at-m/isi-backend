/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.controller.infrastruktureinrichtung;

import de.muenchen.isi.api.dto.infrastruktureinrichtung.GsNachmittagBetreuungDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.InfrastruktureinrichtungApiMapper;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.service.BauvorhabenService;
import de.muenchen.isi.domain.service.infrastruktureinrichtung.GsNachmittagBetreuungService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "GsNachmittagBetreuung", description = "API to interact with the GsNachmittagBetreuung")
@Validated
public class GsNachmittagBetreuungController {

    private final GsNachmittagBetreuungService gsNachmittagBetreuungService;

    private final BauvorhabenService bauvorhabenService;

    private final InfrastruktureinrichtungApiMapper infrastruktureinrichtungApiMapper;

    @Transactional(readOnly = true)
    @GetMapping("gs-nachmittag-betreuungen")
    @Operation(summary = "Lade alle Nachmittagsbetreuungen für Grundschulkinder", description = "Das Ergebnis wird nach Name der Einrichtung aufsteigend sortiert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_GS_NACHMITTAG_BETREUUNG.name())")
    public ResponseEntity<List<GsNachmittagBetreuungDto>> getGsNachmittagBetreuungen() {
        final List<GsNachmittagBetreuungDto> gsNachmittagBetreuungList = this.gsNachmittagBetreuungService.getGsNachmittagBetreuungen()
                .stream()
                .map(this.infrastruktureinrichtungApiMapper::model2Dto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(gsNachmittagBetreuungList, HttpStatus.OK);
    }

    @GetMapping("gs-nachmittag-betreuung/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Lesen einer Nachmittagsbetreuung für Grundschulkinder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Nachmittagsbetreuung für Grundschulkinder mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_GS_NACHMITTAG_BETREUUNG.name())")
    public ResponseEntity<GsNachmittagBetreuungDto> getGsNachmittagBetreuungById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException {
        final var model = this.gsNachmittagBetreuungService.getGsNachmittagBetreuungById(id);
        final var dto = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("gs-nachmittag-betreuung")
    @Transactional
    @Operation(summary = "Anlegen einer neuen Nachmittagsbetreuung für Grundschulkinder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Nachmittagsbetreuung für Grundschulkinder wurde erfolgreich erstellt."),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST -> Nachmittagsbetreuung für Grundschulkinder konnte nicht erstellt werden, überprüfen sie die Eingabe.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_GS_NACHMITTAG_BETREUUNG.name())")
    public ResponseEntity<GsNachmittagBetreuungDto> createGsNachmittagBetreuung(@RequestBody @Valid @NotNull final GsNachmittagBetreuungDto gsNachmittagBetreuungDto) throws EntityNotFoundException {
        var model = this.infrastruktureinrichtungApiMapper.dto2Model(gsNachmittagBetreuungDto);
        final var infrastruktureinrichtung = this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(
                gsNachmittagBetreuungDto.getInfrastruktureinrichtung().getBauvorhaben(),
                model.getInfrastruktureinrichtung()
        );
        model.setInfrastruktureinrichtung(infrastruktureinrichtung);
        model = this.gsNachmittagBetreuungService.saveGsNachmittagBetreuung(model);
        final var saved = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("gs-nachmittag-betreuung")
    @Transactional
    @Operation(summary = "Aktualisierung einer Nachmittagsbetreuung für Grundschulkinder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Nachmittagsbetreuung für Grundschulkinder wurde erfolgreich aktualisiert."),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST -> Nachmittagsbetreuung für Grundschulkinder konnte nicht aktualisiert werden, überprüfen sie die Eingabe.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Nachmittagsbetreuung für Grundschulkinder mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_GS_NACHMITTAG_BETREUUNG.name())")
    public ResponseEntity<GsNachmittagBetreuungDto> updateGsNachmittagBetreuung(@RequestBody @Valid @NotNull final GsNachmittagBetreuungDto gsNachmittagBetreuungDto) throws EntityNotFoundException {
        var model = this.infrastruktureinrichtungApiMapper.dto2Model(gsNachmittagBetreuungDto);
        final var infrastruktureinrichtung = this.bauvorhabenService.assignBauvorhabenToInfrastruktureinrichtung(
                gsNachmittagBetreuungDto.getInfrastruktureinrichtung().getBauvorhaben(),
                model.getInfrastruktureinrichtung()
        );
        model.setInfrastruktureinrichtung(infrastruktureinrichtung);
        model = this.gsNachmittagBetreuungService.updateGsNachmittagBetreuung(model);
        final var saved = this.infrastruktureinrichtungApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("gs-nachmittag-betreuung/{id}")
    @Operation(summary = "Löschen einer Nachmittagsbetreuung für Grundschulkinder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND -> Nachmittagsbetreuung für Grundschulkinder mit dieser ID nicht vorhanden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Nachmittagsbetreuung für Grundschulkinder referenziert ein Bauvorhaben.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @Transactional
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_GS_NACHMITTAG_BETREUUNG.name())")
    public ResponseEntity<Void> deleteGsNachmittagBetreuungById(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, EntityIsReferencedException {
        this.gsNachmittagBetreuungService.deleteGsNachmittagBetreuungById(id);
        return ResponseEntity.ok().build();
    }

}
