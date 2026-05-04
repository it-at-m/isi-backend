package de.muenchen.isi.domain.model.search.request.projection;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Verfahrensstand;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Enthält alle Felder die Abfrage hat
 */
public interface AbfrageProjection extends BaseProjection, AbfrageBauvorhabenProjection {
    ArtAbfrage artAbfrage();
    String name();
    StatusAbfrage statusAbfrage();
    UUID bauvorhabenId();
    LocalDate fristBearbeitung();

    default Verfahrensstand getVerfahrensstand() {
        return verfahrensstand();
    }

    default ArtAbfrage getArtAbfrage() {
        return artAbfrage();
    }

    default String getName() {
        return name();
    }

    default StatusAbfrage getStatusAbfrage() {
        return statusAbfrage();
    }

    default UUID getBauvorhabenId() {
        return bauvorhabenId();
    }

    default LocalDate getFristBearbeitung() {
        return fristBearbeitung();
    }
}
