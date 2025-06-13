package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;

// Record für Abfrage
public record AbfrageRecord(
    @IdProjection UUID id,

    // BaseEntity-Felder
    String resultType,
    LocalDateTime createdDateTime,
    LocalDateTime lastModifiedDateTime,
    String artAbfrage,
    String name,
    StatusAbfrage statusAbfrage,
    UUID bauvorhabenId,
    Wgs84 abfrageCoordinate,
    LocalDate fristBearbeitung
) {}
