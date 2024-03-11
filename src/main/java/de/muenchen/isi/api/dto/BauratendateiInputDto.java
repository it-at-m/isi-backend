package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.dto.calculation.BauratendateiWohneinheiten;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauratendateiInputDto extends BaseEntityDto {

    private String grundschulsprengel;

    private String mittelschulsprengel;

    private String viertel;

    private List<BauratendateiWohneinheiten> wohneinheiten;
}
