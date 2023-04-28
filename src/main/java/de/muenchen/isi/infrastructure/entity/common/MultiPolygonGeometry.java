package de.muenchen.isi.infrastructure.entity.common;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Embeddable
@Data
@TypeDef(name = "jsonb", typeClass = JsonType.class)
public class MultiPolygonGeometry extends Geometry {

    @Type(type = "jsonb")
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<List<List<List<BigDecimal>>>> coordinates;
}
