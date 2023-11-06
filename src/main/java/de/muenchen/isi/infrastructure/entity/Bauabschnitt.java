package de.muenchen.isi.infrastructure.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(
    indexes = {
        @Index(
            name = "bauabschnitt_abfragevariante_bauleitplanverfahren_id_index",
            columnList = "abfragevariante_bauleitplanverfahren_id"
        ),
        @Index(
            name = "bauabschnitt_abfragevariante_baugenehmigungsverfahren_id_index",
            columnList = "abfragevariante_baugenehmigungsverfahren_id"
        ),
        @Index(
            name = "bauabschnitt_abfragevariante_weiteres_verfahren_id_index",
            columnList = "abfragevariante_weiteres_verfahren_id"
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Bauabschnitt extends BaseEntity {

    @Column(nullable = false)
    private String bezeichnung;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bauabschnitt_id")
    @OrderBy("createdDateTime asc")
    private List<Baugebiet> baugebiete;

    @Column(nullable = false)
    private Boolean technical;
}
