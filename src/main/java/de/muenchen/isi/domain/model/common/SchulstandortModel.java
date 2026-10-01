package de.muenchen.isi.domain.model.common;

import lombok.Data;

@Data
public class SchulstandortModel {

    private Long schulnummer;

    private String schulname;

    private MultiPolygonGeometryModel multiPolygon;
}
