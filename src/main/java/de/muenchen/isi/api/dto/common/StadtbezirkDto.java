package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StadtbezirkDto {

    private String nummer;

    private String name;

    @Valid
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;
}
