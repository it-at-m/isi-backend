package de.muenchen.isi.api.dto.common;

import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerortungDto {

    @NotEmpty
    private Set<@Valid StadtbezirkDto> stadtbezirke;

    @NotEmpty
    private Set<@Valid GemarkungDto> gemarkungen;

    @Valid
    @NotNull
    private MultiPolygonGeometryDto multiPolygon;

    @Valid
    private PointGeometryDto pointGeometry;
}
