package de.muenchen.isi.api.dto.calculation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WohneinheitenInformationDto {

    private RealisierungsZeitraumDto realisierungsZeitraum;

    private Integer anzahlWohneinheitenGeplant;

    private BigDecimal geschossflaecheWohnenGeplant;

}
