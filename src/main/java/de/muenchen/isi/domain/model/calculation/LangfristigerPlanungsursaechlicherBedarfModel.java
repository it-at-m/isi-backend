package de.muenchen.isi.domain.model.calculation;

import java.util.List;
import lombok.Data;

@Data
public class LangfristigerPlanungsursaechlicherBedarfModel {

    private List<WohneinheitenProFoerderartProJahrModel> wohneinheiten;

    private List<WohneinheitenProFoerderartProJahrModel> wohneinheitenSumme10Jahre;

    private List<WohneinheitenProFoerderartProJahrModel> wohneinheitenSumme15Jahre;

    private List<WohneinheitenProFoerderartProJahrModel> wohneinheitenSumme20Jahre;

    private List<WohneinheitenProFoerderartProJahrModel> wohneinheitenGesamt;
}
