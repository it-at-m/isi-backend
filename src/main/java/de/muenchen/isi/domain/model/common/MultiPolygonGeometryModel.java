package de.muenchen.isi.domain.model.common;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MultiPolygonGeometryModel extends GeometryModel {

    private List<List<List<List<BigDecimal>>>> coordinates;
}
