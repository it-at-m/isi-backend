package de.muenchen.isi.api.dto.calculation;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlanungsursachlicheWohneinheitenDto {

    @NotNull
    private String foerderart;

    @NotNull
    private String jahr;

    @NotNull
    private BigDecimal wohneinheiten;
}
