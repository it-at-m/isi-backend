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
            columnList = "abfrgvar_bauleitplnvrfhrn_abfrageersteller_id"
        ),
        @Index(
            name = "bedarfsmeldung_abfrgerstlr_abfrgvar_baugnhmgsverfhrn_id_index",
            columnList = "abfrgvar_baugnhmgsverfhrn_abfrageersteller_id"
        ),
        @Index(
            name = "bedarfsmeldung_abfrgerstlr_abfrgvar_weitrs_vrfhrn_id_index",
            columnList = "abfrgvar_weitrs_vrfhrn_abfrageersteller_id"
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
