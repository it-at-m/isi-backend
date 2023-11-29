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

    private List<InfrastrukturbedarfProJahrModel> bedarfKinderkrippe;

    private InfrastrukturbedarfProJahrModel bedarfKinderkrippeMittelwert10;

    private InfrastrukturbedarfProJahrModel bedarfKinderkrippeMittelwert15;

    private InfrastrukturbedarfProJahrModel bedarfKinderkrippeMittelwert20;

    private List<InfrastrukturbedarfProJahrModel> bedarfKindergarten;

    private InfrastrukturbedarfProJahrModel bedarfKindergartenMittelwert10;

    private InfrastrukturbedarfProJahrModel bedarfKindergartenMittelwert15;

    private InfrastrukturbedarfProJahrModel bedarfKindergartenMittelwert20;

    private List<PersonenProJahrModel> alleEinwohner;

    private PersonenProJahrModel alleEinwohnerMittelwert10;

    private PersonenProJahrModel alleEinwohnerMittelwert15;

    private PersonenProJahrModel alleEinwohnerMittelwert20;
}
