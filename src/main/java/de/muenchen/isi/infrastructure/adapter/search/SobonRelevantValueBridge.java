package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

public class SobonRelevantValueBridge implements ValueBridge<UncertainBoolean, String> {

    private static final String IS_SOBON_RELEVANT = "SoBoN-Relevant";

    private static final String IS_NOT_SOBON_RELEVANT = "nicht SoBoN-Relevant";

    @Override
    public String toIndexedValue(UncertainBoolean value, ValueBridgeToIndexedValueContext context) {
        String indexedValue = null;
        if (UncertainBoolean.TRUE.equals(value)) {
            indexedValue = IS_SOBON_RELEVANT;
        } else if (UncertainBoolean.FALSE.equals(value)) {
            indexedValue = IS_NOT_SOBON_RELEVANT;
        }
        return indexedValue;
    }
}
