package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import lombok.Data;

@Data
public class KitaplanungsbereichModel {

    private Long kitaPlb;

    private String kitaPlbT;

    private MultiPolygonGeometry multiPolygon;
}
