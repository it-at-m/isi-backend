package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

@Data
public class Schulstandort {

    private Long schulnummer;

    @FullTextField
    private String schulname;

    private MultiPolygonGeometry multiPolygon;
}
