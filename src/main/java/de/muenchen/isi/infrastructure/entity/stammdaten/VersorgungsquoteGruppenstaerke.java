package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    uniqueConstraints = { @UniqueConstraint(columnNames = { "gueltigAb", "infrastruktureinrichtungTyp" }) },
    indexes = {
        @Index(name = "versorgungsqoute_gruppenstaerke", columnList = "gueltigAb, infrastruktureinrichtungTyp"),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VersorgungsquoteGruppenstaerke extends BaseEntity {

    @Column(nullable = false)
    private LocalDate gueltigAb;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    @Column(
        columnDefinition = "numeric(4,3) not null check (versorgungsquote_planungsursaechlich >= 0 AND versorgungsquote_planungsursaechlich <= 1)"
    )
    private BigDecimal versorgungsquotePlanungsursaechlich;

    @Column(
        columnDefinition = "numeric(4,3) not null check (versorgungsquote_sobon_ursaechlich >= 0 AND versorgungsquote_sobon_ursaechlich <= 1)"
    )
    private BigDecimal versorgungsquoteSobonUrsaechlich;

    @Column(nullable = false)
    private int gruppenstaerke;
}
