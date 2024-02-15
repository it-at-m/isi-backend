package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import lombok.Data;

@Data
public class MittelschulsprengelModel {

    private Long nummer;

    private MultiPolygonGeometry multiPolygon;
}
