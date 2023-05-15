package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.domain.model.BaseEntityModel;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungModel extends BaseEntityModel {

    private Set<StadtbezirkModel> stadtbezirke;

    private Set<GemarkungModel> gemarkungen;

    private MultiPolygonGeometryModel multiPolygon;
}
