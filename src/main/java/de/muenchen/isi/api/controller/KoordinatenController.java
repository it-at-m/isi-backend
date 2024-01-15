package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.common.UtmDto;
import de.muenchen.isi.api.dto.common.Wgs84Dto;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.domain.exception.KoordinatenException;
import de.muenchen.isi.domain.service.KoordinatenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Koordinaten", description = "API to interact with the Koordinaten")
@Validated
public class KoordinatenController {

    private final KoordinatenService koordinatenService;

    @PostMapping("wgs-to-utm")
    @Operation(summary = "Umrechnung Wgs84 zu UTM")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "OK -> Transformation der Koordinate von WGS84 zu UTM32 war erfolgreich."
            ),
            @ApiResponse(
                responseCode = "555",
                description = "CUSTOM INTERNAL SERVER ERROR -> Bei der Transformation der Koordinate ist ein Fehler aufgetreten.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_KOORDINATEN_TRANSFORM.name())")
    public ResponseEntity<UtmDto> wgs84toUtm32(@RequestBody @Valid @NotNull final Wgs84Dto wgs84Dto)
        throws KoordinatenException {
        final UtmDto utmDto = this.koordinatenService.wgs84ToUtm32(wgs84Dto);
        return ResponseEntity.ok(utmDto);
    }

    @PostMapping("utm-to-wgs")
    @Operation(summary = "Umrechnung UTM32 zu WGS84")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "OK -> Transformation der Koordinate von UTM32 zu WGS84 war erfolgreich."
            ),
            @ApiResponse(
                responseCode = "555",
                description = "CUSTOM INTERNAL SERVER ERROR -> Bei der Transformation der Koordinate ist ein Fehler aufgetreten.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_KOORDINATEN_TRANSFORM.name())")
    public ResponseEntity<Wgs84Dto> utm32ToWgs84(@RequestBody @Valid @NotNull final UtmDto utmDto)
        throws KoordinatenException {
        final Wgs84Dto wgs84Dto = this.koordinatenService.utm32ToWgs84(utmDto);
        return ResponseEntity.ok(wgs84Dto);
    }
}
