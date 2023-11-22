package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(
    uniqueConstraints = { @UniqueConstraint(columnNames = { "gueltigAb", "foerderartBezeichnung" }) },
    indexes = {
        @Index(
            name = "staedtebaulicher_orientierungswert_gueltigAb_foerderartBezeichnung_index",
            columnList = "gueltigAb, foerderartBezeichnung"
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StaedtebaulicheOrientierungswert extends BaseEntity {

    @Column(nullable = false)
    private LocalDate gueltigAb;

    @Column(nullable = false)
    private String foerderartBezeichnung;

    @Column(nullable = false)
    private Long durchschnittlicheGrundflaeche;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal belegungsdichte;
}
