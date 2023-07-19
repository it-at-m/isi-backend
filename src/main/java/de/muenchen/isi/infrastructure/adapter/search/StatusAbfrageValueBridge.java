package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class StatusAbfrageValueBridge implements ValueBridge<StatusAbfrage, String> {

    @Override
    public String toIndexedValue(StatusAbfrage value, ValueBridgeToIndexedValueContext context) {
        return value.getBezeichnung();
    }
}
