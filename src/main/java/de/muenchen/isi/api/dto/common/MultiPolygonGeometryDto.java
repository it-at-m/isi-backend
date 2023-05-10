package de.muenchen.isi.api.dto.common;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MultiPolygonGeometryDto extends GeometryDto {

    @NotEmpty
    private List<List<List<List<BigDecimal>>>> coordinates;
}
