package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
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
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "gueltigAb", "einrichtungstyp", "altersklasse", "foerderartBezeichnung" }),
    },
    indexes = {
        @Index(
            name = "sobon_orientierungswert_soziale_infrastruktur_jahr_einrichtungstyp_altersklasse_foerderartBezeichnung_index",
            columnList = "gueltigAb, einrichtungstyp, altersklasse, foerderartBezeichnung"
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SobonOrientierungswertSozialeInfrastruktur extends BaseEntity {

    @Column(nullable = false)
    private LocalDate gueltigAb;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InfrastruktureinrichtungTyp einrichtungstyp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Altersklasse altersklasse;

    @Column(nullable = false)
    private String foerderartBezeichnung;

    @Column(precision = 20, scale = 15, nullable = false)
    private BigDecimal einwohnerJahr1NachErsterstellung;

    @Column(precision = 20, scale = 15, nullable = false)
    private BigDecimal einwohnerJahr2NachErsterstellung;

    @Column(precision = 20, scale = 15, nullable = false)
    private BigDecimal einwohnerJahr3NachErsterstellung;

    @Column(precision = 20, scale = 15, nullable = false)
    private BigDecimal einwohnerJahr4NachErsterstellung;

    @Column(precision = 20, scale = 15, nullable = false)
    private BigDecimal einwohnerJahr5NachErsterstellung;

    @Column(precision = 20, scale = 15, nullable = false)
    private BigDecimal einwohnerJahr6NachErsterstellung;

    @Column(precision = 20, scale = 15, nullable = false)
    private BigDecimal einwohnerJahr7NachErsterstellung;

    @Column(precision = 20, scale = 15, nullable = false)
    private BigDecimal einwohnerJahr8NachErsterstellung;

    @Column(precision = 20, scale = 15, nullable = false)
    private BigDecimal einwohnerJahr9NachErsterstellung;

    @Column(precision = 20, scale = 15, nullable = false)
    private BigDecimal einwohnerJahr10NachErsterstellung;

    @Column(precision = 20, scale = 15, nullable = false)
    private BigDecimal stammwertArbeitsgruppe;
}
