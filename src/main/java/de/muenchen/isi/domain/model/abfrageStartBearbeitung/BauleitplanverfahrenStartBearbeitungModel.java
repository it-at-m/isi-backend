package de.muenchen.isi.domain.model.abfrageStartBearbeitung;

import de.muenchen.isi.domain.model.common.VerortungMultiPolygonModel;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenStartBearbeitungModel extends AbfrageStartBearbeitungModel {

    private VerortungMultiPolygonModel verortung;

    private List<
            AbfragevarianteBauleitplanverfahrenSachbearbeitungStartBearbeitungModel
    > abfragevariantenBauleitplanverfahren;

    private List<
            AbfragevarianteBauleitplanverfahrenStartBearbeitungModel
    > abfragevariantenSachbearbeitungBauleitplanverfahren;
}
