package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.BauvorhabenDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.dto.list.AbfrageListElementDto;
import de.muenchen.isi.api.dto.list.InfrastruktureinrichtungListElementDto;
import de.muenchen.isi.api.mapper.AbfrageApiMapper;
import de.muenchen.isi.api.mapper.BauvorhabenApiMapper;
import de.muenchen.isi.api.mapper.InfrastruktureinrichtungApiMapper;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import de.muenchen.isi.domain.service.BauvorhabenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Bauvorhaben", description = "API um Bauvorhaben zu verwalten")
@Validated
public class BauvorhabenController {

    private final BauvorhabenService bauvorhabenService;

    private final BauvorhabenApiMapper bauvorhabenApiMapper;

    private final AbfrageApiMapper abfrageApiMapper;

    private final InfrastruktureinrichtungApiMapper infrastruktureinrichtungApiMapper;

    @Transactional(readOnly = true)
    @GetMapping("bauvorhaben")
    @Operation(
        summary = "Lade alle Bauvorhaben",
        description = "Das Ergebnis wird nach der Größe des Grundstückes absteigend sortiert"
    )
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_BAUVORHABEN.name())")
    public ResponseEntity<List<BauvorhabenDto>> getBauvorhaben() {
        final List<BauvorhabenDto> bauvorhabenList =
            this.bauvorhabenService.getBauvorhaben()
                .stream()
                .map(this.bauvorhabenApiMapper::model2Dto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bauvorhabenList, HttpStatus.OK);
    }

    @GetMapping("bauvorhaben/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Lesen eines Bauvorhabens")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Bauvorhaben mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_BAUVORHABEN.name())")
    public ResponseEntity<BauvorhabenDto> getBauvorhabenById(@PathVariable @NotNull final UUID id)
        throws EntityNotFoundException {
        final var model = this.bauvorhabenService.getBauvorhabenById(id);
        final var dto = this.bauvorhabenApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("bauvorhaben")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Anlegen eines neuen Bauvorhabens")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Bauvorhaben wurde erfolgreich erstellt."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Bauvorhaben konnte nicht erstellt werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Die ausgewählte Abfrage existiert nicht mehr.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Bauvorhaben konnte nicht erstellt werden, da der Vorhabensname bereits existiert oder bei einer Datenübernahme die Abfrage bereits ein Bauvorhaben referenziert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "412",
                description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_BAUVORHABEN.name())")
    public ResponseEntity<BauvorhabenDto> createBauvorhaben(
        @RequestBody @Valid @NotNull final BauvorhabenDto bauvorhabenDto,
        @RequestParam(required = false) final UUID abfrage
    )
        throws UniqueViolationException, OptimisticLockingException, EntityNotFoundException, EntityIsReferencedException {
        var model = this.bauvorhabenApiMapper.dto2Model(bauvorhabenDto);
        model = this.bauvorhabenService.saveBauvorhaben(model, abfrage);
        final var saved = this.bauvorhabenApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("bauvorhaben")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Aktualisierung eines Bauvorhabens")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Bauvorhaben wurde erfolgreich aktualisiert."),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Bauvorhaben mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Bauvorhaben konnte nicht aktualisiert werden, da der Vorhabensname bereits existiert oder bei einer Datenübernahme die Abfrage bereits ein Bauvorhaben referenziert.",
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
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_BAUVORHABEN.name())")
    public ResponseEntity<BauvorhabenDto> updateBauvorhaben(
        @RequestBody @Valid @NotNull final BauvorhabenDto bauvorhabenDto
    )
        throws EntityNotFoundException, UniqueViolationException, OptimisticLockingException, FileHandlingFailedException, FileHandlingWithS3FailedException, EntityIsReferencedException {
        var model = this.bauvorhabenApiMapper.dto2Model(bauvorhabenDto);
        model = this.bauvorhabenService.updateBauvorhaben(model);
        final var saved = this.bauvorhabenApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("bauvorhaben/{id}")
    @Operation(summary = "Löschen eines Bauvorhabens")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Bauvorhaben mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Das Bauvorhaben wird durch Abfragen referenziert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @Transactional
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_BAUVORHABEN.name())")
    public ResponseEntity<Void> deleteBauvorhaben(@PathVariable @NotNull final UUID id)
        throws EntityNotFoundException, EntityIsReferencedException {
        this.bauvorhabenService.deleteBauvorhaben(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional(readOnly = true)
    @GetMapping("bauvorhaben/referenced/abfragen/{id}")
    @Operation(
        summary = "Lade alle Infrastrukturabfragen die einem Bauvorhaben angehören",
        description = "Das Ergebnis wird anhand des Erstellungsdatums aufsteigend sortiert."
    )
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_BAUVORHABEN.name())")
    public ResponseEntity<List<AbfrageListElementDto>> getReferencedInfrastrukturabfragen(
        @PathVariable @NotNull final UUID id
    ) {
        final var infrastrukturabfragen =
            this.bauvorhabenService.getReferencedInfrastrukturabfragen(id)
                .stream()
                .map(this.abfrageApiMapper::model2ListElementDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(infrastrukturabfragen, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @GetMapping("bauvorhaben/referenced/infrastruktureinrichtung/{id}")
    @Operation(
        summary = "Lade alle Infrastruktureinrichtungen die einem Bauvorhaben angehören",
        description = "Das Ergebnis wird anhand des InfrastruktureinrichtungTyps und innerhalb des Types alphabetisch sortiert"
    )
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_BAUVORHABEN.name())")
    public ResponseEntity<List<InfrastruktureinrichtungListElementDto>> getReferencedInfrastruktureinrichtung(
        @PathVariable @NotNull final UUID id
    ) {
        final var infrastruktureinrichtungen =
            this.bauvorhabenService.getReferencedInfrastruktureinrichtungen(id)
                .stream()
                .map(this.infrastruktureinrichtungApiMapper::model2ListElementDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(infrastruktureinrichtungen, HttpStatus.OK);
    }
}
