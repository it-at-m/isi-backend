package de.muenchen.isi.infrastructure.entity.stammdaten.baurate;

import java.math.BigDecimal;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Jahresrate {

    private Integer jahr;

    private BigDecimal rate;
}
