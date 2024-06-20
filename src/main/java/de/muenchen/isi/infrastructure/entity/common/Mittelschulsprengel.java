package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

@Data
public class Mittelschulsprengel {

    @FullTextField
    private Long nummer;

    private MultiPolygonGeometry multiPolygon;
}
