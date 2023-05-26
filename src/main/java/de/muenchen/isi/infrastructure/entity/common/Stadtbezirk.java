package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;

@Data
public class Stadtbezirk {

    private String nummer;

    private String name;

    private MultiPolygonGeometry multiPolygon;
}
