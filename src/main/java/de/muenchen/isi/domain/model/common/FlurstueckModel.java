package de.muenchen.isi.domain.model.common;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class FlurstueckModel {

    private String nummer;

    private BigDecimal flaecheQm;

    private Long zaehler;

    private Long nenner;

    private Long eigentumsart;

    private String eigentumsartBedeutung;

    private BigDecimal gemarkungNummer;

    private MultiPolygonGeometryModel multiPolygon;
}
