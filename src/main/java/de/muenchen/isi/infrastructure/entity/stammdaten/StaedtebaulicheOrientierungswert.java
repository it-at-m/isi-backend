package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(
    uniqueConstraints = { @UniqueConstraint(columnNames = { "gueltigAb", "foerderartBezeichnung" }) },
    indexes = {
        @Index(name = "staedtebau_orientwrt_gltgAb_frdrtBzchng_index", columnList = "gueltigAb, foerderartBezeichnung"),
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
