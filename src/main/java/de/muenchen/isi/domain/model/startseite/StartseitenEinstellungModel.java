package de.muenchen.isi.domain.model.startseite;

import de.muenchen.isi.domain.model.enums.SchnellfilterVorgaenge;
import de.muenchen.isi.domain.model.enums.SortAttribute;
import lombok.Data;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;

@Data
public class StartseitenEinstellungModel {

    private SchnellfilterVorgaenge schnellfilter;

    private SortAttribute sortBy;

    private SortOrder sortOrder;
}
