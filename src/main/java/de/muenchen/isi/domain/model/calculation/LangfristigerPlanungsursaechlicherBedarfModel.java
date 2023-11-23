package de.muenchen.isi.domain.model.calculation;

import java.util.List;
import lombok.Data;

@Data
public class LangfristigerPlanungsursaechlicherBedarfModel {

    private List<PlanungsursachlicheWohneinheitenModel> wohneinheiten;

    private List<PlanungsursaechlicherBedarfModel> planungsursaechlicherBedarfKinderkrippe;

    private List<PlanungsursaechlicherBedarfModel> planungsursaechlicherBedarfKindergarten;

    private List<PlanungsursaechlicherBedarfModel> planungsursaechlicherBedarfAlleEinwohner;
}
