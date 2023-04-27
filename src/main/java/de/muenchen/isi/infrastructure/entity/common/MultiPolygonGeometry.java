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
@TypeDef(name = "json", typeClass = JsonType.class)
public class MultiPolygonGeometry {

    @Column(nullable = false)
    private String type;

    @Type(type = "json")
    @Column(nullable = false, columnDefinition = "json")
    private List<List<List<List<BigDecimal>>>> coordinates;
}
