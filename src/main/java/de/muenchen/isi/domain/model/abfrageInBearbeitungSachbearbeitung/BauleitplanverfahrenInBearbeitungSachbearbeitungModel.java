package de.muenchen.isi.domain.model.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.common.VerortungModel;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauleitplanverfahrenInBearbeitungSachbearbeitungModel extends AbfrageInBearbeitungSachbearbeitungModel {

    private List<AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungModel> abfragevarianten;

    private List<AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungModel> abfragevariantenSachbearbeitung;

    private VerortungModel verortung;
}
