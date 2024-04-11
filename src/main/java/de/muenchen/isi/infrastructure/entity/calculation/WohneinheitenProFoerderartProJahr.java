package de.muenchen.isi.infrastructure.entity.calculation;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@Embeddable
public class WohneinheitenProFoerderartProJahr {

    @Column
    private String foerderart;

    @Column
    private String jahr;

    @Column(precision = 30, scale = 15)
    private BigDecimal wohneinheiten;
}
