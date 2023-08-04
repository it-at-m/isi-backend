package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.domain.service.search.SearchPreparationService;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;

@Data
public class Gemarkung {

    private BigDecimal nummer;

    @FullTextField(analyzer = "entity_analyzer_string_field")
    @NonStandardField(
        name = "name" + SearchPreparationService.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    private String name;

    @IndexedEmbedded
    private Set<Flurstueck> flurstuecke;

    private MultiPolygonGeometry multiPolygon;
}
