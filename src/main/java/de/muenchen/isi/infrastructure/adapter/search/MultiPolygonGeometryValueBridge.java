package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.search.engine.backend.types.converter.ToDocumentFieldValueConverter;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class MultiPolygonGeometryValueBridge implements ValueBridge<MultiPolygonGeometry, String> {

    @Override
    public String toIndexedValue(final MultiPolygonGeometry value, final ValueBridgeToIndexedValueContext context) {
        if (value == null || value.getCoordinates() == null) {
            return null;
        }
        return value
            .getCoordinates()
            .stream()
            .map(polygon ->
                polygon
                    .stream()
                    .map(ring ->
                        ring
                            .stream()
                            .map(point -> point.stream().map(BigDecimal::toPlainString).collect(Collectors.joining(","))
                            )
                            .collect(Collectors.joining("|"))
                    )
                    .collect(Collectors.joining(";"))
            )
            .collect(Collectors.joining("/"));
    }
}
