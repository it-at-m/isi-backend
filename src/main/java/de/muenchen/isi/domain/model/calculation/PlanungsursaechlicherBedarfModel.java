package de.muenchen.isi.domain.model.calculation;

import java.util.Set;
import lombok.Data;

@Data
public class PlanungsursaechlicherBedarfModel {

    Set<WohneinheitenBedarfModel> wohneinheitenBedarfe;
}
