package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.common.TransitionDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.TransitionApiMapper;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.StringLengthExceededException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.service.AbfrageStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "AbfrageStatus", description = "API to set the status for a Abfrage")
@Validated
@RequestMapping(value = "abfrage-status")
public class AbfrageStatusController {

    private final AbfrageStatusService abfrageStatusService;

    private final TransitionApiMapper transitionApiMapper;

    @GetMapping("{id}/transitions")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(
        summary = "Holt alle möglichen StatusAbfrage Transitions auf Basis der Authorities und des aktuellen Status"
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Alle mögliche Transistions gefunden"),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Sie haben keine Berechtigung, den Statuswechsel durchzuführen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_TRANSITIONS.name())")
    public ResponseEntity<List<TransitionDto>> transitionsAbfrage(@PathVariable @NotNull final UUID id)
        throws EntityNotFoundException, UserRoleNotAllowedException {
        final List<TransitionDto> transistions = abfrageStatusService
            .getStatusAbfrageEventsBasedOnStateAndAuthorities(id)
            .stream()
            .map(this.transitionApiMapper::model2Dto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(transistions);
    }

    @PutMapping("{id}/freigabe")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Setzt eine Abfrage auf den Status OFFEN")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Sie haben keine Berechtigung, den Statuswechsel durchzuführen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht freigegeben werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_FREIGABE_ABFRAGE.name())")
    public ResponseEntity<Void> freigabeAbfrage(
        @PathVariable @NotNull final UUID id,
        @RequestParam(value = "anmerkung", required = false, defaultValue = "") String anmerkung
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.abfrageStatusService.freigabeAbfrage(id, anmerkung);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/abbrechen")
    @Transactional
    @Operation(summary = "Setzt eine Abfrage auf den Status ABBRUCH")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich abbgebrochen."),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Sie haben keine Berechtigung, den Statuswechsel durchzuführen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht abgebrochen werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_ABBRECHEN_ABFRAGE.name())")
    public ResponseEntity<Void> abbrechenAbfrage(
        @PathVariable @NotNull final UUID id,
        @RequestParam(value = "anmerkung", required = false, defaultValue = "") String anmerkung
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.abfrageStatusService.abbrechenAbfrage(id, anmerkung);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/zurueck-an-abfrageerstellung")
    @Transactional
    @Operation(summary = "Setzt eine Abfrage auf den Status ANGELEGT")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "OK -> Abfrage wurde erfolgreich zurückgegeben an den Abfrage Ersteller."
            ),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Sie haben keine Berechtigung, den Statuswechsel durchzuführen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht zur Bearbeitung zurückgegeben werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_ZURUECK_AN_ABFRAGEERSTELLUNG_ABFRAGE.name())"
    )
    public ResponseEntity<Void> zurueckAnAbfrageerstellungAbfrage(
        @PathVariable @NotNull final UUID id,
        @RequestParam(value = "anmerkung", required = false, defaultValue = "") String anmerkung
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.abfrageStatusService.zurueckAnAbfrageerstellungAbfrage(id, anmerkung);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/in-bearbeitung-setzen")
    @Transactional
    @Operation(summary = "Setzt eine Abfrage auf den Status IN_BEARBEITUNG_SACHBEARBEITUNG")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "OK -> Abfrage wurde erfolgreich zurückgegeben an den Abfrage Ersteller."
            ),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Sie haben keine Berechtigung, den Statuswechsel durchzuführen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht zur Bearbeitung zurückgegeben werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_IN_BEARBEITUNG_SETZTEN_ABFRAGE.name())"
    )
    public ResponseEntity<Void> inBearbeitungSetzenAbfrage(
        @PathVariable @NotNull final UUID id,
        @RequestParam(value = "anmerkung", required = false, defaultValue = "") String anmerkung
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.abfrageStatusService.inBearbeitungSetzenAbfrage(id, anmerkung);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/zurueck-an-sachbearbeitung")
    @Transactional
    @Operation(summary = "Setzt eine Abfrage auf den Status IN_BEARBEITUNG_SACHBEARBEITUNG")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "OK -> Abfrage wurde erfolgreich zurückgegeben an den Abfrage Ersteller."
            ),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Sie haben keine Berechtigung, den Statuswechsel durchzuführen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht zur Bearbeitung zurückgegeben werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_ZURUECK_AN_SACHBEARBEITUNG_ABFRAGE.name())"
    )
    public ResponseEntity<Void> zurueckAnSachbearbeitungAbfrage(
        @PathVariable @NotNull final UUID id,
        @RequestParam(value = "anmerkung", required = false, defaultValue = "") String anmerkung
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.abfrageStatusService.zurueckAnSachbearbeitungAbfrage(id, anmerkung);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/erledigt-ohne-fachreferat")
    @Transactional
    @Operation(summary = "Setzt eine Abfrage auf den Status ERLEDIGT_OHNE_FACHREFERAT")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich erledigt."),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Sie haben keine Berechtigung, den Statuswechsel durchzuführen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht erledgit werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_KEINE_BEARBEITUNG_NOETIG_ABFRAGE.name())"
    )
    public ResponseEntity<Void> erledigtOhneFachreferat(
        @PathVariable @NotNull final UUID id,
        @RequestParam(value = "anmerkung", required = false, defaultValue = "") String anmerkung
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.abfrageStatusService.erledigtOhneFachreferat(id, anmerkung);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/verschicken-der-stellungnahme")
    @Transactional
    @Operation(summary = "Setzt eine Abfrage auf den Status IN_BEARBEITUNG_FACHREFERATE")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "OK -> Abfrage wurde erfolgreich an RBS oder SOZ zur Bearbeitung weitergegeben."
            ),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Sie haben keine Berechtigung, den Statuswechsel durchzuführen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht weitergegeben werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_VERSCHICKEN_DER_STELLUNGNAHME_ABFRAGE.name())"
    )
    public ResponseEntity<Void> verschickenDerStellungnahme(
        @PathVariable @NotNull final UUID id,
        @RequestParam(value = "anmerkung", required = false, defaultValue = "") String anmerkung
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.abfrageStatusService.verschickenDerStellungnahme(id, anmerkung);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/bedarfsmeldung-erfolgt")
    @Transactional
    @Operation(summary = "Setzt eine Abfrage auf den Status BEDARFSMELDUNG_ERFOLGT")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Die Bedarfsmeldung der Fachreferate ist erfolgt"),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Sie haben keine Berechtigung, den Statuswechsel durchzuführen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Bedarfsmeldung konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht erfolgen",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_BEDARFSMELDUNG_ERFOLGTE_ABFRAGE.name())"
    )
    public ResponseEntity<Void> bedarfsmeldungErfolgt(
        @PathVariable @NotNull final UUID id,
        @RequestParam(value = "anmerkung", required = false, defaultValue = "") String anmerkung
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.abfrageStatusService.bedarfsmeldungErfolgt(id, anmerkung);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/erledigt-mit-fachreferat")
    @Transactional
    @Operation(summary = "Setzt eine Abfrage auf den Status ERLEDIGT_MIT_FACHREFERAT")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich erledigt."),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Sie haben keine Berechtigung, den Statuswechsel durchzuführen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht erledigt werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG_ABFRAGE.name())"
    )
    public ResponseEntity<Void> erledigtMitFachreferat(
        @PathVariable @NotNull final UUID id,
        @RequestParam(value = "anmerkung", required = false, defaultValue = "") String anmerkung
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.abfrageStatusService.erledigtMitFachreferat(id, anmerkung);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/erneute-bearbeitung-sachbearbeitung")
    @Transactional
    @Operation(summary = "Setzt eine Abfrage auf den Status IN_BEARBEITUNG_SACHBEARBEITUNG")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "OK -> Abfrage wurde erfolgreich zur bearbeitung freigestellt."
            ),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Sie haben keine Berechtigung, den Statuswechsel durchzuführen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht erledigt werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_ERNEUTE_BEARBEITUNG_ABFRAGE.name())"
    )
    public ResponseEntity<Void> erneuteBearbeitungSachbearbeitung(
        @PathVariable @NotNull final UUID id,
        @RequestParam(value = "anmerkung", required = false, defaultValue = "") String anmerkung
    )
        throws EntityNotFoundException, AbfrageStatusNotAllowedException, StringLengthExceededException, UserRoleNotAllowedException {
        this.abfrageStatusService.erneuteBearbeitungSachbearbeitung(id, anmerkung);
        return ResponseEntity.ok().build();
    }
}
