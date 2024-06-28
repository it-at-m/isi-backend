package de.muenchen.isi.api.dto.bauratendatei;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.reporting.client.model.WohneinheitenProFoerderartProJahrDto;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauratendateiInputDto extends BaseEntityDto {

    private List<String> grundschulsprengel;

    private List<String> mittelschulsprengel;

    private List<String> viertel;

    private List<WohneinheitenProFoerderartProJahrDto> wohneinheiten;
}
