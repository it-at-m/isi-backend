package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class MittelschulsprengelDto {

    private Long nummer;

    @Valid
    private MultiPolygonGeometryDto multiPolygon;
}
