package de.muenchen.isi.infrastructure.entity.common;

import java.util.Set;
import lombok.Data;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Data
public class Verortung {

    @IndexedEmbedded
    private Set<Stadtbezirk> stadtbezirke;

    @IndexedEmbedded
    private Set<Gemarkung> gemarkungen;

    private MultiPolygonGeometry multiPolygon;
}
