package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.calculation.CalculationRequestDto;
import de.muenchen.isi.api.dto.calculation.LangfristigerPlanungsursaechlicherBedarfDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.LangfristigerPlanungsursaechlicherBedarfApiMapper;
import de.muenchen.isi.domain.exception.CalculationException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.service.calculation.CalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private final LangfristigerPlanungsursaechlicherBedarfApiMapper langfristigerPlanungsursaechlicherBedarfApiMapper;

    @GetMapping("/planungsursaechlich")
    @Transactional(rollbackFor = OptimisticLockingException.class)
    @Operation(summary = "Berechnung des langfristigen planungsursächlichen Bedarfes")
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
                description = "NOT_FOUND -> Abfrage oder Abfragevariante konnte nicht gefunden werden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_ABFRAGE.name())")
    public ResponseEntity<
        LangfristigerPlanungsursaechlicherBedarfDto
    > calculateLangfristigerPlanungsursaechlicherBedarf(
        @RequestBody @Valid @NotNull final CalculationRequestDto calculationRequestDto
    ) throws EntityNotFoundException, CalculationException {
        final var bedarfModel = calculationService.calculateLangfristigerPlanungsursaechlicherBedarf(
            calculationRequestDto.getAbfrageId(),
            calculationRequestDto.getAbfragevarianteId(),
            calculationRequestDto.getGueltigAb()
        );
        final var bedarfDto = langfristigerPlanungsursaechlicherBedarfApiMapper.model2Dto(bedarfModel);
        return new ResponseEntity<>(bedarfDto, HttpStatus.OK);
    }
}
