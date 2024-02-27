package de.muenchen.isi.infrastructure.entity.common;

import java.util.Set;
import lombok.Data;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Data
public abstract class Verortung {

    @IndexedEmbedded
    private Set<Stadtbezirk> stadtbezirke;

    private Set<Bezirksteil> bezirksteile;

    private Set<Viertel> viertel;

    @IndexedEmbedded
    private Set<Gemarkung> gemarkungen;

    private Set<Kitaplanungsbereich> kitaplanungsbereiche;

    private Set<Grundschulsprengel> grundschulsprengel;

    private Set<Mittelschulsprengel> mittelschulsprengel;
}
