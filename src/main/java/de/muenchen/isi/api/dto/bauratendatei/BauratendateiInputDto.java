package de.muenchen.isi.api.dto.bauratendatei;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.dto.bauratendatei.BauratendateiWohneinheitenDto;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BauratendateiInputDto extends BaseEntityDto {

    private Set<String> grundschulsprengel;

    private Set<String> mittelschulsprengel;

    private Set<String> viertel;

    private List<BauratendateiWohneinheitenDto> wohneinheiten;
}
