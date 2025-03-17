package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class SobonRelevantValueBridge implements ValueBridge<UncertainBoolean, String> {

    @Override
    public String toIndexedValue(final UncertainBoolean value, final ValueBridgeToIndexedValueContext context) {
        return value != null ? value.getBezeichnung() : null;
    }
}
