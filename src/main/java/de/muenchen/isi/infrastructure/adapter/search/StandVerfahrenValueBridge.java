package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um Entitätsattribute vom Typ {@link StandVerfahren} für eine Volltextsuche indizieren zu können.
 * Die zu indizierenden Werte werden aus dem Typattribut {@link StandVerfahren#getBezeichnung()} entnommen.
 */
public class StandVerfahrenValueBridge implements ValueBridge<StandVerfahren, String> {

    @Override
    public String toIndexedValue(final StandVerfahren value, final ValueBridgeToIndexedValueContext context) {
        return value != null ? value.getBezeichnung() : null;
    }
}
