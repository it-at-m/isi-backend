package de.muenchen.isi.api.dto.common;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;
}
