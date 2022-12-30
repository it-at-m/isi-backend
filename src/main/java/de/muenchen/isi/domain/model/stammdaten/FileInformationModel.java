package de.muenchen.isi.domain.model.stammdaten;

import lombok.Data;

import java.util.List;

@Data
public class FileInformationModel {

    private Long maxFileSizeBytes;

    private Long maxNumberOfFiles;

    private List<String> allowedFileExtensions;

    private List<String> allowedMimeTypes;

}
