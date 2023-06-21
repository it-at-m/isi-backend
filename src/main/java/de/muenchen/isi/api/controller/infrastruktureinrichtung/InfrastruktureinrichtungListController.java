package de.muenchen.isi.api.controller.infrastruktureinrichtung;

import de.muenchen.isi.api.dto.list.InfrastruktureinrichtungListElementsDto;
import de.muenchen.isi.api.mapper.InfrastruktureinrichtungApiMapper;
import de.muenchen.isi.domain.service.infrastruktureinrichtung.InfrastruktureinrichtungListService;
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
@Tag(name = "Infrastruktureinrichtunglisten", description = "API to get Infrastruktureinrichtunglisten")
public class InfrastruktureinrichtungListController {

    private final InfrastruktureinrichtungListService infrastruktureinrichtungListService;

    private final InfrastruktureinrichtungApiMapper infrastruktureinrichtungApiMapper;

    @GetMapping("infrastruktureinrichtungen")
    @Transactional(readOnly = true)
    @Operation(summary = "Lade alle Infrastruktureinrichtungen für die Listendarstellung")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_KINDERKRIPPE.name())" +
        " && hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_KINDERGARTEN.name())" +
        " && hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_HAUS_FUER_KINDER.name())" +
        " && hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_GS_NACHMITTAG_BETREUUNG.name())" +
        " && hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_GRUNDSCHULE.name())" +
        " && hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_MITTELSCHULE.name())"
    )
    public ResponseEntity<InfrastruktureinrichtungListElementsDto> getInfrastruktureinrichtungListElements() {
        final var dto =
            this.infrastruktureinrichtungApiMapper.model2Dto(
                    this.infrastruktureinrichtungListService.getInfrastruktureinrichtungListElements()
                );
        return ResponseEntity.ok(dto);
    }
}
