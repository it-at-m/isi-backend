package de.muenchen.isi.domain.model.calculation;

import java.util.List;
import lombok.Data;

@Data
public class LangfristigerPlanungsursaechlicherBedarfModel {

    List<WohneinheitenBedarfModel> wohneinheitenBedarfe;
}
