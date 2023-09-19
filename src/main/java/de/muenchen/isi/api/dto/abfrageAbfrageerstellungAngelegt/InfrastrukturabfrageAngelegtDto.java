package de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt;

import de.muenchen.isi.api.validation.GeschossflaecheWohnenSobonUrsaechlichValid;
import de.muenchen.isi.api.validation.NotUnspecified;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@GeschossflaecheWohnenSobonUrsaechlichValid
public class InfrastrukturabfrageAngelegtDto {

    private Long version;

    @Valid
    @NotNull
    private AbfrageAngelegtDto abfrage;

    @NotNull
    @NotUnspecified
    private UncertainBoolean sobonRelevant;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<@Valid @NotNull AbfragevarianteAngelegtDto> abfragevarianten;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String aktenzeichenProLbk;

    @NotNull
    @NotUnspecified
    private UncertainBoolean offiziellerVerfahrensschritt;

    @Size(max = 255, message = "Es sind maximal {max} Zeichen erlaubt")
    private String displayName;
}
