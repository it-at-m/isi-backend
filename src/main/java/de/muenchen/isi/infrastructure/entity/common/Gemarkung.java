package de.muenchen.isi.infrastructure.entity.common;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class Gemarkung {

    private BigDecimal nummer;

    private String name;

    private Set<Flurstueck> flurstuecke;

    private MultiPolygonGeometry multiPolygon;
}
