package de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.AbfragevarianteSachbearbeitungModel;
import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SachbearbeitungInfrastrukturabfrageInBearbeitungSachbearbeitungModel {

    private Long version;

    private List<AbfragevarianteSachbearbeitungModel> abfragevarianten;

    private List<SachbearbeitungAbfragevarianteInBearbeitungSachbearbeitungModel> abfragevariantenSachbearbeitung;
}
