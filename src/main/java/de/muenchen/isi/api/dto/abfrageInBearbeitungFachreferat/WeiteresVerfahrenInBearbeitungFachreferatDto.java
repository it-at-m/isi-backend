package de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat;

import de.muenchen.isi.api.validation.CustomNotNull;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WeiteresVerfahrenInBearbeitungFachreferatDto extends AbfrageInBearbeitungFachreferatDto {

    @CustomNotNull
    @Size(min = 1, max = 5)
    private List<
        @NotNull @Valid AbfragevarianteWeiteresVerfahrenInBearbeitungFachreferatDto
    > abfragevariantenWeiteresVerfahren;

    @CustomNotNull
    @Size(min = 0, max = 5)
    private List<
        @NotNull @Valid AbfragevarianteWeiteresVerfahrenInBearbeitungFachreferatDto
    > abfragevariantenSachbearbeitungWeiteresVerfahren;
}
