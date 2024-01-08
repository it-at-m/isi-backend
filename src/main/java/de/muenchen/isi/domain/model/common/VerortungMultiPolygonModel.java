package de.muenchen.isi.domain.model.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungMultiPolygonModel extends VerortungModel {

    private MultiPolygonGeometryModel multiPolygon;
}
