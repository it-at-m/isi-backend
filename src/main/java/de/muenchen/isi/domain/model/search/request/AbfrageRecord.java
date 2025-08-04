package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FieldProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

// Record für Abfrage
@ProjectionConstructor
public record AbfrageRecord(
    @IdProjection UUID id,

    // BaseEntity-Felder
    String resultType,
    LocalDateTime createdDateTime,
    LocalDateTime lastModifiedDateTime,
    @FieldProjection(path = "adresse.coordinate.latitude") Double adresse_coordinate_latitude,

    @FieldProjection(path = "adresse.coordinate.longitude") Double adresse_coordinate_longitude,
    @FieldProjection(path = "verortungJson") String verortung,
    String artAbfrage,
    String name,
    StatusAbfrage statusAbfrage,
    UUID bauvorhabenId,
    LocalDate fristBearbeitung
) {}
