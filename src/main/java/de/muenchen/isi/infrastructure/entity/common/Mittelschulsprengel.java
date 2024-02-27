package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;

@Data
public class Mittelschulsprengel {

    private Long nummer;

    private MultiPolygonGeometry multiPolygon;
}
