package de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaugenehmigungsverfahrenInBearbeitungFachreferatModel extends AbfrageInBearbeitungFachreferatModel {

    private List<AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel> abfragevarianten;

    private List<AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatModel> abfragevariantenSachbearbeitung;
}
