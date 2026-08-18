package de.muenchen.isi.domain.model.abfrageStartBearbeitung;

import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaugenehmigungsverfahrenStartBearbeitungModel extends AbfrageStartBearbeitungModel {

    private VerortungMultiPolygonModel verortung;

    private List<AbfragevarianteBaugenehmigungsverfahrenSachbearbeitungStartBearbeitungModel> abfragevariantenBaugenehmigungsverfahren;

    private List<AbfragevarianteBaugenehmigungsverfahrenStartBearbeitungModel> abfragevariantenSachbearbeitungBaugenehmigungsverfahren;
}
