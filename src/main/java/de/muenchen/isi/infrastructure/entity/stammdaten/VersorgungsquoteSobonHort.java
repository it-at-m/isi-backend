package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VersorgungsquoteSobonHort extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String beschreibung;

    @Column(
        columnDefinition = "numeric(4,3) not null check (versorgungsquote_sobon >= 0 AND versorgungsquote_sobon <= 1)"
    )
    private BigDecimal versorgungsquoteSobon;
}
