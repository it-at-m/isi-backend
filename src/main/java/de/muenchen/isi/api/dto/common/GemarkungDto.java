package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class GemarkungDto {

    private BigDecimal nummer;

    private String name;

    @NotNull
    private Set<@Valid FlurstueckDto> flurstuecke;

    @Valid
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;
}
