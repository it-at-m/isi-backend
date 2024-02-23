package de.muenchen.isi.domain.model.common;

import lombok.Data;

@Data
public class KitaplanungsbereichModel {

    private Long kitaPlb;

    private String kitaPlbT;

    private MultiPolygonGeometryModel multiPolygon;
}
