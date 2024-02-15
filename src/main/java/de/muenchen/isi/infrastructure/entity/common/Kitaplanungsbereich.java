package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;

@Data
public class Kitaplanungsbereich {

    private Long kitaPlb;

    private String kitaPlbT;

    private MultiPolygonGeometry multiPolygon;
}
