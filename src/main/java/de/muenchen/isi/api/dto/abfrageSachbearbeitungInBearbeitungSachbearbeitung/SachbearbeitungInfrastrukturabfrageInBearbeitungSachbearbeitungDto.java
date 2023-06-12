package de.muenchen.isi.api.dto.abfrageSachbearbeitungInBearbeitungSachbearbeitung;

import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SachbearbeitungInfrastrukturabfrageInBearbeitungSachbearbeitungDto {

    private Long version;

    private List<SachbearbeitungAbfragevarianteInBearbeitungSachbearbeitungDto> abfragevariantenSachbearbeitung;
}
