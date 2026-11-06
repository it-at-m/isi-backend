package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ViertelDto {

    private String nummer;

    private BigDecimal flaecheQm;

    @Valid
    private MultiPolygonGeometryDto multiPolygon;
}
