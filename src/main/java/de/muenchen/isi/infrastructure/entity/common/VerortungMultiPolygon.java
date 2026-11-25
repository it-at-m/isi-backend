package de.muenchen.isi.infrastructure.entity.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Gebietszuordnung und Schwerpunkt einer Verortung mit eigenem Umgriff. Der Umgriff selbst liegt in {@link VerortungGeometrie}.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VerortungMultiPolygon extends Verortung {

    private PointGeometry centroid;

    private Utm centroidUtm;
}
