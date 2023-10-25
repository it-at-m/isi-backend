package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um Entitätsattribute vom Typ {@link StatusInfrastruktureinrichtung} für eine Volltextsuche indizieren zu können.
 * Die zu indizierenden Werte werden aus dem Typattribut {@link StatusInfrastruktureinrichtung#getBezeichnung()} entnommen.
 */
public class StatusInfrastruktureinrichtungValueBridge implements ValueBridge<StatusInfrastruktureinrichtung, String> {

    @Override
    public String toIndexedValue(
        final StatusInfrastruktureinrichtung value,
        final ValueBridgeToIndexedValueContext context
    ) {
        return value != null ? value.getBezeichnung() : null;
    }
}
