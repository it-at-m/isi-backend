package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WeiteresVerfahrenInBearbeitungSachbearbeitungModel extends AbfrageInBearbeitungSachbearbeitungModel {

    private VerortungMultiPolygonModel verortung;

    private List<
        AbfragevarianteWeiteresVerfahrenSachbearbeitungInBearbeitungSachbearbeitungModel
    > abfragevariantenWeiteresVerfahren;

    private List<
        AbfragevarianteWeiteresVerfahrenInBearbeitungSachbearbeitungModel
    > abfragevariantenSachbearbeitungWeiteresVerfahren;
}
