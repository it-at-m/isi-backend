package de.muenchen.isi.api.dto.abfrageBedarfsmeldungInBearbeitungFachreferate;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class InfrastrukturabfrageInBearbeitungFachreferateDto {

    private Long version;

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<@NotNull @Valid AbfragevarianteInBearbeitungFachreferateDto> abfragevarianten;

    @NotNull
    @Size(min = 0, max = 5)
    private List<@NotNull @Valid AbfragevarianteInBearbeitungFachreferateDto> abfragevariantenSachbearbeitung;
}
