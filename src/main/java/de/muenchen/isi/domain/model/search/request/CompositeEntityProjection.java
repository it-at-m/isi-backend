package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

@ProjectionConstructor
public record CompositeEntityProjection(
    @IdProjection UUID id,

    // BaseEntity-Felder
    String resultType,
    LocalDateTime createdDateTime,
    LocalDateTime lastModifiedDateTime,

    // Bauvorhaben-spezifisch
    String nameVorhaben,
    BigDecimal grundstuecksgroesse,
    StandVerfahren stand_verfahren_filter,
    Wgs84 bauvorhabenCoordinate,
    MultiPolygonGeometry umgriff,

    // Abfrage-spezifisch
    ArtAbfrage artAbfrage,
    String name,
    StatusAbfrage statusAbfrage,
    UUID bauvorhabenId,
    Wgs84 abfrageCoordinate,
    LocalDate fristBearbeitung,

    // Infrastruktureinrichtung-spezifisch
    String infrastruktureinrichtungTyp,
    String nameEinrichtung,
    StatusInfrastruktureinrichtung status,
    String bauvorhabenName,
    Wgs84 infrastruktureinrichtungCoordinate
) {}
