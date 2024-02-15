package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;

@Data
public class Grundschulsprengel {

    private Long nummer;

    private MultiPolygonGeometry multiPolygon;
}
