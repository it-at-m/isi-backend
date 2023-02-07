package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
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
    @Operation(summary = "Gibt eine Infrastrukturabfrage frei")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK -> Abfrage wurde erfolgreich freigegeben."),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND -> Es gibt keine Abfrage mit der ID.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "CONFLICT -> Die Abfrage konnte aufgrund des aktuellen Status oder des bereits existierenden Abfragenamen nicht freigegeben werden.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class))),
            @ApiResponse(responseCode = "412", description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.", content = @Content(schema = @Schema(implementation = InformationResponseDto.class)))
    })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_FREIGABE_ABFRAGE.name())")
    public ResponseEntity<Void> freigabeInfrastrukturabfrage(@PathVariable @NotNull final UUID id) throws EntityNotFoundException, AbfrageStatusNotAllowedException, UniqueViolationException, OptimisticLockingException {
        this.abfrageStatusService.freigabeInfrastrukturabfrage(id);
        return ResponseEntity.ok().build();
    }

}
