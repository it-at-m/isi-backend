package de.muenchen.isi.infrastructure.entity.common;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PointGeometry extends Geometry {

    private List<BigDecimal> coordinates;
}
