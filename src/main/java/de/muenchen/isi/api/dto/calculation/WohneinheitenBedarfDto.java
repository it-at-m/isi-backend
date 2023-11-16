package de.muenchen.isi.api.dto.calculation;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WohneinheitenBedarfDto {

    @NotNull
    private String foerderart;

    @NotNull
    private Integer jahr; // JJJJ

    @NotNull
    private BigDecimal wohneinheiten;
}
