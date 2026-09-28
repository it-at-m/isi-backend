package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
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
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = { "gueltigAb", "jahrBezeichnung", "einrichtungstyp", "altersklasse", "foerderartBezeichnung" }
        ),
    },
    indexes = {
        @Index(
            name = "sobon_orientwrt_soz_infra_jahr_einr_typ_altkl_frdrtBzchng_index",
            columnList = "gueltigAb, jahrBezeichnung, einrichtungstyp, altersklasse, foerderartBezeichnung"
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SobonOrientierungswertSozialeInfrastruktur extends BaseEntity {

    @Column(nullable = false)
    private LocalDate gueltigAb;

    @Column(nullable = false)
    private String jahrBezeichnung;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InfrastruktureinrichtungTyp einrichtungstyp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Altersklasse altersklasse;

    @Column(nullable = false)
    private String foerderartBezeichnung;

    @Column(precision = 20, scale = 15)
    private BigDecimal einwohnerJahr1nachErsterstellung;

    @Column(precision = 20, scale = 15)
    private BigDecimal einwohnerJahr2nachErsterstellung;

    @Column(precision = 20, scale = 15)
    private BigDecimal einwohnerJahr3nachErsterstellung;

    @Column(precision = 20, scale = 15)
    private BigDecimal einwohnerJahr4nachErsterstellung;

    @Column(precision = 20, scale = 15)
    private BigDecimal einwohnerJahr5nachErsterstellung;

    @Column(precision = 20, scale = 15)
    private BigDecimal einwohnerJahr6nachErsterstellung;

    @Column(precision = 20, scale = 15)
    private BigDecimal einwohnerJahr7nachErsterstellung;

    @Column(precision = 20, scale = 15)
    private BigDecimal einwohnerJahr8nachErsterstellung;

    @Column(precision = 20, scale = 15)
    private BigDecimal einwohnerJahr9nachErsterstellung;

    @Column(precision = 20, scale = 15)
    private BigDecimal einwohnerJahr10nachErsterstellung;

    @Column(precision = 20, scale = 15)
    private BigDecimal stammwertArbeitsgruppe;
}
