package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.Wohnungstyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"jahr", "wohnungstyp"})
        },
        indexes = {
                @Index(name = "jahr_wohnungstyp_index", columnList = "jahr, wohnungstyp")
        }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StaedtebaulicheOrientierungswert extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SobonVerfahrensgrundsaetzeJahr jahr;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Wohnungstyp wohnungstyp;

    @Column(nullable = false)
    private Long durchschnittlicheGrundflaeche;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal belegungsdichte;

}
