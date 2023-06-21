package de.muenchen.isi.api.dto.common;

import java.math.BigDecimal;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
