package de.muenchen.isi.domain.model.common;

import lombok.Data;

@Data
public class BezirksteilModel {

    private String nummer;

    private MultiPolygonGeometryModel multiPolygon;
}
