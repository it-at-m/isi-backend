package de.muenchen.isi.infrastructure.entity.common;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ScaledNumberField;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MultiPolygonGeometry extends Geometry {

    private List<List<List<List<BigDecimal>>>> coordinates;
}
