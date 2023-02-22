package de.muenchen.isi.api.dto.stammdaten;

import lombok.Data;

@Data
public class FileInformationDto {

    private Long maxFileSizeBytes;

    private Long maxNumberOfFiles;

}
