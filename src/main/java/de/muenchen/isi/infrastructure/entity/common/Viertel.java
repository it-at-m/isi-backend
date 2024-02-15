package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;

@Data
public class Viertel {

    private String nummer;

    private MultiPolygonGeometry multiPolygon;
}
