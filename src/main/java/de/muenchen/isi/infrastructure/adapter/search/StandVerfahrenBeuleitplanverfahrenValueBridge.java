package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahrenBauleitplanverfahren;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um Entitätsattribute vom Typ {@link StandVerfahrenBauleitplanverfahren} für eine Volltextsuche indizieren zu können.
 * Die zu indizierenden Werte werden aus dem Typattribut {@link StandVerfahrenBauleitplanverfahren#getBezeichnung()} entnommen.
 */
public class StandVerfahrenBeuleitplanverfahrenValueBridge
    implements ValueBridge<StandVerfahrenBauleitplanverfahren, String> {

    @Override
    public String toIndexedValue(
        final StandVerfahrenBauleitplanverfahren value,
        final ValueBridgeToIndexedValueContext context
    ) {
        return value != null ? value.getBezeichnung() : null;
    }
}
