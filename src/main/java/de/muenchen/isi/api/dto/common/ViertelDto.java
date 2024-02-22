package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ViertelDto {

    private BigDecimal xCoordinate;

    private BigDecimal yCoordinate;

    private String viertelNummer;

    private BigDecimal flaecheQm;

    @Valid
    @NotNull
    private MultiPolygonGeometry multiPolygon;
}
