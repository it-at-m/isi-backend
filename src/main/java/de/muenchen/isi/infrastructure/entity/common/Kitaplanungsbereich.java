package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

@Data
public class Kitaplanungsbereich {

    private Long kitaPlb;

    @FullTextField
    private String kitaPlbT;

    private MultiPolygonGeometry multiPolygon;
}
