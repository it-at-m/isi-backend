package de.muenchen.isi.api.dto.abfrageSachbearbeitungInBearbeitungSachbearbeitung;

import de.muenchen.isi.api.dto.AbfragevarianteSachbearbeitungDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfrageerstellungAbfragevarianteAngelegtDto;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SachbearbeitungAbfragevarianteInBearbeitungSachbearbeitungDto {

    private Long version;

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<@Valid @NotNull AbfragevarianteSachbearbeitungDto> abfragevarianten;
}
