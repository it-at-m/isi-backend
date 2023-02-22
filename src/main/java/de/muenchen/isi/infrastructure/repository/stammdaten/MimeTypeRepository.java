package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.MimeTypeInformation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Diese Klasse hält für einen MIME-Type die möglichen Dateiendungen vor.
 */
@Repository
@Slf4j
public class MimeTypeRepository {

    public Optional<MimeTypeInformation> findByMimeType(final String mimeType) {
        final TikaConfig config = TikaConfig.getDefaultConfig();
        final MimeTypes mimeTypesRepository = config.getMimeRepository();
        Optional<MimeTypeInformation> mimeTypeInformationResult;
        try {
            final var foundMimeType = mimeTypesRepository.getRegisteredMimeType(mimeType);
            if (ObjectUtils.isNotEmpty(foundMimeType)) {
                final var mimeTypeInformation = new MimeTypeInformation();
                mimeTypeInformation.setMimeType(mimeType);
                mimeTypeInformation.setFileExtensions(foundMimeType.getExtensions());
                mimeTypeInformationResult = Optional.of(mimeTypeInformation);
            } else {
                mimeTypeInformationResult = Optional.empty();
            }
        } catch (final MimeTypeException exception) {
            final var message = String.format("Der MimeType %s ist ungültig.", mimeType);
            log.error(message);
            mimeTypeInformationResult = Optional.empty();
        }
        return mimeTypeInformationResult;
    }

    public List<MimeTypeInformation> findAllByMimeTypes(final List<String> mimeTypes) {
        return mimeTypes.stream()
                .map(this::findByMimeType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

}
