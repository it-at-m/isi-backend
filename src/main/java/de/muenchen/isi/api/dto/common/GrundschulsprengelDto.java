package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GrundschulsprengelDto {

    private Long nummer;

    @Valid
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;
}
