package de.muenchen.isi.api.dto.stammdaten;

import lombok.Data;

import java.util.List;

@Data
public class FileInformationDto {

    private Long maxFileSizeBytes;

    private Long maxNumberOfFiles;

    private List<String> allowedFileExtensions;

    private List<String> allowedMimeTypes;

}
