package de.muenchen.isi.domain.model.common;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class GemarkungModel {

    private BigDecimal nummer;

    private String name;

    private Set<FlurstueckModel> flurstuecke;

    private MultiPolygonGeometryModel multiPolygon;
}
