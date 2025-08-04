package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FieldProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

// Record für Bauvorhaben
@ProjectionConstructor
public record BauvorhabenRecord(
    @IdProjection UUID id,
    String resultType,
    LocalDateTime createdDateTime,
    LocalDateTime lastModifiedDateTime,
    @FieldProjection(path = "adresse.coordinate.latitude") Double adresse_coordinate_latitude,
    @FieldProjection(path = "adresse.coordinate.longitude") Double adresse_coordinate_longitude,
    @FieldProjection(path = "verortungJson") VerortungMultiPolygon verortungJson,
    String nameVorhaben,
    BigDecimal grundstuecksgroesse,
    StandVerfahren stand_verfahren_filter,
    MultiPolygonGeometry umgriff
) {}
