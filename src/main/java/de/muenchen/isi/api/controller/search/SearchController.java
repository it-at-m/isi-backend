package de.muenchen.isi.api.controller.search;

import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.api.dto.search.request.SearchQueryAndSortingDto;
import de.muenchen.isi.api.dto.search.request.SearchQueryDto;
import de.muenchen.isi.api.dto.search.response.SearchResultsDto;
import de.muenchen.isi.api.dto.search.response.SuchwortSuggestionsDto;
import de.muenchen.isi.api.mapper.SearchApiMapper;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.service.search.EntitySearchService;
import de.muenchen.isi.domain.service.search.SearchWordSuggesterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
@Tag(name = "Suche", description = "API für die Suche")
@Validated
public class SearchController {

    private final SearchApiMapper searchApiMapper;

    private final SearchWordSuggesterService searchService;

    private final EntitySearchService entitySearchService;

    @PostMapping("/searchword-suggestion")
    @Operation(summary = "Suche nach Suchwortvorschläge für das im Request-Body gegebene Suchanfrage.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "OK -> Es wurde eine erfolgreiche Suche nach Suchwortvorschlägen durchgeführt."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Im Requestbody wurde kein zu durchsuchender Entitätstyp gewählt.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    public ResponseEntity<SuchwortSuggestionsDto> searchForSearchwordSuggestion(
        @RequestBody @NotNull @Valid final SearchQueryDto searchQueryInformation
    ) throws EntityNotFoundException {
        final var requestModel = searchApiMapper.dto2Model(searchQueryInformation);
        final var model = searchService.searchForSearchwordSuggestion(requestModel);
        final var dto = searchApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/entities")
    @Transactional(readOnly = true)
    @Operation(summary = "Suche nach Entitäten für die im Request-Body gegebene Suchanfrage.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "OK -> Es wurde eine erfolgreiche Enititätssuche durchgeführt."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND -> Im Requestbody wurde kein zu durchsuchender Entitätstyp gewählt.",
                content = @Content(schema = @Schema(implementation = InformationResponseDto.class))
            ),
        }
    )
    public ResponseEntity<SearchResultsDto> searchForEntities(
        @RequestBody @NotNull @Valid final SearchQueryAndSortingDto searchQueryAndSortingInformation
    ) throws EntityNotFoundException {
        final var requestModel = searchApiMapper.dto2Model(searchQueryAndSortingInformation);
        final var responseModel = entitySearchService.searchForEntities(requestModel);
        final var dto = searchApiMapper.model2Dto(responseModel);
        return ResponseEntity.ok(dto);
    }
}
