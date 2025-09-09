package de.muenchen.isi.domain.model.search.request.projection;

import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface AbfrageProjection {
    UUID id();
    LocalDateTime createdDateTime();
    LocalDateTime lastModifiedDateTime();
    Adresse adresseJson();
    VerortungMultiPolygon verortungJson();
    String artAbfrage();
    String name();
    String statusAbfrage();
    UUID bauvorhabenId();
    LocalDate fristBearbeitung();
}
