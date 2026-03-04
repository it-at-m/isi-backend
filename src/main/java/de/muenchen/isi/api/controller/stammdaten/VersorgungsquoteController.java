package de.muenchen.isi.api.controller.stammdaten;

import de.muenchen.isi.api.dto.stammdaten.VersorgungsquoteSobonHortDto;
import de.muenchen.isi.api.mapper.StammdatenApiMapper;
import de.muenchen.isi.domain.service.stammdaten.VersorungsquoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Versorgungsquote", description = "API zum Interagieren mit Versorgungsquoten")
@Validated
public class VersorgungsquoteController {

    private final VersorungsquoteService versorungsquoteService;

    private final StammdatenApiMapper stammdatenApiMapper;

    @Transactional(readOnly = true)
    @GetMapping("stammdaten/versorungsquote")
    @Operation(summary = "Lade alle Versorungsquoten für den Hort")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_READ_STAMMDATEN_FOERDERMIX.name())"
    )
    public ResponseEntity<List<VersorgungsquoteSobonHortDto>> getVersorungsquoteSobonHort() {
        final List<VersorgungsquoteSobonHortDto> versorgungsquoteSobonHortDtoList =
            this.versorungsquoteService.getVersorgungsquoteHortSobon()
                .stream()
                .map(this.stammdatenApiMapper::model2Dto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(versorgungsquoteSobonHortDtoList, HttpStatus.OK);
    }
}
