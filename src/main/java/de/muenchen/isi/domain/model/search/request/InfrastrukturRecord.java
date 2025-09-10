package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.domain.model.search.request.projection.InfrastruktureinrichtungProjection;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FieldProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

// Record für Infrastruktur
@ProjectionConstructor
public record InfrastrukturRecord(
    // BaseEntity-Felder
    @IdProjection UUID id,
    String resultType,
    LocalDateTime createdDateTime,
    LocalDateTime lastModifiedDateTime,

    @FieldProjection(path = "adresseJson") Adresse adresse,
    @FieldProjection(path = "verortungPointJson") VerortungPoint verortungPoint,
    InfrastruktureinrichtungTyp infrastruktureinrichtungTyp,
    String nameEinrichtung,
    StatusInfrastruktureinrichtung status,
    String bauvorhabenName
)
    implements InfrastruktureinrichtungProjection {}
