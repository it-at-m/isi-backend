package de.muenchen.isi.domain.model.abfrageBedarfsmeldungInBearbeitungFachreferate;

import java.util.List;
import lombok.Data;

@Data
public class InfrastrukturabfrageInBearbeitungFachreferateModel {

    private Long version;

    private List<AbfragevarianteInBearbeitungFachreferateModel> abfragevarianten;

    private List<AbfragevarianteInBearbeitungFachreferateModel> abfragevariantenSachbearbeitung;
}
