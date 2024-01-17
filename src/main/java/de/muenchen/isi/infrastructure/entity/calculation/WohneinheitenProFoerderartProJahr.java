package de.muenchen.isi.infrastructure.entity.calculation;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WohneinheitenProFoerderartProJahr {

    private String foerderart;

    private String jahr;

    private BigDecimal wohneinheiten;
}
