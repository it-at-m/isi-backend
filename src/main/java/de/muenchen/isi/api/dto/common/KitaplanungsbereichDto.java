package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class KitaplanungsbereichDto {

    private Long kitaPlb;

    private String kitaPlbT;

    @Valid
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;
}
