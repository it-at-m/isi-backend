package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
    indexes = {
        @Index(
            name = "bedarfsmeldung_fachreferate_abfragevariante_bauleitplanverfahren_id_index",
            columnList = "abfragevariante_bauleitplanverfahren_fachreferate_id"
        ),
        @Index(
            name = "bedarfsmeldung_fachreferate_abfragevariante_baugenehmigungsverfahren_id_index",
            columnList = "abfragevariante_baugenehmigungsverfahren_fachreferate_id"
        ),
        @Index(
            name = "bedarfsmeldung_fachreferate_abfragevariante_weiteres_verfahren_id_index",
            columnList = "abfragevariante_weiteres_verfahren_fachreferate_id"
        ),
        @Index(
            name = "bedarfsmeldung_abfrageersteller_abfragevariante_bauleitplanverfahren_id_index",
            columnList = "abfragevariante_bauleitplanverfahren_abfrageersteller_id"
        ),
        @Index(
            name = "bedarfsmeldung_abfrageersteller_abfragevariante_baugenehmigungsverfahren_id_index",
            columnList = "abfragevariante_baugenehmigungsverfahren_abfrageersteller_id"
        ),
        @Index(
            name = "bedarfsmeldung_abfrageersteller_abfragevariante_weiteres_verfahren_id_index",
            columnList = "abfragevariante_weiteres_verfahren_abfrageersteller_id"
        ),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Bedarfsmeldung extends BaseEntity {

    @Column(nullable = false)
    private Integer anzahlEinrichtungen;

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
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
