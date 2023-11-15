package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.calculation.CalculationRequestDto;
import de.muenchen.isi.api.dto.calculation.PlanungsursaechlicherBedarfDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.PlanungsursaechlicherBedarfApiMapper;
import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.model.AbfrageModel;
import de.muenchen.isi.domain.model.AbfragevarianteBauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugenehmigungsverfahrenModel;
import de.muenchen.isi.domain.model.BauleitplanverfahrenModel;
import de.muenchen.isi.domain.model.calculation.PlanungsursaechlicherBedarfModel;
import de.muenchen.isi.domain.service.AbfrageService;
import de.muenchen.isi.domain.service.CalculationService;
import de.muenchen.isi.infrastructure.entity.Bauabschnitt;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/calculation")
@Tag(name = "Berechnungen", description = "API für Berechnungen")
@Validated
public class CalculationController {

    private final CalculationService calculationService;

    private final AbfrageService abfrageService;

    private final PlanungsursaechlicherBedarfApiMapper planungsursaechlicherBedarfApiMapper;

    @GetMapping("/planungsursaechlich")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Berechnung der planungsursächlichen Bedarfe")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Berechnung konnte durchgeführt werden."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Berechnung konnte wegen fehlerhafter Daten nicht durchgeführt werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Abfrage oder Abfragevariante mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_ABFRAGE.name())")
    public ResponseEntity<PlanungsursaechlicherBedarfDto> calculatePlanungsursaechlicherBedarf(
        @RequestBody @Valid @NotNull final CalculationRequestDto calculationRequest
    ) throws CalculationException, EntityNotFoundException {
        final var abfrage = abfrageService.getById(calculationRequest.getAbfrageId());
        List<BauabschnittModel> bauabschnitte = null;
        SobonOrientierungswertJahr sobonJahr = null;

        switch (abfrage.getArtAbfrage()) {
            case BAULEITPLANVERFAHREN:
                final var bauleitplanverfahren = (BauleitplanverfahrenModel) abfrage;
                final var abfragevarianteBauleitplanverfahren = Stream
                    .concat(
                        bauleitplanverfahren.getAbfragevariantenBauleitplanverfahren().stream(),
                        bauleitplanverfahren.getAbfragevariantenSachbearbeitungBauleitplanverfahren().stream()
                    )
                    .filter(abfragevariante -> abfragevariante.getId().equals(calculationRequest.getAbfragevarianteId())
                    )
                    .findAny()
                    .orElseThrow(() -> new EntityNotFoundException("Abfragevariante nicht gefunden."));
                bauabschnitte = abfragevarianteBauleitplanverfahren.getBauabschnitte();
                sobonJahr = abfragevarianteBauleitplanverfahren.getSobonOrientierungswertJahr();
                break;
            case BAUGENEHMIGUNGSVERFAHREN:
                final var baugenehmigungsverfahren = (BaugenehmigungsverfahrenModel) abfrage;
                final var abfragevarianteBaugenehmigungsverfahren = Stream
                    .concat(
                        baugenehmigungsverfahren.getAbfragevariantenBaugenehmigungsverfahren().stream(),
                        baugenehmigungsverfahren.getAbfragevariantenSachbearbeitungBaugenehmigungsverfahren().stream()
                    )
                    .filter(abfragevariante -> abfragevariante.getId().equals(calculationRequest.getAbfragevarianteId())
                    )
                    .findAny()
                    .orElseThrow(() -> new EntityNotFoundException("Abfragevariante nicht gefunden."));
                bauabschnitte = abfragevarianteBaugenehmigungsverfahren.getBauabschnitte();
                sobonJahr = abfragevarianteBaugenehmigungsverfahren.getSobonOrientierungswertJahr();
                break;
            default:
                break;
        }

        if (bauabschnitte == null || sobonJahr == null) {
            throw new CalculationException("Die Berechnung kann für diese Art von Abfrage nicht durchgeführt werden.");
        }

        final var bedarf = calculationService.calculatePlanungsursaechlicherBedarf(
            bauabschnitte,
            sobonJahr,
            calculationRequest.getGueltigAb()
        );
        return new ResponseEntity<>(planungsursaechlicherBedarfApiMapper.model2Dto(bedarf), HttpStatus.OK);
    }
}
