/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.stammdaten.LookupListsDto;
import de.muenchen.isi.api.mapper.LookupApiMapper;
import de.muenchen.isi.domain.service.LookupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Lookup", description = "API zum Erhalt der LookupListen.")
public class LookupController {

    private final LookupApiMapper lookupApiMapper;

    private final LookupService lookupService;

    @GetMapping("lookup-lists")
    @Operation(summary = "Gibt die Lookuplisten zurück.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "OK -> Lookuplisten wurden erfolgreich zurückgegeben."),
        }
    )
    @PreAuthorize("hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_LOOKUP_LIST.name())")
    public ResponseEntity<LookupListsDto> getLookupLists() {
        final LookupListsDto dto = this.lookupApiMapper.model2Dto(this.lookupService.getLookupLists());
        return ResponseEntity.ok(dto);
    }
}
