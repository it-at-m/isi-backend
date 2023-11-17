package de.muenchen.isi.api.dto.calculation;

import de.muenchen.isi.domain.model.BauabschnittModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonOrientierungswertJahr;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalculationRequestDto {

    @NotEmpty
    private List<BauabschnittModel> bauabschnitte;

    @NotNull
    private SobonOrientierungswertJahr sobonJahr;

    @NotNull
    private LocalDate gueltigAb;
}
