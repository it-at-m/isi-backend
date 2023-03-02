package de.muenchen.isi.infrastructure.entity.filehandling;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
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
