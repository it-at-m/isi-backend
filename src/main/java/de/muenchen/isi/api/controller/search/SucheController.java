package de.muenchen.isi.api.controller.search;

import de.muenchen.isi.api.dto.search.SearchQueryForEntitiesDto;
import de.muenchen.isi.api.dto.search.SearchResultsDto;
import de.muenchen.isi.api.dto.search.SuchwortSuggestionsDto;
import de.muenchen.isi.api.mapper.SearchApiMapper;
import de.muenchen.isi.domain.service.search.SucheService;
import de.muenchen.isi.domain.service.search.SuchwortService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
@Tag(name = "Suche", description = "API für die Suche")
@Validated
public class SucheController {

    private final SearchApiMapper searchApiMapper;

    private final SuchwortService suchwortService;

    private final SucheService sucheService;

    @GetMapping("/searchword-suggestion")
    @Transactional(readOnly = true)
    @Operation(summary = "Suche nach Suchwortvorschläge für ein einzelnes im Parameter gegebenes Wort.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_SEARCH.name())")
    public ResponseEntity<SuchwortSuggestionsDto> searchForSearchwordSuggestion(
        @RequestParam(value = "single-word-query") @NotEmpty final String singleWordQuery
    ) {
        final var model = suchwortService.searchForSearchwordSuggestion(singleWordQuery);
        final var dto = searchApiMapper.model2Dto(model);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/entities")
    @Transactional(readOnly = true)
    @Operation(summary = "Suche nach Entitäten für die im Request-Body gegebene Suchanfrage.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_SEARCH.name())")
    public ResponseEntity<SearchResultsDto> searchForEntities(
        @RequestBody @NotNull @Valid final SearchQueryForEntitiesDto searchQueryInformation
    ) {
        final var requestModel = searchApiMapper.dto2Model(searchQueryInformation);
        final var responseModel = sucheService.searchForEntities(requestModel);
        final var dto = searchApiMapper.model2Dto(responseModel);
        return ResponseEntity.ok(dto);
    }
}
