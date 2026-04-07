package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.adapter.search.ArtAbfrageValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.ResultTypeValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StatusAbfrageSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.StatusAbfrageValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.entity.common.Bearbeitungshistorie;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ResultType;
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
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.extractor.mapping.annotation.ContainerExtract;
import org.hibernate.search.mapper.pojo.extractor.mapping.annotation.ContainerExtraction;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "artAbfrage")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(indexes = { @Index(name = "abfrage_name_index", columnList = "name") })
public abstract class Abfrage extends BaseEntity {

    /**
     * Virtuelles Feld für Hibernate Search / Elasticsearch, das den "Typ" des
     * indexierten Objekts festlegt.
     *
     * <p>
     * Obwohl es keine persistierte Spalte in der Datenbank gibt
     * ({@link Transient}), wird dieses Feld im Suchindex unter dem Namen
     * {@code resultType} gespeichert und ist in Projektionen verfügbar
     * ({@link GenericField} mit {@code projectable = YES}).
     * </p>
     *
     * <p>
     * Hintergrund: In einer gemeinsamen Index-Struktur werden unterschiedliche
     * Objekttypen (z. B. Bauvorhaben, Abfrage, Infrastruktureinrichtung)
     * zusammen gespeichert. Damit bei einer Suchanfrage bzw. einer Projection
     * zur Laufzeit unterschieden werden kann, von welchem Typ ein Treffer ist,
     * braucht Elasticsearch dieses Feld. In den Projection-Records (mit
     * {@code @ProjectionConstructor}) gibt es deshalb ein Attribut
     * {@code resultType}, das aus genau diesem Getter befüllt wird.
     * </p>
     *
     * <p>
     * {@link IndexingDependency} mit {@code reindexOnUpdate = NO} signalisiert,
     * dass sich der Wert nie ändert (er ist hier fest auf "ABFRAGE" gesetzt),
     * sodass Hibernate Search keine Neuindizierung bei Entity-Änderungen
     * auslösen muss.
     * </p>
     *
     * @return Der feste String "ABFRAGE", der diesen Objekttyp im Index markiert.
     */
    @Transient
    @GenericField(
        name = "resultType",
        projectable = Projectable.YES,
        valueBridge = @ValueBridgeRef(type = ResultTypeValueBridge.class)
    )
    @IndexingDependency(
        reindexOnUpdate = ReindexOnUpdate.NO,
        extraction = @ContainerExtraction(extract = ContainerExtract.NO)
    )
    public ResultType getResultType() {
        return ResultType.ABFRAGE;
    }

    @KeywordField(name = "name_sort", sortable = Sortable.YES, normalizer = "lowercase")
    @FullTextField
    @NonStandardField(
        name = "name" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column(nullable = false, length = 70)
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

    @Column(length = 2000)
    private String anmerkung;

    @ManyToOne
    private Bauvorhaben bauvorhaben;

    /**
     * Virtuelles Feld für Hibernate Search, das die UUID des verknüpften
     * {@code Bauvorhaben} im Suchindex ablegt. Es ist {@link Transient}, da
     * es nicht in der Datenbank gespeichert wird, sondern nur zur Indexierung
     * und Projektion dient. Über {@link GenericField} wird es als
     * {@code bauvorhabenId} im Index verfügbar gemacht und kann direkt in
     * Suchergebnissen zurückgegeben werden.
     *
     * {@link IndexingDependency} weist Hibernate Search an, dieses Feld von der
     * Beziehung {@code bauvorhaben} abzuleiten. Ändert sich das Bauvorhaben oder
     * dessen ID, wird das abgeleitete Feld automatisch im Index aktualisiert.
     */
    @Transient
    @GenericField(name = "bauvorhabenId", projectable = Projectable.YES)
    @IndexingDependency(derivedFrom = @ObjectPath(@PropertyValue(propertyName = "bauvorhaben")))
    public UUID getBauvorhabenUuid() {
        return bauvorhaben != null ? bauvorhaben.getId() : null;
    }

    @GenericField
    @Column(nullable = false)
    private String sub;

    /**
     * Diese Methode gibt den Wert der {@link DiscriminatorColumn} zurück.
     * Ist kein {@link DiscriminatorValue} gesetzt, so wird null zurückgegeben.
     *
     * @return Wert der {@link DiscriminatorColumn}.
     */
    @Transient
    @KeywordField(
        name = "artAbfrage",
        projectable = Projectable.YES,
        searchable = Searchable.NO,
        valueBridge = @ValueBridgeRef(type = ArtAbfrageValueBridge.class)
    )
    @IndexingDependency(
        reindexOnUpdate = ReindexOnUpdate.NO,
        extraction = @ContainerExtraction(extract = ContainerExtract.NO)
    )
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
