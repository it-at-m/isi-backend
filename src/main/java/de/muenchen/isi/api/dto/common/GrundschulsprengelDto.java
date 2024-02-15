package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GrundschulsprengelDto {

    private Long nummer;

    @Valid
    @NotNull
    private MultiPolygonGeometry multiPolygon;
}
