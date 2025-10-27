package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.domain.model.search.request.projection.BauvorhabenProjection;
import de.muenchen.isi.domain.model.search.request.projection.InfrastruktureinrichtungProjection;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ResultType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FieldProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

@ProjectionConstructor
public record BauvorhabenInfrastruktureinrichtungRecord(
    // BaseEntity-Attribute
    @IdProjection UUID id,
    ResultType resultType,
    LocalDateTime createdDateTime,
    LocalDateTime lastModifiedDateTime,
    @FieldProjection(path = "adresseJson") Adresse adresse,
    @FieldProjection(path = "verortungJson") VerortungMultiPolygon verortung,
    @FieldProjection(path = "verortungPointJson") VerortungPoint verortungPoint,
    StandVerfahren standVerfahren,
    // Bauvorhaben-Attribute
    String nameVorhaben,
    BigDecimal grundstuecksgroesse,
    // Infrastruktureinrichtung-Attribute
    InfrastruktureinrichtungTyp infrastruktureinrichtungTyp,
    String nameEinrichtung,
    StatusInfrastruktureinrichtung status,
    String bauvorhabenName
) implements BauvorhabenProjection, InfrastruktureinrichtungProjection {}
