package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.apache.tika.config.Field;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FieldProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

@ProjectionConstructor
public record CompositeEntityProjection(
    @IdProjection UUID id,

    // BaseEntity-Felder
    String resultType,
    LocalDateTime createdDateTime,
    LocalDateTime lastModifiedDateTime,

    @FieldProjection(path = "adresse.coordinate.latitude") Double adresse_coordinate_latitude,

    @FieldProjection(path = "adresse.coordinate.longitude") Double adresse_coordinate_longitude,

    @FieldProjection(path = "verortungJson") String verortung,

    // Bauvorhaben-spezifisch
    String nameVorhaben,
    BigDecimal grundstuecksgroesse,
    StandVerfahren stand_verfahren_filter,
    Wgs84 bauvorhabenCoordinate,
    MultiPolygonGeometry umgriff,

    // Abfrage-spezifisch
    String artAbfrage,
    String name,
    StatusAbfrage statusAbfrage,
    UUID bauvorhabenId,
    Wgs84 abfrageCoordinate,
    LocalDate fristBearbeitung,

    // Infrastruktureinrichtung-spezifisch
    InfrastruktureinrichtungTyp infrastruktureinrichtungTyp,
    String nameEinrichtung,
    StatusInfrastruktureinrichtung status,
    String bauvorhabenName,
    Wgs84 infrastruktureinrichtungCoordinate,
    @FieldProjection(path = "verortungPointJson") String verortungPoint
) {}
