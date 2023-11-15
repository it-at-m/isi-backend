package de.muenchen.isi.api.dto.calculation;

import de.muenchen.isi.api.dto.AbfragevarianteDto;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalculationRequestDto {

    @NotNull
    private AbfragevarianteDto abfragevariante;

    @NotNull
    private LocalDate gueltigAb;
}
