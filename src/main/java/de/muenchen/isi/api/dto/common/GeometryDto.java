package de.muenchen.isi.api.dto.common;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public abstract class GeometryDto {

    @NotEmpty
    private String type;
}
