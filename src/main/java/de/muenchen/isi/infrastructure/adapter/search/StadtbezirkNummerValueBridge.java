package de.muenchen.isi.infrastructure.adapter.search;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.search.mapper.pojo.bridge.ValueBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.ValueBridgeToIndexedValueContext;

/**
 * ValueBridge um die Nummer des Stadtbezirks (z.B. 01 oder 12) für eine Volltextsuche indizieren zu können.
 *
 * Die ValueBridge entfernt die führenden "0" bei den zu indizierenden Stadtbezirken.
 */
public class StadtbezirkNummerValueBridge implements ValueBridge<String, String> {

    @Override
    public String toIndexedValue(final String value, final ValueBridgeToIndexedValueContext context) {
        return StringUtils.isNotEmpty(value) ? Integer.valueOf(NumberUtils.toInt(value)).toString() : null;
    }
}
