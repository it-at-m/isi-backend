package de.muenchen.isi.api.dto.calculation;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class WohneinheitenInformationDto {

    private RealisierungsZeitraumDto realisierungsZeitraum;

    private Integer anzahlWohneinheitenGeplant;

    private BigDecimal geschossflaecheWohnenGeplant;
}
