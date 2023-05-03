package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.api.dto.BaseEntityDto;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FlurstueckDto extends BaseEntityDto {

    private String nummer;

    private BigDecimal flaecheQm;

    private Long zaehler;

    private Long nenner;

    private Long eigentumsart;

    private String eigentumsartBedeutung;

    private BigDecimal gemarkungNummer;

    private MultiPolygonGeometryDto multiPolygon;
}
