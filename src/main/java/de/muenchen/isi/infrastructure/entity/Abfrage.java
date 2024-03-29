package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.adapter.search.StatusAbfrageSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.StatusAbfrageValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.entity.common.Bearbeitungshistorie;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "artAbfrage")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(indexes = { @Index(name = "abfrage_name_index", columnList = "name") })
public abstract class Abfrage extends BaseEntity {

    @KeywordField(name = "name_sort", sortable = Sortable.YES, normalizer = "lowercase")
    @FullTextField
    @NonStandardField(
        name = "name" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column(nullable = false, unique = true, length = 70)
    private String name;

    @GenericField(name = "statusAbfrage_filter")
    @FullTextField(valueBridge = @ValueBridgeRef(type = StatusAbfrageValueBridge.class))
    @NonStandardField(
        name = "statusAbfrage" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StatusAbfrageSuggestionBinder.class)
    )
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAbfrage statusAbfrage;

    @Column(length = 1000)
    private String anmerkung;

    @ManyToOne
    private Bauvorhaben bauvorhaben;

    @Column(nullable = false)
    private String sub;

    /**
     * Diese Methode gibt den Wert der {@link DiscriminatorColumn} zurück.
     * Ist kein {@link DiscriminatorValue} gesetzt, so wird null zurückgegeben.
     *
     * @return Wert der {@link DiscriminatorColumn}.
     */
    @Transient
    public ArtAbfrage getArtAbfrage() {
        final var discriminatorValue = this.getClass().getAnnotation(DiscriminatorValue.class);
        return ObjectUtils.isEmpty(discriminatorValue)
            ? null
            : EnumUtils.getEnum(ArtAbfrage.class, discriminatorValue.value());
    }

    @Column(length = 8000)
    private String linkEakte;

    @ElementCollection
    @CollectionTable(
        indexes = { @Index(name = "abfrage_bearbeitungshistorie_abfrage_id_index", columnList = "abfrage_id") }
    )
    @OrderBy("zeitpunkt asc")
    private List<Bearbeitungshistorie> bearbeitungshistorie = new ArrayList<>();
}
