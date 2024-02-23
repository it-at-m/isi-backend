package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ViertelDto {

    private String nummer;

    private BigDecimal flaecheQm;

    @Valid
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;
}
