package de.muenchen.isi.api.dto.calculation;

import de.muenchen.isi.api.dto.BaseEntityDto;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauratendateiWohneinheiten extends BaseEntityDto {

    private String foerderart;

    private String jahr;

    private BigDecimal wohneinheiten;
}
