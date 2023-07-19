package de.muenchen.isi.infrastructure.entity.common;

import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

@Data
public class Flurstueck {

    @FullTextField(analyzer = "entity_analyzer_string_field")
    private String nummer;

    private BigDecimal flaecheQm;

    private Long zaehler;

    private Long nenner;

    private Long eigentumsart;

    private String eigentumsartBedeutung;

    private BigDecimal gemarkungNummer;

    private MultiPolygonGeometry multiPolygon;
}
