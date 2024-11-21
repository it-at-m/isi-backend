package de.muenchen.isi.api.dto.abfrageStartBearbeitung;

import de.muenchen.isi.api.dto.common.VerortungMultiPolygonDto;
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
public class BauleitplanverfahrenStartBearbeitungDto extends AbfrageStartBearbeitungDto {

    @Valid
    private VerortungMultiPolygonDto verortung;

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<
        @NotNull @Valid AbfragevarianteBauleitplanverfahrenSachbearbeitungStartBearbeitungDto
    > abfragevariantenBauleitplanverfahren;

    @NotNull
    @Size(min = 0, max = 5)
    private List<
        @NotNull @Valid AbfragevarianteBauleitplanverfahrenStartBearbeitungDto
    > abfragevariantenSachbearbeitungBauleitplanverfahren;
}
