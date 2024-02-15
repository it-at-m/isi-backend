package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class KitaplanungsbereichDto {

    private Long kitaPlb;

    private String kitaPlbT;

    @Valid
    @NotNull
    private MultiPolygonGeometry multiPolygon;
}
