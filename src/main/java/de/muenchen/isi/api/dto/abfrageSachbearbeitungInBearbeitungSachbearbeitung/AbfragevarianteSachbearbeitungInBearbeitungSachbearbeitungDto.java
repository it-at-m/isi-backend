package de.muenchen.isi.api.dto.abfrageSachbearbeitungInBearbeitungSachbearbeitung;

import de.muenchen.isi.api.dto.AbfragevarianteSachbearbeitungDto;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungDto {

    private UUID id;

    private Long version;

    @NotNull
    @Valid
    private AbfragevarianteSachbearbeitungDto abfragevarianteSachbearbeitung;
}
