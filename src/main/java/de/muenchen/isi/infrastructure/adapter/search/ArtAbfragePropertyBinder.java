package de.muenchen.isi.infrastructure.adapter.search;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.engine.backend.types.IndexFieldType;
import org.hibernate.search.mapper.pojo.bridge.PropertyBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.PropertyBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.PropertyBinder;
import org.hibernate.search.mapper.pojo.bridge.runtime.PropertyBridgeWriteContext;

public class ArtAbfragePropertyBinder implements PropertyBinder {

    @Override
    public void bind(PropertyBindingContext context) {
        context.dependencies().useRootOnly(); // Keine echte Property-Abhängigkeit

        IndexFieldType<String> fieldType = context.typeFactory().asString().toIndexFieldType();

        context.bridge(
            Object.class,
            new ArtAbfragePropertyBridge(context.indexSchemaElement().field("artAbfrage", fieldType).toReference())
        );
    }
}

class ArtAbfragePropertyBridge implements PropertyBridge<Object> {

    private final IndexFieldReference<String> fieldReference;

    public ArtAbfragePropertyBridge(IndexFieldReference<String> fieldReference) {
        this.fieldReference = fieldReference;
    }

    @Override
    public void write(DocumentElement target, Object bridgedElement, PropertyBridgeWriteContext context) {
        if (bridgedElement instanceof ArtAbfrage artAbfrage && artAbfrage != null) {
            target.addValue(fieldReference, artAbfrage.toString());
        }
    }
}
