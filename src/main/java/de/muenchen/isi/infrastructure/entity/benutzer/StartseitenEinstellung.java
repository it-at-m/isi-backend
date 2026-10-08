package de.muenchen.isi.infrastructure.entity.benutzer;

import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import lombok.Data;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;

/**
 * Die Voreinstellungen eines Nutzers für den Startseitenbereich "Meine Vorgänge".
 * <p>
 * Wird als JSON in {@link Benutzer#getStartseitenEinstellung()} abgelegt.
 */
@Data
public class StartseitenEinstellung {

    private SchnellfilterVorgaenge schnellfilter;

    private SortAttribute sortBy;

    private SortOrder sortOrder;
}
