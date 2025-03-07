package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.infrastructure.entity.enums.EntityType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

@ProjectionConstructor
public record CompositeEntityProjection(
    @IdProjection UUID id,
    EntityType type,
    String nameVorhaben,
    StandVerfahren stand_verfahren_filter,
    UncertainBoolean sobon_relevant_filter,
    String name,
    StatusAbfrage statusAbfrage_filter,
    String nameEinrichtung,
    StatusInfrastruktureinrichtung status_infrastruktureinrichtung_filter,
    LocalDateTime createdDateTime,
    LocalDateTime lastModifiedDateTime
) {}
