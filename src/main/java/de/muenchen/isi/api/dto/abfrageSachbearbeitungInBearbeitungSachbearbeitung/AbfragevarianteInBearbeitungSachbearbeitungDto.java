package de.muenchen.isi.api.dto.abfrageSachbearbeitungInBearbeitungSachbearbeitung;

import de.muenchen.isi.api.dto.AbfragevarianteSachbearbeitungDto;
import de.muenchen.isi.api.dto.abfrageAbfrageerstellungAngelegt.AbfragevarianteAngelegtDto;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbfragevarianteInBearbeitungSachbearbeitungDto extends AbfragevarianteAngelegtDto {

    @NotNull
    @Valid
    private AbfragevarianteSachbearbeitungDto abfragevarianteSachbearbeitung;
}
