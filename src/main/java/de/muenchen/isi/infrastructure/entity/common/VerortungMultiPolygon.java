package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Indexed
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungMultiPolygon extends Verortung {

    private MultiPolygonGeometry multiPolygon;

    private PointGeometry centroid;

    private Utm centroidUtm;
}
