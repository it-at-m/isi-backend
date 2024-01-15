package de.muenchen.isi.api.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PointGeometryDto extends GeometryDto {

    @Schema(description = "Die Punktkoordinate ([x,y] bzw. [longitude, latitude]) im Standard EPSG:4326 (WGS84).")
    @NotEmpty
    @Size(
        min = 2,
        max = 2,
        message = "Die durch die Liste repräsentierte Koordinate muss der Länge 2 entsprechen und somit einen X und einen Y-Abschnitt besitzen."
    )
    private List<BigDecimal> coordinates;
}
