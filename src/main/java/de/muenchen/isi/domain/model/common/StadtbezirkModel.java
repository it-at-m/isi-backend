package de.muenchen.isi.domain.model.common;

import lombok.Data;

@Data
public class StadtbezirkModel {

    private String nummer;

    private String name;

    private MultiPolygonGeometryModel multiPolygon;
}
