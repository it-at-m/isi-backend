/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.BaurateDto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.mapper.BaurateApiMapper;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.service.BaurateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/baurate")
@Tag(name = "Baurate", description = "API zum interagieren mit Bauraten")
@Validated
public class BaurateController {

    private final BaurateService baurateService;

    private final BaurateApiMapper baurateApiMapper;

    @GetMapping("/determine")
    @Transactional(readOnly = true)
    @Operation(summary = "Ermittelt die Bauraten auf Basis der Stammdaten für idealtypische Bauraten")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Es konnten keine Bauraten ermittelt werden, da keine idealtypischen Bauraten für die gegebenen Parameter existieren.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_DETERMINE_BAURATEN.name())")
    public ResponseEntity<List<BaurateDto>> determineBauraten(
        @RequestParam @NotNull @Min(0) final Integer realisierungsbeginn,
        @RequestParam(required = false) @Min(0) final Long wohneinheiten,
        @RequestParam(required = false) @Min(0) final BigDecimal geschossflaecheWohnen
    ) throws EntityNotFoundException {
        final List<BaurateDto> baurateDtoList =
            this.baurateService.determineBauraten(realisierungsbeginn, wohneinheiten, geschossflaecheWohnen)
                .stream()
                .map(this.baurateApiMapper::model2Dto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(baurateDtoList);
    }
}
