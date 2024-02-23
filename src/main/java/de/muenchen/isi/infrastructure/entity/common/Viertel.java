package de.muenchen.isi.infrastructure.entity.common;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Viertel {

    private String nummer;

    private BigDecimal flaecheQm;

    private MultiPolygonGeometry multiPolygon;
}
