package de.muenchen.isi.api.controller.startseite;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.dto.startseite.StartseitenEinstellungDto;
import de.muenchen.isi.api.mapper.StartseitenEinstellungApiMapper;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UserRoleNotAllowedException;
import de.muenchen.isi.domain.service.startseite.StartseitenEinstellungService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/startseiten-einstellung")
@Tag(name = "StartseitenEinstellung", description = "API zur Interaktion mit den persönlichen Startseiteneinstellungen")
@Validated
public class StartseitenEinstellungController {

    private final StartseitenEinstellungService startseitenEinstellungService;

    private final StartseitenEinstellungApiMapper startseitenEinstellungApiMapper;

    @GetMapping
    @Operation(
        summary = "Lesen der persönlichen Startseiteneinstellungen. Sind keine gespeichert, werden die Standardeinstellungen zurückgegeben."
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Keine Berechtigung, um die Startseiteneinstellungen anzusehen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    public StartseitenEinstellungDto getStartseitenEinstellung() throws UserRoleNotAllowedException {
        final var model = startseitenEinstellungService.getStartseitenEinstellung();
        return startseitenEinstellungApiMapper.model2Dto(model);
    }

    @PutMapping
    @Operation(summary = "Speichern der persönlichen Startseiteneinstellungen.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Einstellungen wurden erfolgreich gespeichert."),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Einstellungen konnten nicht gespeichert werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Keine Berechtigung, um die Startseiteneinstellungen zu speichern.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "412",
                description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @Transactional(rollbackFor = OptimisticLockingException.class)
    public StartseitenEinstellungDto saveStartseitenEinstellung(
        @RequestBody @Valid @NotNull StartseitenEinstellungDto startseitenEinstellungDto
    ) throws UserRoleNotAllowedException, OptimisticLockingException {
        final var requestModel = startseitenEinstellungApiMapper.dto2Model(startseitenEinstellungDto);
        final var responseModel = startseitenEinstellungService.save(requestModel);
        return startseitenEinstellungApiMapper.model2Dto(responseModel);
    }
}
