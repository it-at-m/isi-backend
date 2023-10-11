package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenInBearbeitungSachbearbeitungModel extends AbfrageInBearbeitungSachbearbeitungModel {

    private List<
        AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungAngelegtModel
    > abfragevarianten;

    private List<
        AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungAngelegtModel
    > abfragevariantenSachbearbeitung;
}
