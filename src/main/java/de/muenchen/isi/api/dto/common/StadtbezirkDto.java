package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class StadtbezirkDto {

    private String nummer;

    private String name;

    @Valid
    private MultiPolygonGeometryDto multiPolygon;
}
