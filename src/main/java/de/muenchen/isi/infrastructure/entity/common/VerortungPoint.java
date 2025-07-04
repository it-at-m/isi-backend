package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungPoint extends Verortung {

    @IndexedEmbedded(structure = ObjectStructure.NESTED)
    private PointGeometry point;

    private Utm pointUtm;
}
