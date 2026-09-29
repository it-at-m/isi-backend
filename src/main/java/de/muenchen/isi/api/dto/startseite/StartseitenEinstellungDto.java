package de.muenchen.isi.api.dto.startseite;

import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import jakarta.validation.constraints.NotNull;
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
}
