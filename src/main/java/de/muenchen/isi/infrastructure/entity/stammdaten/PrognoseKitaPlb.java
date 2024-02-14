package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.Altersgruppe;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data
public class PrognoseKitaPlb extends BaseEntity {

    @Column(nullable = false)
    private Long kitaPlb;

    @Column(nullable = false)
    private LocalDate berichtsstand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Altersgruppe altersgruppe;

    @Column(precision = 20, scale = 2, nullable = false)
    private BigDecimal anzahlKinder;
}
