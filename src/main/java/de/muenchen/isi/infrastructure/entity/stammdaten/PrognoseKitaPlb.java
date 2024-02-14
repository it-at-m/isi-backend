package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.Altersgruppe;
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

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "prognose_kita_plb_altersgruppe_unique",
            columnNames = { "kitaPlb", "berichtsstand", "altersgruppe" }
        ),
    },
    indexes = { @Index(name = "prognosedaten_kita_plb_index", columnList = "kitaPlb, berichtsstand, altersgruppe") }
)
@Data
public class PrognoseKitaPlb extends BaseEntity {

    @Column(nullable = false)
    private Long kitaPlb;

    @Column(nullable = false)
    private LocalDate berichtsstand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Altersgruppe altersgruppe;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal anzahlKinder;
}
