package de.muenchen.isi.infrastructure.entity.benutzer;

import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import java.util.UUID;
import lombok.Data;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;

/**
 * Die Voreinstellungen eines Nutzers für die Startseite.
 * <p>
 * Wird als JSON in {@link Benutzer#getStartseitenEinstellung()} abgelegt.
 */
@Data
public class StartseitenEinstellung {

    private SchnellfilterVorgaenge schnellfilter;

    private SortAttribute sortBy;

    private SortOrder sortOrder;

    /**
     * Die ID des persönlichen Filters, der beim Laden der Anwendung in der Übersicht vorbelegt wird.
     * Ist keine ID gesetzt, gilt die Standardeinstellung.
     */
    private UUID standardFilterId;
}
