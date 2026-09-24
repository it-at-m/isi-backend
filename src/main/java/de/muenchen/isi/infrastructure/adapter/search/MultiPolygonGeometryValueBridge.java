package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * ValueBridge für Hibernate Search, um {@link MultiPolygonGeometry}-Objekte in den Suchindex
 * und wieder zurück zu konvertieren.
 *
 * Hibernate Search kann in Lucene/Elasticsearch nur einfache Typen wie String
 * oder Zahlen speichern. Komplexe Objekte wie {@link MultiPolygonGeometry} müssen daher
 * serialisiert werden.
 *
 * Diese Bridge:
 * - wandelt eine {@link MultiPolygonGeometry} beim Indizieren in einen JSON-String um
 *   ({@code toIndexedValue}),
 * - liest den JSON-String bei Projektionen wieder zurück in eine {@link MultiPolygonGeometry}
 *   ({@code fromIndexedValue}).
 *
 * Damit können {@link MultiPolygonGeometry}-Felder auch in Hibernate Search Projections
 * (z. B. @ProjectionConstructor-Records) korrekt befüllt werden.
 */
@Slf4j
public class MultiPolygonGeometryValueBridge implements ValueBridge<MultiPolygonGeometry, String> {

    private static final ObjectMapper objectMapper = new JsonMapper();

    @Override
    public String toIndexedValue(final MultiPolygonGeometry value, final ValueBridgeToIndexedValueContext context) {
        return toJson(value);
    }

    @Override
    public MultiPolygonGeometry fromIndexedValue(
        final String indexedValue,
        final ValueBridgeFromIndexedValueContext context
    ) {
        return fromJson(indexedValue);
    }

    public static String toJson(final MultiPolygonGeometry value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JacksonException e) {
            log.error("Fehler beim Serialisieren von MultiPolygonGeometry: {}", e.getMessage());
            return null;
        }
    }

    public static MultiPolygonGeometry fromJson(final String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, MultiPolygonGeometry.class);
        } catch (JacksonException e) {
            log.error("Fehler beim Deserialisieren von MultiPolygonGeometry: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isCompatibleWith(final ValueBridge<?, ?> other) {
        return other != null && getClass().equals(other.getClass());
    }

    @Override
    public boolean equals(Object o) {
        return o != null && this.getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
