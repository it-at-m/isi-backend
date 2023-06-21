package de.muenchen.isi.infrastructure.entity.filehandling;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
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
