package de.muenchen.isi.domain.model.calculation;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class WohneinheitenInformationModel {

    private RealisierungsZeitraumModel realisierungsZeitraum;

    private Integer anzahlWohneinheitenGeplant;

    private BigDecimal geschossflaecheWohnenGeplant;
}
