package de.muenchen.isi.api.dto.startseite;

import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;

@Data
public class StartseitenEinstellungDto {

    @NotNull
    private SchnellfilterVorgaenge schnellfilter;

    @NotNull
    private SortAttribute sortBy;

    @NotNull
    private SortOrder sortOrder;

    /**
     * Optionaler persönlicher Filter, der beim Laden der Anwendung in der Übersicht vorbelegt wird.
     */
    private UUID standardFilterId;
}
