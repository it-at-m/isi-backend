package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um Entitätsattribute vom Typ {@link StandVorhaben} für eine Volltextsuche indizieren zu können.
 * Die zu indizierenden Werte werden aus dem Typattribut {@link StandVorhaben#getBezeichnung()} entnommen.
 */
public class StandVorhabenValueBridge implements ValueBridge<StandVorhaben, String> {

    @Override
    public String toIndexedValue(final StandVorhaben value, final ValueBridgeToIndexedValueContext context) {
        return value != null ? value.getBezeichnung() : null;
    }
}
