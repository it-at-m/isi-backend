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

    private List<InfrastrukturbedarfProJahr> bedarfKinderkrippe;

    private InfrastrukturbedarfProJahr bedarfKinderkrippeMittelwert10;

    private InfrastrukturbedarfProJahr bedarfKinderkrippeMittelwert15;

    private InfrastrukturbedarfProJahr bedarfKinderkrippeMittelwert20;

    private List<InfrastrukturbedarfProJahr> bedarfKindergarten;

    private InfrastrukturbedarfProJahr bedarfKindergartenMittelwert10;

    private InfrastrukturbedarfProJahr bedarfKindergartenMittelwert15;

    private InfrastrukturbedarfProJahr bedarfKindergartenMittelwert20;

    private List<PersonenProJahr> alleEinwohner;

    private PersonenProJahr alleEinwohnerMittelwert10;

    private PersonenProJahr alleEinwohnerMittelwert15;

    private PersonenProJahr alleEinwohnerMittelwert20;
}
