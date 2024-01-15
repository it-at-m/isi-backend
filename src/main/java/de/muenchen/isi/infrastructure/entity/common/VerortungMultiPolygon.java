package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungMultiPolygon extends Verortung {

    private MultiPolygonGeometry multiPolygon;
}
