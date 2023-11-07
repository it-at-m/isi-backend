package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
public class Umlegung extends BaseEntity {

    @Column(nullable = false)
    private String bezeichnung;

    @Column(nullable = false)
    private LocalDate gueltigAb;

    @Column(nullable = false)
    private String bezeichnungFoerderart1;

    @Column(nullable = false)
    private BigDecimal prozentAnteilFoerderart1;

    @Column(nullable = false)
    private String bezeichnungFoerderart2;

    @Column(nullable = false)
    private BigDecimal prozentAnteilFoerderart2;
}
