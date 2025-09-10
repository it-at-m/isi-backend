package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.domain.model.search.request.projection.AbfrageProjection;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FieldProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

// Record für Abfrage
@ProjectionConstructor
public record AbfrageRecord(
    // BaseEntity-Felder
    @IdProjection UUID id,
    String resultType,
    LocalDateTime createdDateTime,
    LocalDateTime lastModifiedDateTime,
    @FieldProjection(path = "adresse") Adresse adresse,
    @FieldProjection(path = "verortungJson") VerortungMultiPolygon verortung,
    StandVerfahren standVerfahren,
    ArtAbfrage artAbfrage,
    String name,
    StatusAbfrage statusAbfrage,
    UUID bauvorhabenId,
    LocalDate fristBearbeitung
)
    implements AbfrageProjection {}
