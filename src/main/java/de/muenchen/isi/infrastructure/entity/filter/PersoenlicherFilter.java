package de.muenchen.isi.infrastructure.entity.filter;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

/**
 * Ein gespeicherter persönlicher Filter eines Nutzers.
 * <p>
 * Wird als Element der JSON-Liste in
 * {@link de.muenchen.isi.infrastructure.entity.benutzer.Benutzer#getPersoenlicheFilter()} abgelegt.
 * Id und Zeitstempel werden deshalb im Service gepflegt und nicht vom JPA-Auditing, welches nur die
 * umgebende Zeile erfasst.
 */
@Data
public class PersoenlicherFilter {

    private UUID id;

    private String filterName;

    private LocalDateTime createdDateTime;

    private LocalDateTime lastModifiedDateTime;

    private FilterSettings filterSettings;
}
