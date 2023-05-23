package de.muenchen.isi.infrastructure.entity.common;

import java.util.Set;
import lombok.Data;

@Data
public class Verortung {

    private Set<Stadtbezirk> stadtbezirke;

    private Set<Gemarkung> gemarkungen;

    private MultiPolygonGeometry multiPolygon;
}
