package de.muenchen.isi.api.dto.sachbearbeitung;

import de.muenchen.isi.api.dto.AbfrageResponseDto;
import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.validation.GeschossflaecheWohnenSobonUrsaechlichValid;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.api.validation.UniqueRelevantAbfragevarianteValid;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@GeschossflaecheWohnenSobonUrsaechlichValid
public class InfrastrukturabfrageSachbearbeitungDto extends BaseEntityDto {

    @Valid
    @NotNull
    private AbfrageResponseDto abfrage;

    @NotNull
    @NotUnspecified
    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @NotEmpty
    @Size(min = 1, max = 5)
    @UniqueRelevantAbfragevarianteValid
    private List<@Valid @NotNull AbfragevarianteSachbearbeitungDto> abfragevarianten;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String aktenzeichenProLbk;

    @NotNull
    @NotUnspecified
    private UncertainBoolean offiziellerVerfahrensschritt;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String displayName;
}
