package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.PointGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FieldProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

// Record für Infrastruktur
@ProjectionConstructor
public record InfrastrukturRecord(
    @IdProjection UUID id,

    // BaseEntity-Felder
    String resultType,
    LocalDateTime createdDateTime,
    LocalDateTime lastModifiedDateTime,
    @FieldProjection(path = "adresse.coordinate.latitude") Double adresse_coordinate_latitude,

    @FieldProjection(path = "adresse.coordinate.longitude") Double adresse_coordinate_longitude,

    PointGeometry pointGeometry,
    InfrastruktureinrichtungTyp infrastruktureinrichtungTyp,
    String nameEinrichtung,
    StatusInfrastruktureinrichtung status,
    String bauvorhabenName,
    Wgs84 infrastruktureinrichtungCoordinate
) {}
