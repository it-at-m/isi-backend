package de.muenchen.isi.api.dto.calculation;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LangfristigerPlanungsursaechlicherBedarfDto {

    @NotNull
    private List<WohneinheitenProFoerderartProJahrDto> wohneinheiten;

    @NotNull
    private List<WohneinheitenProFoerderartProJahrDto> wohneinheitenSumme10Jahre;

    @NotNull
    private List<WohneinheitenProFoerderartProJahrDto> wohneinheitenSumme15Jahre;

    @NotNull
    private List<WohneinheitenProFoerderartProJahrDto> wohneinheitenSumme20Jahre;

    @NotNull
    private List<WohneinheitenProFoerderartProJahrDto> wohneinheitenGesamt;
}
