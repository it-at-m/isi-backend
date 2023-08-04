package de.muenchen.isi.infrastructure.entity.common;

import de.muenchen.isi.domain.service.search.SearchPreparationService;
import de.muenchen.isi.infrastructure.adapter.search.StringCustomSuggesterBinder;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;

@Data
public class Flurstueck {

    @FullTextField(analyzer = "entity_analyzer_string_field")
    @NonStandardField(
        name = "name" + SearchPreparationService.SUFFIX_ATTRIBUTE_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringCustomSuggesterBinder.class)
    )
    private String nummer;

    private BigDecimal flaecheQm;

    private Long zaehler;

    private Long nenner;

    private Long eigentumsart;

    private String eigentumsartBedeutung;

    private BigDecimal gemarkungNummer;

    private MultiPolygonGeometry multiPolygon;
}
