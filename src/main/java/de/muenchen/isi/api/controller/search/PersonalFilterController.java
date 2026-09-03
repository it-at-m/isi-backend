package de.muenchen.isi.api.controller.search;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.dto.search.filter.PersonalFilterRequestDto;
import de.muenchen.isi.api.dto.search.filter.PersonalFilterResponseDto;
import de.muenchen.isi.api.mapper.PersonalFilterApiMapper;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.service.search.PersonalFilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/personal-filter")
@Tag(name = "PersonalFilter", description = "API zum interagieren mit persönlichen Filtern")
@Validated
public class PersonalFilterController {

    private final PersonalFilterService personalFilterService;

    private final PersonalFilterApiMapper personalFilterApiMapper;

    @GetMapping
    @Operation(summary = "Lesen aller persönlichen Filter.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Keine Berechtigung um alle persönlichen Filter anzusehen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    public List<PersonalFilterResponseDto> getPersonalFilters() throws IllegalAccessException {
        var modles = personalFilterService.getPersonalFilters();
        return personalFilterApiMapper.models2Dtos(modles);
    }

    @GetMapping("/{filterId}")
    @Operation(summary = "Lesen eines persönlichen Filters.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Keine Berechtigung um diesen persönlichen Filter anzusehen.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Persönlicher Filter mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    public PersonalFilterResponseDto getByFilterID(@PathVariable @NotNull UUID filterId)
        throws EntityNotFoundException, IllegalAccessException {
        var responseModel = personalFilterService.getByFilterID(filterId);
        return personalFilterApiMapper.model2Dto(responseModel);
    }

    @Operation(summary = "Aktualisierung eines persönlichen Filters.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "OK -> Persönlicher Filter wurde erfolgreich aktualisiert."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Persönlicher Filter konnte nicht aktualisiert werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN -> Keine Berechtigung um diesen persönlicher Filter zu bearbeiten.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Persönlicher Filter mit dieser ID nicht vorhanden.",
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
    @PatchMapping("/edit")
    public PersonalFilterResponseDto editFilter(
        @RequestBody @Valid @NotNull PersonalFilterRequestDto personalFilterRequestDto
    ) throws OptimisticLockingException, EntityNotFoundException, IllegalAccessException {
        var requestModel = personalFilterApiMapper.dto2Model(personalFilterRequestDto);
        var responseModel = personalFilterService.update(requestModel);
        return personalFilterApiMapper.model2Dto(responseModel);
    }

    @Transactional(rollbackFor = OptimisticLockingException.class)
    @PostMapping("/create")
    @Operation(summary = "Anlegen eines neuen persönlichen Filters")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "CREATED -> Persönlicher Filter wurde erfolgreich erstellt."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "BAD_REQUEST -> Persönlicher Filter konnte nicht erstellt werden, überprüfen sie die Eingabe.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "412",
                description = "PRECONDITION_FAILED -> In der Anwendung ist bereits eine neuere Version der Entität gespeichert.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    public ResponseEntity<PersonalFilterResponseDto> createFilter(
        @RequestBody @Valid @NotNull PersonalFilterRequestDto personalFilterRequestDto
    ) throws OptimisticLockingException, IllegalAccessException {
        var requestModel = personalFilterApiMapper.dto2Model(personalFilterRequestDto);
        var responseModel = personalFilterService.save(requestModel);
        var dto = personalFilterApiMapper.model2Dto(responseModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Löschen eines persönlichen Filters")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
            @ApiResponse(
                responseCode = "404",
                description = "NOT FOUND -> Persönlicher Filter mit dieser ID nicht vorhanden.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "CONFLICT -> Keine Berechtigung zum Löschen dieses persönlichen Filters.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    @DeleteMapping("/delete/{filterId}")
    public void deleteFilter(@PathVariable UUID filterId) throws EntityNotFoundException, IllegalAccessException {
        personalFilterService.delete(filterId);
    }
}
