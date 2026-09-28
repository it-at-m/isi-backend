package de.muenchen.isi.api.dto.abfrageEinpflegenBedarfsmeldung;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WeiteresVerfahrenEinpflegenBedarfsmeldungDto extends AbfrageEinpflegenBedarfsmeldungDto {

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<@NotNull @Valid AbfragevarianteWeiteresVerfahrenEinpflegenBedarfsmeldungDto> abfragevariantenWeiteresVerfahren;

    @NotNull
    @Size(min = 0, max = 5)
    private List<@NotNull @Valid AbfragevarianteWeiteresVerfahrenEinpflegenBedarfsmeldungDto> abfragevariantenSachbearbeitungWeiteresVerfahren;
}
