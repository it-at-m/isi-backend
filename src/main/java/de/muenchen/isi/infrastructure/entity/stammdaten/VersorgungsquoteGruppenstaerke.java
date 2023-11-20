package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.Bildungseinrichtung;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(
    uniqueConstraints = { @UniqueConstraint(columnNames = { "gueltigAb", "bildungseinrichtung" }) },
    indexes = { @Index(name = "versorgungsqoute_gruppen", columnList = "gueltigAb, bildungseinrichtung") }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VersorgungsquoteGruppenstaerke extends BaseEntity {

    @Column(nullable = false)
    private LocalDate gueltigAb;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Bildungseinrichtung bildungseinrichtung;

    @Column(nullable = false, precision = 4, scale = 3)
    private BigDecimal versorgungsquotePlanungsursaechlich;

    @Column(nullable = false, precision = 4, scale = 3)
    private BigDecimal versorgungsquoteSobonUrsaechlich;

    @Column(nullable = false)
    private int gruppenstaerke;
}
