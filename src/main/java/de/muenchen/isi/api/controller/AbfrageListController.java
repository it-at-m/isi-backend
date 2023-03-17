package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.list.AbfrageListElementsDto;
import de.muenchen.isi.api.mapper.AbfrageListElementApiMapper;
import de.muenchen.isi.domain.service.AbfrageListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Abfragelisten", description = "API to get Abfragelisten")
public class AbfrageListController {

    private final AbfrageListService abfrageListService;

    private final AbfrageListElementApiMapper abfrageListElementApiMapper;

    @GetMapping("abfragen")
    @Transactional(readOnly = true)
    @Operation(summary = "Lade alle Abfragen für die Listendarstellung")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_ABFRAGE.name())")
    public ResponseEntity<AbfrageListElementsDto> getAbfrageListElements() {
        final var dto = this.abfrageListElementApiMapper.model2Dto(this.abfrageListService.getAbfrageListElements());
        return ResponseEntity.ok(dto);
    }
}
