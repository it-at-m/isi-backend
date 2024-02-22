package de.muenchen.isi.infrastructure.entity.common;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Viertel {

    private BigDecimal xCoordinate;

    private BigDecimal yCoordinate;

    private String viertelNummer;

    private BigDecimal flaecheQm;

    private MultiPolygonGeometry multiPolygon;
}
