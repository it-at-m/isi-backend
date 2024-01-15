package de.muenchen.isi.api.dto.common;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public abstract class GeometryDto {

    @NotEmpty
    private String type;
}
