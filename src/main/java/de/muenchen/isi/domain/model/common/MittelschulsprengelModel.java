package de.muenchen.isi.domain.model.common;

import lombok.Data;

@Data
public class MittelschulsprengelModel {

    private Long nummer;

    private MultiPolygonGeometryModel multiPolygon;
}
