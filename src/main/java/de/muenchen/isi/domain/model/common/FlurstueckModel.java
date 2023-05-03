package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FlurstueckModel extends BaseEntityModel {

    private String nummer;

    private BigDecimal flaecheQm;

    private Long zaehler;

    private Long nenner;

    private Long eigentumsart;

    private String eigentumsartBedeutung;

    private BigDecimal gemarkungNummer;

    private MultiPolygonGeometryModel multiPolygon;
}
