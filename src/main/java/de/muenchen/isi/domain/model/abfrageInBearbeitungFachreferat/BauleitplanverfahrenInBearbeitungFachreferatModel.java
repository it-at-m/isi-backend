package de.muenchen.isi.domain.model.abfrageInBearbeitungFachreferat;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenInBearbeitungFachreferatModel extends AbfrageInBearbeitungFachreferatModel {

    private List<AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel> abfragevarianten;

    private List<AbfragevarianteBauleitplanverfahrenInBearbeitungFachreferatModel> abfragevariantenSachbearbeitung;
}
