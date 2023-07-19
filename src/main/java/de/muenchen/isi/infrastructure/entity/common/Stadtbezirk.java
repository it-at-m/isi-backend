package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

@Data
public class Stadtbezirk {

    private String nummer;

    @FullTextField(analyzer = "entity_analyzer_string_field")
    private String name;

    private MultiPolygonGeometry multiPolygon;
}
