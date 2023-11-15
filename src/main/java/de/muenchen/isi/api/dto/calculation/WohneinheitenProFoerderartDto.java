package de.muenchen.isi.api.dto.calculation;

import java.util.List;
import lombok.Data;

@Data
public class WohneinheitenProFoerderartDto {

    private String foerderart;

    private List<WohneinheitenProJahrDto> wohneinheitenProJahr;
}
