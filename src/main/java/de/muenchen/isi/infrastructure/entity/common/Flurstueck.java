package de.muenchen.isi.infrastructure.entity.common;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Flurstueck {

    private String nummer;

    private BigDecimal flaecheQm;

    private Long zaehler;

    private Long nenner;

    private Long eigentumsart;

    private String eigentumsartBedeutung;

    private BigDecimal gemarkungNummer;

    private MultiPolygonGeometry multiPolygon;
}
