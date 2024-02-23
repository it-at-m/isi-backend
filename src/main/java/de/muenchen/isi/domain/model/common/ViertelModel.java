package de.muenchen.isi.domain.model.common;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ViertelModel {

    private String nummer;

    private BigDecimal flaecheQm;

    private MultiPolygonGeometryModel multiPolygon;
}
