package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(
    indexes = {
        @Index(name = "kommentar_bauvorhaben_id_index", columnList = "bauvorhaben_id"),
        @Index(name = "kommentar_infrastruktureinrichtung_id_index", columnList = "infrastruktureinrichtung_id"),
    }
)
public class Kommentar extends BaseEntity {

    @Column(length = 32)
    private String datum;

    @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "bauvorhaben_id")
    private Bauvorhaben bauvorhaben;

    @ManyToOne
    @JoinColumn(name = "infrastruktureinrichtung_id")
    private Infrastruktureinrichtung infrastruktureinrichtung;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "kommentar_id")
    private List<Dokument> dokumente;
}
