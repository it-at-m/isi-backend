package de.muenchen.isi.infrastructure.entity.filehandling;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(
    indexes = {
        @Index(name = "dokument_bauleitplanverfahren_id_index", columnList = "bauleitplanverfahren_id"),
        @Index(name = "dokument_baugenehmigungsverfahren_id_index", columnList = "baugenehmigungsverfahren_id"),
        @Index(name = "dokument_bauvorhaben_id_index", columnList = "bauvorhaben_id"),
        @Index(name = "dokument_kommentar_id_index", columnList = "kommentar_id"),
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
