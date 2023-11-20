package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
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
    uniqueConstraints = { @UniqueConstraint(columnNames = { "gueltigAb", "bildungseinrichtungBezeichnung" }) },
    indexes = { @Index(name = "versorgungsqoute_gruppen", columnList = "gueltigAb, bildungseinrichtungBezeichnung") }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VersorgungsquoteGruppenstaerke extends BaseEntity {

    @Column(nullable = false)
    private LocalDate gueltigAb;

    @Column(nullable = false)
    private String bildungseinrichtungBezeichnung;

    @Column(nullable = false)
    private int versorgungsquotePlanungsursaechlich;

    @Column(nullable = false)
    private int versorgungsquoteSobonUrsaechlich;

    @Column(nullable = false)
    private int gruppenstaerke;
}
