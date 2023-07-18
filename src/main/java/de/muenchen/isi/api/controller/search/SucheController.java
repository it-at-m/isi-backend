package de.muenchen.isi.api.controller.search;

import de.muenchen.isi.domain.service.search.SucheService;
import de.muenchen.isi.domain.service.search.SuchwortService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
@Tag(name = "Suche", description = "API für die Suche")
@Validated
public class SucheController {

    private final SuchwortService suchwortService;

    private final SucheService sucheService;

    @GetMapping("/searchword-suggestion")
    @Transactional(readOnly = true)
    @Operation(summary = "Suche nach Suchwortvorschläge für ein einzelnen im Parameter gegebenes Wort.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_SEARCH.name())")
    public void searchForSearchwordSuggestion(
        @RequestParam(value = "single-word-query") @NotNull final String singleWordQuery
    ) {}

    @GetMapping("/entities")
    @Transactional(readOnly = true)
    @Operation(summary = "Suche nach Entitäten für die im Parameter gegebenen Suchanfrage.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_SEARCH.name())")
    public void searchForEntities(@RequestParam(value = "search-query") @NotNull final String searchQuery) {}
}
