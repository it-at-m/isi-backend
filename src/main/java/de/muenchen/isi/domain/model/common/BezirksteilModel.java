package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import lombok.Data;

@Data
public class BezirksteilModel {

    private String nummer;

    private MultiPolygonGeometry multiPolygon;
}
