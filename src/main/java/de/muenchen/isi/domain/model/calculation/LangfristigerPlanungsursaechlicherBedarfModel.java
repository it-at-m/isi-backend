package de.muenchen.isi.domain.model.calculation;

import java.util.List;
import lombok.Data;

@Data
public class LangfristigerPlanungsursaechlicherBedarfModel {

    private List<WohneinheitenProFoerderartProJahrModel> wohneinheiten;

    private List<InfrastrukturbedarfProJahrModel> bedarfKinderkrippe;

    private List<InfrastrukturbedarfProJahrModel> bedarfKindergarten;
}
