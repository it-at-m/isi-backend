package de.muenchen.isi.domain.model.search.request;

import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.search.engine.search.common.ValueModel;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FieldProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;

// Bauvorhaben + Abfrage
@ProjectionConstructor
public record BauvorhabenAbfrageRecord(
    @IdProjection UUID id,
    String resultType,
    LocalDateTime createdDateTime,
    LocalDateTime lastModifiedDateTime,

    @FieldProjection(path = "adresseJson") Adresse adresseJson,
    @FieldProjection(path = "verortungJson") VerortungMultiPolygon verortungJson,

    // Bauvorhaben-Felder
    String nameVorhaben,
    BigDecimal grundstuecksgroesse,
    StandVerfahren stand_verfahren_filter,
    MultiPolygonGeometry umgriff,

    // Abfrage-Felder
    ArtAbfrage artAbfrage,
    String name,
    StatusAbfrage statusAbfrage,
    UUID bauvorhabenId,
    LocalDate fristBearbeitung
) {}
