package de.muenchen.isi.api.dto.calculation;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class WohneinheitenProFoerderartProJahrDto {

    @NotNull
    private String foerderart;

    @NotNull
    private String jahr;

    @NotNull
    private BigDecimal wohneinheiten;
}
