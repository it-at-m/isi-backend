package de.muenchen.isi.domain.model.stammdaten;

import java.util.List;
import lombok.Data;

@Data
public class FileInformationModel {

    private Long maxFileSizeBytes;

    private Long maxNumberOfFiles;

    private List<String> allowedMimeTypes;
}
