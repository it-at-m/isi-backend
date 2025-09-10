package de.muenchen.isi.domain.model.search.request.projection;

import de.muenchen.isi.infrastructure.entity.common.Adresse;
import java.time.LocalDateTime;
import java.util.UUID;

public interface BaseProjection {
    UUID id();
    LocalDateTime createdDateTime();
    LocalDateTime lastModifiedDateTime();
    Adresse adresse();

    default UUID getId() {
        return id();
    }

    default LocalDateTime getCreatedDateTime() {
        return createdDateTime();
    }

    default LocalDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime();
    }

    default Adresse getAdresse() {
        return adresse();
    }
}
