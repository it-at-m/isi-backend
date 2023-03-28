package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.common.TransitionDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.TransitionApiMapper;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.service.AbfrageStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@Tag(name = "AbfrageStatus", description = "API to set the status for a Abfrage")
@Validated
@RequestMapping(value = "infrastruktur-abfrage")
public class AbfrageStatusController {

    private final AbfrageStatusService abfrageStatusService;

    private final TransitionApiMapper transitionApiMapper;


    @GetMapping("{id}/transitions")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Hollt alle möglichen StatusAbfrage Transitions auf Basis der Rolle und des akutellen Status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Alle mögliche Transistions gefunden"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_FREIGABE_ABFRAGE.name())")
    public ResponseEntity<List<TransitionDto>> transitions(@PathVariable @NotNull final UUID id) throws EntityNotFoundException {
        final List<TransitionDto> transistions = abfrageStatusService.getStatusAbfrageEventsBasedOnStateAndRole(id)
                .stream()
                .map(this.transitionApiMapper::model2Dto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transistions);
    }

    @PutMapping("{id}/freigabe")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf den Status OFFEN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_FREIGABE_ABFRAGE.name())")
    public ResponseEntity<Void> freigabeInfrastrukturabfrage(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.freigabeAbfrage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/abbrechen")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf den Status ABBRUCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich abbgebrochen."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht abgebrochen werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_ABBRECHEN_ABFRAGE.name())")
    public ResponseEntity<Void> abbrechenInfrastrukturabfrage(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.abbrechenAbfrage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/angabe-anpassen")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf den Status ANGELEGT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich zurückgegeben an den Abfrage Ersteller."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht zur Bearbeitung zurückgegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_ANGABEN_ANPASSEN_ABFRAGE.name())")
    public ResponseEntity<Void> angabenAnpassenInfrastrukturabfrage(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.angabenAnpassenAbfrage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/in-bearbeitung-setzen")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf den Status IN_BEARBEITUNG_PLAN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich zurückgegeben an den Abfrage Ersteller."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht zur Bearbeitung zurückgegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_IN_BEARBEITUNG_SETZTEN.name())")
    public ResponseEntity<Void> inBearbeitungSetzenInfrastrukturabfrage(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.inBearbeitungSetzenAbfrage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/korrigieren")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf den Status IN_BEARBEITUNG_PLAN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich zurückgegeben an den Abfrage Ersteller."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht zur Bearbeitung zurückgegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_KORRIGIEREN.name())")
    public ResponseEntity<Void> korrigierenInfrastrukturabfrage(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.korrigierenAbfrage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/keine-bearbeitung-noetig")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf den Status ERLEDIGT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich erledigt."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht erledgit werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_KEINE_BEARBEITUNG_NOETIG_ABFRAGE.name())")
    public ResponseEntity<Void> keineBearbeitungNoetig(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.keineBearbeitungNoetig(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/verschicken-der-stellungnahme")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf den Status IN_BEARBEITUNG_FACHREFERATE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich an RBS oder SOZ zur Bearbeitung weitergegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht weitergegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_VERSCHICKEN_DER_STELLUNGNAHME_ABFRAGE.name())")
    public ResponseEntity<Void> verschickenDerStellungnahme(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.verschickenDerStellungnahme(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/bedarfsmeldung-erfolgt")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf den Status BEDARFSMELDUNG_ERFOLGT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Die Bedarfsmeldung der Fachreferate ist erfolgt"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Bedarfsmeldung konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht erfolgen", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_BEDARFSMELDUNG_ERFOLGT_ABFRAGE.name())")
    public ResponseEntity<Void> bedarfsmeldungErfolgt(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.bedarfsmeldungErfolgt(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/speicher-von-soz-infrastruktur-versorgung")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf den Status ERLEDIGT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich erledigt."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht erledigt werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG_ABFRAGE.name())")
    public ResponseEntity<Void> speichernVonSozialinfrastrukturVersorgung(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(id);
        return ResponseEntity.ok().build();
    }

}
