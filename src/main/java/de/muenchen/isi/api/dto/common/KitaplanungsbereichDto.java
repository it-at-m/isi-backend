package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class KitaplanungsbereichDto {

    private Long kitaPlb;

    private String kitaPlbT;

    @Valid
    private MultiPolygonGeometryDto multiPolygon;
}
