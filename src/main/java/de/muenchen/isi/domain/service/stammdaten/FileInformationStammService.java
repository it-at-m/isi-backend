package de.muenchen.isi.domain.service.stammdaten;

import de.muenchen.isi.domain.model.stammdaten.FileInformationModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileInformationStammService {

    private final Long maxFileSizeBytes;

    private final Long maxNumberOfFiles;

    public FileInformationStammService(@Value("${file.size.max:31457280}") final Long maxFileSizeBytes,
                                       @Value("${file.number.max:20}") final Long maxNumberOfFiles) {
        this.maxFileSizeBytes = maxFileSizeBytes;
        this.maxNumberOfFiles = maxNumberOfFiles;
    }

    /**
     * @return die in der Anwendung erlaubten Dateiendungen (z.B. .pdf), maximalen Dateigrößen, ....
     */
    public FileInformationModel getFileInformation() {
        final var fileInformationModel = new FileInformationModel();
        fileInformationModel.setMaxFileSizeBytes(this.maxFileSizeBytes);
        fileInformationModel.setMaxNumberOfFiles(this.maxNumberOfFiles);
        return fileInformationModel;
    }

}
