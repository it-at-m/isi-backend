package de.muenchen.isi.infrastructure.entity.calculation;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
public class WohneinheitenProFoerderartProJahr extends BaseEntity {

    @Column
    private String foerderart;

    @Column
    private String jahr;

    @Column(precision = 30, scale = 15)
    private BigDecimal wohneinheiten;
}
