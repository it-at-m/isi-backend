package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class BezirksteilDto {

    private String nummer;

    @Valid
    private MultiPolygonGeometryDto multiPolygon;
}
