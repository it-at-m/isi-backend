package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class FlurstueckDto {

    private String nummer;

    private BigDecimal flaecheQm;

    private Long zaehler;

    private Long nenner;

    private Long eigentumsart;

    private String eigentumsartBedeutung;

    private BigDecimal gemarkungNummer;

    @Valid
    private MultiPolygonGeometryDto multiPolygon;
}
