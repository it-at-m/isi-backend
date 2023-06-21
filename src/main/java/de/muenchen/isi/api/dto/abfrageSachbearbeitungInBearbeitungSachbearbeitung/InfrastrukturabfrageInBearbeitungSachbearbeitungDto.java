package de.muenchen.isi.api.dto.abfrageSachbearbeitungInBearbeitungSachbearbeitung;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class InfrastrukturabfrageInBearbeitungSachbearbeitungDto {

    private Long version;

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<@NotNull @Valid AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungDto> abfragevarianten;

    @NotNull
    @Size(min = 0, max = 5)
    private List<@NotNull @Valid AbfragevarianteInBearbeitungSachbearbeitungDto> abfragevariantenSachbearbeitung;
}
