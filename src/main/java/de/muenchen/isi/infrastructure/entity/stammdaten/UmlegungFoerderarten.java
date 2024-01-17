package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "UniqueUmlegungFoerderarten", columnNames = { "bezeichnung", "gueltigAb" }),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UmlegungFoerderarten extends BaseEntity {

    @Column(nullable = false)
    private String bezeichnung;

    @Column(nullable = false)
    private LocalDate gueltigAb;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Umlegungsschluessel umlegungsschluessel;
}
