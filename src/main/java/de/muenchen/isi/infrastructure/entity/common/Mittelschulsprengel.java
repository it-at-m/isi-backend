package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

@Data
public class Mittelschulsprengel {

    @GenericField
    private Long nummer;

    private MultiPolygonGeometry multiPolygon;
}
