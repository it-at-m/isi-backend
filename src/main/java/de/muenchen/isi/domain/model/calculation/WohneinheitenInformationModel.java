package de.muenchen.isi.domain.model.calculation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WohneinheitenInformationModel {

    private RealisierungsZeitraumModel realisierungsZeitraum;

    private Integer anzahlWohneinheitenGeplant;

    private BigDecimal geschossflaecheWohnenGeplant;

}
