package de.muenchen.isi.domain.service.stammdaten;

import de.muenchen.isi.domain.model.stammdaten.FileInformationModel;
import de.muenchen.isi.infrastructure.repository.stammdaten.MimeTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class FileInformationStammService {

    private final List<String> allowedFileExtensions;

    private final List<String> allowedMimeTypes;

    private final Long maxFileSizeBytes;

    private final Long maxNumberOfFiles;

    private final MimeTypeRepository mimeTypeRepository;

    public FileInformationStammService(@Value("#{'${file.extensions.allowed}'.split(',')}") final List<String> allowedFileExtensions,
                                       @Value("#{'${file.mimetypes.allowed}'.split(',')}") final List<String> allowedMimeTypes,
                                       @Value("${file.size.max:31457280}") final Long maxFileSizeBytes,
                                       @Value("${file.number.max:20}") final Long maxNumberOfFiles,
                                       final MimeTypeRepository mimeTypeRepository) {
        this.allowedFileExtensions = allowedFileExtensions.stream()
                .map(StringUtils::trimToEmpty)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        this.allowedMimeTypes = allowedMimeTypes.stream()
                .map(StringUtils::trimToEmpty)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        this.maxFileSizeBytes = maxFileSizeBytes;
        this.maxNumberOfFiles = maxNumberOfFiles;
        this.mimeTypeRepository = mimeTypeRepository;
    }

    /**
     * @return die in der Anwendung erlaubten Dateiendungen (z.B. .pdf), maximalen Dateigrößen, ....
     */
    public FileInformationModel getFileInformation() {
        // Zusammenfügen der Dateiendungen von Mimetypes und aus Property.
        final Stream<String> fileExtensionsFromMimeTypes = this.getFileExtensionsFromMimeTypes(this.allowedMimeTypes);
        final List<String> combinedFileExtensions = Stream
                .concat(this.allowedFileExtensions.stream(), fileExtensionsFromMimeTypes)
                .distinct()
                .collect(Collectors.toList());
        final var fileInformationModel = new FileInformationModel();
        fileInformationModel.setMaxFileSizeBytes(this.maxFileSizeBytes);
        fileInformationModel.setMaxNumberOfFiles(this.maxNumberOfFiles);
        fileInformationModel.setAllowedFileExtensions(combinedFileExtensions);
        fileInformationModel.setAllowedMimeTypes(this.allowedMimeTypes);
        return fileInformationModel;
    }

    public Stream<String> getFileExtensionsFromMimeTypes(final List<String> mimeTypes) {
        final var listMimeTypeInfos = this.mimeTypeRepository.findAllByMimeTypes(mimeTypes);
        return listMimeTypeInfos.stream()
                .flatMap(mimeTypeInfos -> mimeTypeInfos.getFileExtensions().stream());
    }

}
