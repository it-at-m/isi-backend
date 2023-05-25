package de.muenchen.isi.domain.model.common;

import java.util.Set;
import lombok.Data;

@Data
public class VerortungModel {

    private Set<StadtbezirkModel> stadtbezirke;

    private Set<GemarkungModel> gemarkungen;

    private MultiPolygonGeometryModel multiPolygon;
}
