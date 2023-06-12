package de.muenchen.isi.domain.model.abfrageSachbearbeitungInBearbeitungSachbearbeitung;

import de.muenchen.isi.domain.model.AbfragevarianteSachbearbeitungModel;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AbfragevarianteSachbearbeitungInBearbeitungSachbearbeitungModel {

    private UUID id;

    private Long version;

    @NotNull
    @Valid
    private AbfragevarianteSachbearbeitungModel abfragevarianteSachbearbeitung;
}
