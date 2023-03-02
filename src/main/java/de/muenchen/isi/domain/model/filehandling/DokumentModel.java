package de.muenchen.isi.domain.model.filehandling;

import de.muenchen.isi.domain.model.BaseEntityModel;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtDokument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DokumentModel extends BaseEntityModel {

    private FilepathModel filePath;

    private ArtDokument artDokument;

    private Long sizeInBytes;

    private String typDokument;

}
