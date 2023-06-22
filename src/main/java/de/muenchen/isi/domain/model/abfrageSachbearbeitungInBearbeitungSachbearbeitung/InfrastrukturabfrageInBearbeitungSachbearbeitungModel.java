package de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung;

import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class InfrastrukturabfrageInBearbeitungSachbearbeitungModel {

    private Long version;

    private List<AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungModel> abfragevarianten;

    private List<AbfragevarianteInBearbeitungSachbearbeitungModel> abfragevariantenSachbearbeitung;
}
