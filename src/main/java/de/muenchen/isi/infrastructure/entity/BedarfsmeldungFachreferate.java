package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(
    indexes = {
        @Index(
            name = "bedarfsmeldung_fachreferate_abfragevariante_bauleitplanverfahren_id_index",
            columnList = "abfragevariante_bauleitplanverfahren_id"
        ),
        @Index(
            name = "bedarfsmeldung_fachreferate_abfragevariante_baugenehmigungsverfahren_id_index",
            columnList = "abfragevariante_baugenehmigungsverfahren_id"
        ),
        @Index(
            name = "bedarfsmeldung_fachreferate_abfragevariante_weiteres_verfahren_id_index",
            columnList = "abfragevariante_weiteres_verfahren_id"
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BedarfsmeldungFachreferate extends BaseEntity {

    @Column(nullable = false)
    private Integer anzahlEinrichtungen;

    @Column(nullable = false)
    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    @Column(nullable = true)
    private Integer anzahlKinderkrippengruppen;

    @Column(nullable = true)
    private Integer anzahlKindergartengruppen;

    @Column(nullable = true)
    private Integer anzahlHortgruppen;

    @Column(nullable = true)
    private Integer anzahlGrundschulzuege;
}
