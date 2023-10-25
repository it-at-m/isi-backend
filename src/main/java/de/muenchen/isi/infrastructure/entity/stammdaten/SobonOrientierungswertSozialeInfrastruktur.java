package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.Altersklasse;
import de.muenchen.isi.infrastructure.entity.enums.Einrichtungstyp;
import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import java.math.BigDecimal;
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
        @UniqueConstraint(columnNames = { "jahr", "einrichtungstyp", "altersklasse", "wohnungstyp" }),
    },
    indexes = {
        @Index(
            name = "sobon_orientierungswert_soziale_infrastruktur_jahr_einrichtungstyp_altersklasse_wohnungstyp_index",
            columnList = "jahr, einrichtungstyp, altersklasse, wohnungstyp"
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SobonOrientierungswertSozialeInfrastruktur extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SobonVerfahrensgrundsaetzeJahr jahr;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Einrichtungstyp einrichtungstyp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Altersklasse altersklasse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Wohnungstyp wohnungstyp;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr1NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr2NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr3NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr4NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr5NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr6NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr7NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr8NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr9NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal einwohnerJahr10NachErsterstellung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal mittelwertEinwohnerJeWohnung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal faktor1EinwohnerJeWohnung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal faktorEinwohnerJeWohnung;

    @Column(precision = 10, scale = 4, nullable = false)
    private BigDecimal perzentil75ProzentEinwohnerJeWohnung;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal perzentil75ProzentGerundetEinwohnerJeWohnung;
}
