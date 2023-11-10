package de.muenchen.isi.domain.model.calculation;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanungsursaechlicherBedarfModel {

    List<WohneinheitenProFoerderartModel> wohneinheitenProFoerderart;
}
