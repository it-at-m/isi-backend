package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ViertelModel {

    private BigDecimal xCoordinate;

    private BigDecimal yCoordinate;

    private String viertelNummer;

    private BigDecimal flaecheQm;

    private MultiPolygonGeometry multiPolygon;
}
