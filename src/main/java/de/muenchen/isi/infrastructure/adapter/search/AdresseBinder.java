package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.common.Adresse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.engine.backend.types.IndexFieldType;
import org.hibernate.search.mapper.pojo.bridge.TypeBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.TypeBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.TypeBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.TypeBridgeWriteContext;

public class AdresseBinder implements TypeBinder {

    @Override
    public void bind(TypeBindingContext context) {
        context.dependencies().use("strasse").use("hausnummer");

        final IndexFieldType<String> strasseHausnummerType = context
            .typeFactory()
            .asString()
            .analyzer("entity_analyzer_string_field")
            .toIndexFieldType();

        final IndexFieldReference<String> strasseHausnummerField = context
            .indexSchemaElement()
            .field("strasseHausnummer", strasseHausnummerType)
            .toReference();

        context.bridge(Adresse.class, new AdresseBridge(strasseHausnummerField));
    }

    @RequiredArgsConstructor
    private static class AdresseBridge implements TypeBridge<Adresse> {

        private final IndexFieldReference<String> strasseHausnummerField;

        @Override
        public void write(DocumentElement target, Adresse adresse, TypeBridgeWriteContext context) {
            if (ObjectUtils.isNotEmpty(adresse) && ObjectUtils.isNotEmpty(adresse.getStrasse())) {
                final String strasseHausnummer =
                    adresse.getStrasse() +
                    (
                        ObjectUtils.isNotEmpty(adresse.getHausnummer())
                            ? StringUtils.SPACE + adresse.getHausnummer()
                            : ""
                    );
                target.addValue(this.strasseHausnummerField, strasseHausnummer);
            }
        }
    }
}
