package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Indexed
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungMultiPolygon extends Verortung {

    @IndexedEmbedded(structure = ObjectStructure.NESTED)
    private MultiPolygonGeometry multiPolygon;

    private PointGeometry centroid;

    private Utm centroidUtm;
}
