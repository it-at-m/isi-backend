package de.muenchen.isi.api.dto.abfrageInBearbeitungSachbearbeitung;

import de.muenchen.isi.api.dto.common.VerortungDto;
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
public class BauleitplanverfahrenInBearbeitungSachbearbeitungDto extends AbfrageInBearbeitungSachbearbeitungDto {

    @Valid
    private VerortungDto verortung;

    @CustomNotNull
    @Size(min = 1, max = 5)
    private List<
        @NotNull @Valid AbfragevarianteBauleitplanverfahrenSachbearbeitungInBearbeitungSachbearbeitungDto
    > abfragevariantenBauleitplanverfahren;

    @CustomNotNull
    @Size(min = 0, max = 5)
    private List<
        @NotNull @Valid AbfragevarianteBauleitplanverfahrenInBearbeitungSachbearbeitungDto
    > abfragevariantenSachbearbeitungBauleitplanverfahren;
}
