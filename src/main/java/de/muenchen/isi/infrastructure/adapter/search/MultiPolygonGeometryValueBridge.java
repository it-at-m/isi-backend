package de.muenchen.isi.infrastructure.adapter.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeFromIndexedValueContext;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class MultiPolygonGeometryValueBridge implements ValueBridge<MultiPolygonGeometry, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String toIndexedValue(MultiPolygonGeometry value, ValueBridgeToIndexedValueContext context) {
        if (value == null) {
            return null;
        }
        try {
            // Konvertiert die Koordinaten (als verschachtelte Listen) in einen JSON-String
            return OBJECT_MAPPER.writeValueAsString(value.getCoordinates());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Fehler beim Konvertieren der MultiPolygonGeometry in JSON", e);
        }
    }

    @Override
    public MultiPolygonGeometry fromIndexedValue(String value, ValueBridgeFromIndexedValueContext context) {
        if (value == null) {
            return null;
        }
        try {
            // Liest den JSON-String und wandelt ihn in die entsprechende Struktur um
            List<List<List<List<BigDecimal>>>> coordinates = OBJECT_MAPPER.readValue(
                value,
                new TypeReference<List<List<List<List<BigDecimal>>>>>() {}
            );
            MultiPolygonGeometry geometry = new MultiPolygonGeometry();
            geometry.setCoordinates(coordinates);
            return geometry;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Fehler beim Konvertieren von JSON in MultiPolygonGeometry", e);
        }
    }
}
