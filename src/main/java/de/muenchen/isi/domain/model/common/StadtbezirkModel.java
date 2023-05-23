package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.domain.model.BaseEntityModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StadtbezirkModel extends BaseEntityModel {

    private String nummer;

    private String name;

    private MultiPolygonGeometryModel multiPolygon;
}
