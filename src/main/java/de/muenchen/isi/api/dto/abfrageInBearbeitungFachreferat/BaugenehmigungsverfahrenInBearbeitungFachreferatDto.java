package de.muenchen.isi.api.dto.abfrageInBearbeitungFachreferat;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaugenehmigungsverfahrenInBearbeitungFachreferatDto extends AbfrageInBearbeitungFachreferatDto {

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<
        @NotNull @Valid AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatDto
    > abfragevariantenBaugenehmigungsverfahren;

    @NotNull
    @Size(min = 0, max = 5)
    private List<
        @NotNull @Valid AbfragevarianteBaugenehmigungsverfahrenInBearbeitungFachreferatDto
    > abfragevariantenSachbearbeitungBaugenehmigungsverfahren;
}
