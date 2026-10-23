package de.muenchen.isi.api.controller.common;

import de.muenchen.isi.api.dto.common.KommentarBauvorhabenDto;
import de.muenchen.isi.api.dto.common.KommentarInfrastruktureinrichtungDto;
import de.muenchen.isi.api.dto.enums.InformationResponseType;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.KommentarApiMapper;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.service.common.KommentarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/kommentar")
@Tag(name = "Kommentare", description = "API um Kommentare zu verwalten")
@Validated
public class KommentarController {

    private final KommentarService kommentarService;

    private final KommentarApiMapper kommentarApiMapper;

    @GetMapping("/all/bauvorhaben/{bauvorhabenId}")
    @Transactional(readOnly = true)
    @Operation(summary = "Holen der Kommentare eines Bauvorhabens")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_KOMMENTAR_BAUVORHABEN.name())"
    )
    public ResponseEntity<List<KommentarBauvorhabenDto>> getKommentareForBauvorhaben(
        @PathVariable @NotNull final UUID bauvorhabenId
    ) {
        final var models = kommentarService.getKommentareForBauvorhaben(bauvorhabenId);
        final var dtos = models.stream().map(kommentarApiMapper::model2Dto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/all/infrastruktureinrichtung/{infrastruktureinrichtungId}")
    @Transactional(readOnly = true)
    @Operation(summary = "Holen der Kommentare einer Infrastruktureinrichtung")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_KOMMENTAR_INFRASTRUKTUREINRICHTUNG.name())"
    )
    public ResponseEntity<List<KommentarInfrastruktureinrichtungDto>> getKommentareForInfrastruktureinrichtung(
        @PathVariable @NotNull final UUID infrastruktureinrichtungId
    ) {
        final var models = kommentarService.getKommentareForInfrastruktureinrichtung(infrastruktureinrichtungId);
        final var dtos = models.stream().map(kommentarApiMapper::model2Dto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("bauvorhaben")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Anlegen eines neuen Kommentars für ein Bauvorhaben")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Kommentar wurde erfolgreich erstellt."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Kommentar konnte nicht erstellt werden, überprüfen sie die Eingabe.",
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
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_KOMMENTAR_BAUVORHABEN.name())"
    )
    public ResponseEntity<KommentarBauvorhabenDto> createKommentarForBauvorhaben(
        @RequestBody @Valid @NotNull final KommentarBauvorhabenDto kommentarDto
    ) throws EntityNotFoundException, OptimisticLockingException {
        if (kommentarDto.getId() != null || kommentarDto.getBauvorhaben() == null) {
            return ResponseEntity.badRequest().build();
        }
        var model = this.kommentarApiMapper.dto2Model(kommentarDto);
        model = this.kommentarService.saveKommentarForBauvorhaben(model);
        final var saved = this.kommentarApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("infrastruktureinrichtung")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Anlegen eines neuen Kommentars für eine Infrastruktureinrichtung")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Kommentar wurde erfolgreich erstellt."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Kommentar konnte nicht erstellt werden, überprüfen sie die Eingabe.",
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
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_KOMMENTAR_INFRASTRUKTUREINRICHTUNG.name())"
    )
    public ResponseEntity<KommentarInfrastruktureinrichtungDto> createKommentarForInfrastruktureinrichtung(
        @RequestBody @Valid @NotNull final KommentarInfrastruktureinrichtungDto kommentarDto
    ) throws EntityNotFoundException, OptimisticLockingException {
        if (kommentarDto.getId() != null || kommentarDto.getInfrastruktureinrichtung() == null) {
            return ResponseEntity.badRequest().build();
        }
        var model = this.kommentarApiMapper.dto2Model(kommentarDto);
        model = this.kommentarService.saveKommentarForInfrastruktureinrichtung(model);
        final var saved = this.kommentarApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("infrastruktureinrichtung/bedarfsmeldung_sozialplanung")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(
        summary = "Anlegen eines neuen Kommentars für eine Infrastruktureinrichtung für die Rolle Bedarfsmeldung Sozialplanung"
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "CREATED -> Kommentar wurde erfolgreich erstellt."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Kommentar konnte nicht erstellt werden, überprüfen sie die Eingabe.",
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
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_KOMMENTAR_INFRASTRUKTUREINRICHTUNG_BEDARFSMELDUNG_SOZIALPLANUNG.name())"
    )
    public ResponseEntity<KommentarInfrastruktureinrichtungDto> createKommentarForInfrastruktureinrichtungBedarfsmeldungSozialplanung(
        @RequestBody @Valid @NotNull final KommentarInfrastruktureinrichtungDto kommentarDto
    ) throws EntityNotFoundException, OptimisticLockingException {
        if (kommentarDto.getId() != null || kommentarDto.getInfrastruktureinrichtung() == null) {
            return ResponseEntity.badRequest().build();
        }
        var model = this.kommentarApiMapper.dto2Model(kommentarDto);
        model = this.kommentarService.saveKommentarForInfrastruktureinrichtung(model);
        final var saved = this.kommentarApiMapper.model2Dto(model);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("bauvorhaben")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Aktualisierung eines Kommentars eines Bauvorhabens")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Kommentar wurde erfolgreich aktualisiert."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Kommentar konnte nicht erstellt werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Kommentar mit dieser ID nicht vorhanden.",
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
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_KOMMENTAR_BAUVORHABEN.name())"
    )
    public ResponseEntity<KommentarBauvorhabenDto> updateKommentarForBauvorhaben(
        @RequestBody @Valid @NotNull final KommentarBauvorhabenDto kommentarDto
    ) throws EntityNotFoundException, OptimisticLockingException, FileHandlingFailedException {
        final var savedKommentar = this.kommentarService.getKommentarEntityById(kommentarDto.getId());
        if (
            savedKommentar.getBauvorhaben() == null ||
            !savedKommentar.getBauvorhaben().getId().equals(kommentarDto.getBauvorhaben())
        ) {
            return ResponseEntity.badRequest().build();
        }
        var model = this.kommentarApiMapper.dto2Model(kommentarDto);
        model = this.kommentarService.updateKommentarForBauvorhaben(model);
        final var saved = this.kommentarApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("infrastruktureinrichtung")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Aktualisierung eines Kommentars für eine Infrastruktureinrichtung")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Kommentar wurde erfolgreich aktualisiert."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Kommentar konnte nicht erstellt werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Kommentar mit dieser ID nicht vorhanden.",
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
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_KOMMENTAR_INFRASTRUKTUREINRICHTUNG.name())"
    )
    public ResponseEntity<KommentarInfrastruktureinrichtungDto> updateKommentarForInfrastruktureinrichtung(
        @RequestBody @Valid @NotNull final KommentarInfrastruktureinrichtungDto kommentarDto
    ) throws EntityNotFoundException, OptimisticLockingException, FileHandlingFailedException {
        final var savedKommentar = this.kommentarService.getKommentarEntityById(kommentarDto.getId());
        if (
            savedKommentar.getInfrastruktureinrichtung() == null ||
            !savedKommentar.getInfrastruktureinrichtung().getId().equals(kommentarDto.getInfrastruktureinrichtung())
        ) {
            return ResponseEntity.badRequest().build();
        }
        var model = this.kommentarApiMapper.dto2Model(kommentarDto);
        model = this.kommentarService.updateKommentarForInfrastruktureinrichtung(model);
        final var saved = this.kommentarApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("infrastruktureinrichtung/bedarfsmeldung_sozialplanung")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(
        summary = "Aktualisierung eines Kommentars für eine Infrastruktureinrichtung für die Rolle Bedarfsmeldung Sozialplanung"
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Kommentar wurde erfolgreich aktualisiert."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Kommentar konnte nicht erstellt werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Kommentar mit dieser ID nicht vorhanden.",
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
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WRITE_KOMMENTAR_INFRASTRUKTUREINRICHTUNG_BEDARFSMELDUNG_SOZIALPLANUNG.name())"
    )
    public ResponseEntity<KommentarInfrastruktureinrichtungDto> updateKommentarForInfrastruktureinrichtungBedarfsmeldungSozialplanung(
        @RequestBody @Valid @NotNull final KommentarInfrastruktureinrichtungDto kommentarDto
    ) throws EntityNotFoundException, OptimisticLockingException, FileHandlingFailedException {
        final var savedKommentar = this.kommentarService.getKommentarEntityById(kommentarDto.getId());
        if (
            savedKommentar.getInfrastruktureinrichtung() == null ||
            !savedKommentar.getInfrastruktureinrichtung().getId().equals(kommentarDto.getInfrastruktureinrichtung())
        ) {
            return ResponseEntity.badRequest().build();
        }
        var model = this.kommentarApiMapper.dto2Model(kommentarDto);
        model = this.kommentarService.updateKommentarForInfrastruktureinrichtung(model);
        final var saved = this.kommentarApiMapper.model2Dto(model);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("bauvorhaben/{id}")
    @Transactional
    @Operation(summary = "Löschen eines Kommentars eines Bauvorhabens")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Kommentar konnte nicht gelöscht werden",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Kommentar mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_KOMMENTAR_BAUVORHABEN.name())"
    )
    public ResponseEntity<Object> deleteKommentarForBauvorhaben(@PathVariable @NotNull final UUID id)
        throws EntityNotFoundException {
        final var kommentar = this.kommentarService.getKommentarEntityById(id);
        // Verify it's a Bauvorhaben comment
        if (kommentar.getBauvorhaben() == null) {
            // Kommentar gehört nicht zu einem Bauvorhaben -> ablehnen
            return buildBadRequestInformationResponse("Kommentar gehört nicht zu einem Bauvorhaben");
        }
        this.kommentarService.deleteKommentarById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("infrastruktureinrichtung/{id}")
    @Transactional
    @Operation(summary = "Löschen eines Kommentars einer Infrastruktureinrichtung")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Kommentar konnte nicht gelöscht werden",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Kommentar mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_KOMMENTAR_INFRASTRUKTUREINRICHTUNG.name())"
    )
    public ResponseEntity<Object> deleteKommentarForInfrastruktureinrichtung(@PathVariable @NotNull final UUID id)
        throws EntityNotFoundException {
        final var kommentar = this.kommentarService.getKommentarEntityById(id);
        // Überprüfen ob der Kommentar zu einer Infrastruktureinrichtung gehört
        if (kommentar.getInfrastruktureinrichtung() == null) {
            // Kommentar gehört nicht zu einer Infrastruktureinrichtung -> ablehnen
            return buildBadRequestInformationResponse("Kommentar gehört nicht zu einer Infrastruktureinrichtung");
        }
        this.kommentarService.deleteKommentarById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("infrastruktureinrichtung/bedarfsmeldung_sozialplanung/{id}")
    @Transactional
    @Operation(
        summary = "Löschen eines Kommentars einer Infrastruktureinrichtung für die Rolle Bedarfsmeldung Sozialplanung"
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Kommentar konnte nicht gelöscht werden",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Kommentar mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DELETE_KOMMENTAR_INFRASTRUKTUREINRICHTUNG_BEDARFSMELDUNG_SOZIALPLANUNG.name())"
    )
    public ResponseEntity<Object> deleteKommentarForInfrastruktureinrichtungBedarfsmeldungSozialplanung(
        @PathVariable @NotNull final UUID id
    ) throws EntityNotFoundException {
        final var kommentar = this.kommentarService.getKommentarEntityById(id);
        // Überprüfen ob der Kommentar zu einer Infrastruktureinrichtung gehört
        if (kommentar.getInfrastruktureinrichtung() == null) {
            // Kommentar gehört nicht zu einer Infrastruktureinrichtung -> ablehnen
            return buildBadRequestInformationResponse("Kommentar gehört nicht zu einer Infrastruktureinrichtung");
        }
        this.kommentarService.deleteKommentarById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Object> buildBadRequestInformationResponse(final String message) {
        final var errorResponseDto = new InformationResponseDto();
        errorResponseDto.setType(InformationResponseType.ERROR);
        errorResponseDto.setTimestamp(LocalDateTime.now());
        errorResponseDto.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDto.setMessages(List.of(message));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
}
