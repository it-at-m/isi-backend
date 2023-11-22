package de.muenchen.isi.domain.model.calculation;

import java.util.List;
import lombok.Data;

@Data
public class PlanungsursaechlicherBedarfModel {

    private List<WohneinheitenBedarfModel> wohneinheitenBedarfe;
}
