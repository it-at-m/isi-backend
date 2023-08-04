package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.domain.service.search.SearchPreparationService;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import lombok.Data;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;

@Data
public class Stadtbezirk {

    private String nummer;

    @FullTextField(analyzer = "entity_analyzer_string_field")
    @NonStandardField(
        name = "name" + SearchPreparationService.SUFFIX_ATTRIBUTE_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    private String name;

    private MultiPolygonGeometry multiPolygon;
}
