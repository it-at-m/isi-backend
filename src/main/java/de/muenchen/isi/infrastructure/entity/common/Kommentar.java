package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung.Infrastruktureinrichtung;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(
    indexes = {
        @Index(name = "bauvorhaben_id_index", columnList = "bauvorhaben_id"),
        @Index(name = "infrastruktureinrichtung_id_index", columnList = "infrastruktureinrichtung_id"),
    }
)
public class Kommentar extends BaseEntity {

    @Column(nullable = true, length = 32)
    private String datum;

    @Type(type = "org.hibernate.type.TextType")
    @Column(nullable = true)
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
