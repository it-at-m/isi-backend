package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Foerderart;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "UniqueUmlegungFoerderarten", columnNames = { "bezeichnung", "gueltigAb" }),
    }
)
@Data
@EqualsAndHashCode
public class UmlegungFoerderarten extends BaseEntity {

    @Column(nullable = false)
    private String bezeichnung;

    @Column(nullable = false)
    private LocalDate gueltigAb;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Set<Foerderart> umlegungsschluessel;
}
