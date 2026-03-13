package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
    indexes = {
        @Index(
            name = "bedarfsmeldung_fachreferate_abfrgvar_bauleitplnvrfhrn_id_index",
            columnList = "abfrgvar_bauleitplnvrfhrn_fachreferate_id"
        ),
        @Index(
            name = "bedarfsmeldung_fachreferate_abfrgvar_baugnhmgsverfhrn_id_index",
            columnList = "abfrgvar_baugnhmgsverfhrn_fachreferate_id"
        ),
        @Index(
            name = "bedarfsmeldung_fachreferate_abfrgvar_weitrs_vrfhrn_id_index",
            columnList = "abfrgvar_weitrs_vrfhrn_fachreferate_id"
        ),
        @Index(
            name = "bedarfsmeldung_abfrgerstlr_abfrgvar_bauleitplnvrfhrn_id_index",
            columnList = "abfrgvar_bauleitplnvrfhrn_abfrgerstlr_id"
        ),
        @Index(
            name = "bedarfsmeldung_abfrgerstlr_abfrgvar_baugnhmgsverfhrn_id_index",
            columnList = "abfrgvar_baugnhmgsverfhrn_abfrgerstlrr_id"
        ),
        @Index(
            name = "bedarfsmeldung_abfrgerstlr_abfrgvar_weitrs_vrfhrn_id_index",
            columnList = "abfrgvar_weitrs_vrfhrn_abfrgerstlr_id"
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

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bedarfsmeldung_id")
    private List<Dokument> dokumente;
}
