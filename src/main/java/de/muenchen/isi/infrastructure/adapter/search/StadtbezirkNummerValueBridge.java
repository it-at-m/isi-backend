package de.muenchen.isi.infrastructure.adapter.search;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um die Nummer des Stadtbezirks (z.B. 01 oder 12) ohne führende "0"en indizieren zu können.
 */
public class StadtbezirkNummerValueBridge implements ValueBridge<String, String> {

    @Override
    public String toIndexedValue(final String value, final ValueBridgeToIndexedValueContext context) {
        return StringUtils.isNotEmpty(value) ? Integer.valueOf(NumberUtils.toInt(value)).toString() : null;
    }

    @Override
    public boolean isCompatibleWith(final ValueBridge<?, ?> other) {
        return other == this || getClass().equals(other.getClass());
    }
}
