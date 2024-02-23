package de.muenchen.isi.domain.model.common;

import lombok.Data;

@Data
public class GrundschulsprengelModel {

    private Long nummer;

    private MultiPolygonGeometryModel multiPolygon;
}
