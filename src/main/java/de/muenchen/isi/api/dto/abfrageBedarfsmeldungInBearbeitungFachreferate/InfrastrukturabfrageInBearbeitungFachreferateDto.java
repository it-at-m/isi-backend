package de.muenchen.isi.api.dto.abfrageBedarfsmeldungInBearbeitungFachreferate;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class InfrastrukturabfrageInBearbeitungFachreferateDto {

    private UUID id;

    private Long version;

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<@NotNull @Valid AbfragevarianteBedarfsmeldungDto> abfragevarianten;

    @NotNull
    @Size(min = 0, max = 5)
    private List<@NotNull @Valid AbfragevarianteBedarfsmeldungDto> abfragevariantenSachbearbeitung;
}
