package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GemarkungModel extends BaseEntityModel {

    private BigDecimal nummer;

    private String name;

    private Set<FlurstueckModel> flurstuecke;

    private MultiPolygonGeometryModel multiPolygon;
}
