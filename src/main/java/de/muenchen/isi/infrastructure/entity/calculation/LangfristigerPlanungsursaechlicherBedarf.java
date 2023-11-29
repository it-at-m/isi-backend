package de.muenchen.isi.infrastructure.entity.calculation;

import java.util.List;
import lombok.Data;

@Data
public class LangfristigerPlanungsursaechlicherBedarf {

    private List<WohneinheitenProFoerderartProJahr> wohneinheiten;

    private List<WohneinheitenProFoerderartProJahr> wohneinheitenSumme10Jahre;

    private List<WohneinheitenProFoerderartProJahr> wohneinheitenSumme15Jahre;

    private List<WohneinheitenProFoerderartProJahr> wohneinheitenSumme20Jahre;

    private List<WohneinheitenProFoerderartProJahr> wohneinheitenGesamt;
}
