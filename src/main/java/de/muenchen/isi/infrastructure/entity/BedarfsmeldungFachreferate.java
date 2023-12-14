package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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
    @Enumerated(EnumType.STRING)
    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    @Column
    private Integer anzahlKinderkrippengruppen;

    @Column
    private Integer anzahlKindergartengruppen;

    @Column
    private Integer anzahlHortgruppen;

    @Column
    private Integer anzahlGrundschulzuege;
}
