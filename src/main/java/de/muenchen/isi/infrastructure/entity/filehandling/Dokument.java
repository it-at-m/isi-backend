package de.muenchen.isi.infrastructure.entity.filehandling;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
        @Index(name = "dokument_bauleitplanverfahren_id_index", columnList = "bauleitplanverfahren_id"),
        @Index(name = "dokument_baugenehmigungsverfahren_id_index", columnList = "baugenehmigungsverfahren_id"),
        @Index(name = "dokument_weiteres_verfahren_id_index", columnList = "weiteres_verfahren_id"),
        @Index(name = "dokument_bauvorhaben_id_index", columnList = "bauvorhaben_id"),
        @Index(name = "dokument_kommentar_id_index", columnList = "kommentar_id"),
        @Index(name = "dokument_abfrgvar_baugnhmgsverfhrn_id_index", columnList = "abfrgvar_baugnhmgsverfhrn_id"),
        @Index(name = "dokument_abfrgvar_bauleitplnvrfhrn_id_index", columnList = "abfrgvar_bauleitplnvrfhrn_id"),
        @Index(name = "dokument_abfrgvar_weitrs_vrfhrn_id_index", columnList = "abfrgvar_weitrs_vrfhrn_id"),
    }
)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Dokument extends BaseEntity {

    @Embedded
    private Filepath filePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtDokument artDokument;

    @Column(nullable = false)
    private Long sizeInBytes;

    @Column(nullable = false)
    private String typDokument;
}
