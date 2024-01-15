package de.muenchen.isi.api.dto.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungMultiPolygonDto extends VerortungDto {

    @Valid
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;
}
