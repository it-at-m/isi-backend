package de.muenchen.isi.domain.model.calculation;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WohneinheitenProFoerderartProJahrModel {

    private String foerderart;

    private String jahr;

    private BigDecimal wohneinheiten;
}
