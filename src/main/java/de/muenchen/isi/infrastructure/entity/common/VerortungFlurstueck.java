package de.muenchen.isi.infrastructure.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.MultiPolygon;

/**
 * Polygon eines Flurstücks. Die Sachdaten des Flurstücks stehen in der jsonb-Spalte {@code verortung}.
 */
@Embeddable
@Getter
@Setter
public class VerortungFlurstueck {

    @Column
    private BigDecimal gemarkungNummer;

    @Column
    private String nummer;

    @Column(columnDefinition = "geometry(MultiPolygon,4326)", nullable = false)
    private MultiPolygon geometrie;
}
