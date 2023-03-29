package de.muenchen.isi.api.dto.stammdaten;

import java.util.List;
import lombok.Data;

@Data
public class FileInformationDto {

    private Long maxFileSizeBytes;

    private Long maxNumberOfFiles;

    private List<String> allowedMimeTypes;
}
