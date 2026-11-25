package de.muenchen.isi.infrastructure.entity.common;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.MultiPolygon;

/**
 * Eigene Geometrie einer Verortung: der Umgriff und die Polygone der Flurstücke.
 * <p>
 * Die Geometrie wird bewusst getrennt von der Gebietszuordnung in der jsonb-Spalte {@code verortung} gespeichert und
 * ist keine JPA-Relation des Eigentümers. So laden Statuswechsel, Berechnung und Suche keine Polygone.
 * Die ID entspricht der ID des Eigentümers (Bauvorhaben oder Abfrage). Beim Löschen des Eigentümers räumt ein Trigger auf.
 */
@Entity
@Getter
@Setter
public class VerortungGeometrie {

    @Id
    @Column(length = 36)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(columnDefinition = "geometry(MultiPolygon,4326)", nullable = false)
    private MultiPolygon umgriff;

    /**
     * Hash über Umgriff und Flurstücke. Solange er gleich bleibt, wird die Geometrie nicht neu geschrieben.
     */
    @Column(length = 64)
    private String geometrieHash;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "verortung_flurstueck", joinColumns = @JoinColumn(name = "verortung_geometrie_id"))
    private List<VerortungFlurstueck> flurstuecke = new ArrayList<>();
}
