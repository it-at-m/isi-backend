package de.muenchen.isi.api.controller;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import de.muenchen.isi.api.dto.BauabschnittDto;
import de.muenchen.isi.api.dto.BaugebietDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import de.muenchen.isi.api.dto.calculation.WohneinheitenInformationDto;
import de.muenchen.isi.api.mapper.AbfragevarianteApiMapper;
import de.muenchen.isi.api.mapper.BauabschnittApiMapper;
import de.muenchen.isi.api.mapper.BaugebietApiMapper;
import de.muenchen.isi.api.mapper.WohneinheitenInformationApiMapper;
import de.muenchen.isi.domain.model.AbfragevarianteModel;
import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.domain.model.BaugebietModel;
import de.muenchen.isi.domain.model.calculation.WohneinheitenInformationModel;
import de.muenchen.isi.domain.service.calculation.WohneinheitenInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "WohneinheitenInformation", description = "API zum Berechnen der Informationen bezüglich Wohneinheiten")
public class WohneinheitenInformationController {

    private final WohneinheitenInformationApiMapper wohneinheitenInformationApiMapper;

    private final AbfragevarianteApiMapper abfragevarianteApiMapper;

    private final BauabschnittApiMapper bauabschnittApiMapper;

    private final BaugebietApiMapper baugebietApiMapper;

    private final WohneinheitenInformationService wohneinheitenInformationService;

    @PostMapping("calculate-wohneinheiten-information-for-abfragevariante")
    @Operation(
        summary = "Berechne Informationen zu Wohneinheiten für eine Abfragevariante",
        description = "Das Ergebnis summiert die Informationen der in der Abfragevariante beinhalteten Bauabschnitte"
    )
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_CALCULATE_WOHNEINHEITEN.name())"
    )
    public ResponseEntity<WohneinheitenInformationDto> calculateWohneinheitenInformation(
        @RequestBody @NotNull final AbfrageerstellungAbfragevarianteAngelegtDto abfragevariante
    ) {
        AbfragevarianteDto abfragevarianteDto = new AbfragevarianteDto();
        abfragevarianteDto = this.abfragevarianteApiMapper.request2Response(abfragevariante, abfragevarianteDto);
        final AbfragevarianteModel abfragevarianteModel = this.abfragevarianteApiMapper.dto2Model(abfragevarianteDto);
        final WohneinheitenInformationModel wohneinheitenInformationModel =
            this.wohneinheitenInformationService.calculateWohneinheitenInformation(abfragevarianteModel);
        return ResponseEntity.ok(this.wohneinheitenInformationApiMapper.model2Dto(wohneinheitenInformationModel));
    }

    @PostMapping("calculate-wohneinheiten-information-for-bauabschnitt")
    @Operation(
        summary = "Berechne Informationen zu Wohneinheiten für einen Bauabschnitt",
        description = "Das Ergebnis summiert die Informationen der im Bauabschnitt beinhalteten Baugebiete"
    )
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_CALCULATE_WOHNEINHEITEN.name())"
    )
    public ResponseEntity<WohneinheitenInformationDto> calculateWohneinheitenInformation(
        @RequestBody @NotNull final BauabschnittDto bauabschnitt
    ) {
        final BauabschnittModel bauabschnittModel = this.bauabschnittApiMapper.dto2Model(bauabschnitt);
        final WohneinheitenInformationModel wohneinheitenInformationModel =
            this.wohneinheitenInformationService.calculateWohneinheitenInformation(bauabschnittModel);
        return ResponseEntity.ok(this.wohneinheitenInformationApiMapper.model2Dto(wohneinheitenInformationModel));
    }

    @PostMapping("calculate-wohneinheiten-information-for-baugebiet")
    @Operation(
        summary = "Berechne Informationen zu Wohneinheiten für ein Baugebiet",
        description = "Das Ergebnis summiert die Informationen der im Baugebiet beinhalteten Bauraten"
    )
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @PreAuthorize(
        "hasAuthority(T(de.muenchen.isi.security.AuthoritiesEnum).ISI_BACKEND_CALCULATE_WOHNEINHEITEN.name())"
    )
    public ResponseEntity<WohneinheitenInformationDto> calculateWohneinheitenInformation(
        @RequestBody @NotNull final BaugebietDto baugebiet
    ) {
        final BaugebietModel baugebietModel = this.baugebietApiMapper.dto2Model(baugebiet);
        final WohneinheitenInformationModel wohneinheitenInformationModel =
            this.wohneinheitenInformationService.calculateWohneinheitenInformation(baugebietModel);
        return ResponseEntity.ok(this.wohneinheitenInformationApiMapper.model2Dto(wohneinheitenInformationModel));
    }
}
