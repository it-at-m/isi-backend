package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;

@Data
public class Bezirksteil {

    private String nummer;

    private MultiPolygonGeometry multiPolygon;
}
