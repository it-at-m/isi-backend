package de.muenchen.isi.infrastructure.entity.common;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Data
public class Gemarkung {

    private BigDecimal nummer;

    @FullTextField(analyzer = "entity_analyzer_string_field")
    private String name;

    @IndexedEmbedded
    private Set<Flurstueck> flurstuecke;

    private MultiPolygonGeometry multiPolygon;
}
