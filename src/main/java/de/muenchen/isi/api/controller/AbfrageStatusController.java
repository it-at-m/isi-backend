package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Tag(name = "AbfrageStatus", description = "API to set the status for a Abfrage")
@Validated
public class AbfrageStatusController {

    private final AbfrageStatusService abfrageStatusService;

    @PutMapping("infrastruktur-abfrage/{id}/freigabe")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf OFFEN genaue Statusänderungdefinition in {@link  StateMachineConfiguration}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_FREIGABE_ABFRAGE.name())")
    public ResponseEntity<Void> freigabeInfrastrukturabfrage(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.freigabeAbfrage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("infrastruktur-abfrage/{id}/abbrechen")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf ABBRUCH genaue Statusänderungdefinition in {@link  StateMachineConfiguration}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_ABBRECHEN_ABFRAGE.name())")
    public ResponseEntity<Void> abbrechenInfrastrukturabfrage(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.abbrechenAbfrage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("infrastruktur-abfrage/{id}/angabe-anpassen")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf ANGELEGT genaue Statusänderungdefinition in {@link  StateMachineConfiguration}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_ANGABEN_ANPASSEN_ABFRAGE.name())")
    public ResponseEntity<Void> angabenAnpassenInfrastrukturabfrage(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.angabenAnpassenAbfrage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("infrastruktur-abfrage/{id}/weitere-abfragevarianten-anlegen")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf IN_ERFASSUNG genaue Statusänderungdefinition in {@link  StateMachineConfiguration}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_WEITERE_ABFRAGEVARIANTEN_ANLEGEN_ABFRAGE.name())")
    public ResponseEntity<Void> weitereAbfragevariantenAnlegen(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.weitereAbfragevariantenAnlegen(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("infrastruktur-abfrage/{id}/keine-zusaetzliche-abfragevariante")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf IN_BEARBEITUNG_PLAN genaue Statusänderungdefinition in {@link  StateMachineConfiguration}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_KEINE_ZUSAETZLICHE_ABFRAGEVARIANTE_ABFRAGE.name())")
    public ResponseEntity<Void> keineZusaetzlicheAbfragevariante(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.keineZusaetzlicheAbfragevariante(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("infrastruktur-abfrage/{id}/zusaetzliche-abfragevariante-anlegen")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf IN_ERFASSUNG genaue Statusänderungdefinition in {@link  StateMachineConfiguration}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_ZUSAETZLICHE_ABFRAGEVARIANTE_ANLEGEN_ABFRAGE.name())")
    public ResponseEntity<Void> zusaetzlicheAbfragevarianteAnlegen(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.zusaetzlicheAbfragevarianteAnlegen(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("infrastruktur-abfrage/{id}/speicher-der-varianten")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf IN_BEARBEITUBG_PLAN genaue Statusänderungdefinition in {@link  StateMachineConfiguration}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_SPEICHER_DER_VARIANTEN_ABFRAGE.name())")
    public ResponseEntity<Void> speicherDerVarianten(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.speichernDerVarianten(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("infrastruktur-abfrage/{id}/keine-bearbeitung-noetig")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf ERLEDIGT genaue Statusänderungdefinition in {@link  StateMachineConfiguration}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_KEINE_BEARBEITUNG_NOETIG_ABFRAGE.name())")
    public ResponseEntity<Void> keineBearbeitungNoetig(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.keineBearbeitungNoetig(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("infrastruktur-abfrage/{id}/verschicken-der-stellungnahme")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf IN_BEARBEITUNG_FACHREFERATE genaue Statusänderungdefinition in {@link  StateMachineConfiguration}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_VERSCHICKEN_DER_STELLUNGNAHME_ABFRAGE.name())")
    public ResponseEntity<Void> verschickenDerStellungnahme(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.verschickenDerStellungnahme(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("infrastruktur-abfrage/{id}/bedarfsmeldung-erfolgt")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf BEDARFSMELDUNG_ERFOLGT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_BEDARFSMELDUNG_ERFOLGT_ABFRAGE.name())")
    public ResponseEntity<Void> bedarfsmeldungErfolgt(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.bedarfsmeldungErfolgt(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("infrastruktur-abfrage/{id}/speicher-von-soz-infrastruktur-versorgung")
    @Transactional
    @Operation(summary = "Setzt eine Infrastrukturabfrage auf ERLEDIGT genaue Statusänderungdefinition in {@link  StateMachineConfiguration}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_SPEICHERN_VON_SOZIALINFRASTRUKTUR_VERSORGUNG_ABFRAGE.name())")
    public ResponseEntity<Void> speichernVonSozialinfrastrukturVersorgung(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException {
        this.abfrageStatusService.speichernVonSozialinfrastrukturVersorgung(id);
        return ResponseEntity.ok().build();
    }
}
