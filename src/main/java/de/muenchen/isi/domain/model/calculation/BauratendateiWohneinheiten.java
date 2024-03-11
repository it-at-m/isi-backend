package de.muenchen.isi.domain.model.calculation;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.math.BigDecimal;
import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BauratendateiWohneinheiten extends BaseEntityModel {

    private String foerderart;

    private String jahr;

    private BigDecimal wohneinheiten;
}
