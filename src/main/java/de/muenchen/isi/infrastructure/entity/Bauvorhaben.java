package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.adapter.listener.BauvorhabenListener;
import de.muenchen.isi.infrastructure.adapter.search.AdresseValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.MultiPolygonGeometryValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.ResultTypeValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StandVerfahrenSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.StandVerfahrenValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.VerortungMultiPolygonValueBridge;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.BearbeitendePerson;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ResultType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.enums.lookup.WesentlicheRechtsgrundlage;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
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
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;
import org.hibernate.type.SqlTypes;

@Slf4j
@Entity
@EntityListeners({ BauvorhabenListener.class })
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(indexes = { @Index(name = "bauvorhaben_name_index", columnList = "nameVorhaben") })
@Indexed
public class Bauvorhaben extends BaseEntity {

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
     * @return Der feste String "BAUVORHABEN", der diesen Objekttyp im Index markiert.
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
        return ResultType.BAUVORHABEN;
    }

    @Embedded
    @AttributeOverrides(
        {
            @AttributeOverride(name = "name", column = @Column(name = "bearbeitende_person_name")),
            @AttributeOverride(name = "email", column = @Column(name = "bearbeitende_person_email")),
            @AttributeOverride(
                name = "organisationseinheit",
                column = @Column(name = "bearbeitende_person_organisationseinheit")
            ),
        }
    )
    private BearbeitendePerson bearbeitendePerson;

    /**
     * Einheitlicher indexiertes sortierbares Namensattributs
     * zur einheitlichen entitätsübergreifenden Sortierung der Suchergebnisse.
     */
    @KeywordField(name = "name_sort", sortable = Sortable.YES, normalizer = "lowercase")
    @FullTextField
    @NonStandardField(
        name = "nameVorhaben" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column(nullable = false)
    private String nameVorhaben;

    @Column(precision = 10, scale = 2)
    @GenericField(projectable = Projectable.YES)
    private BigDecimal grundstuecksgroesse;

    @FullTextField(valueBridge = @ValueBridgeRef(type = StandVerfahrenValueBridge.class))
    @NonStandardField(
        name = "standVerfahren" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StandVerfahrenSuggestionBinder.class)
    )
    @GenericField(name = "stand_verfahren_filter")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StandVerfahren standVerfahren;

    @Column(length = 1000)
    private String standVerfahrenFreieEingabe;

    @FullTextField
    @NonStandardField(
        name = "bauvorhabenNummer" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column
    private String bauvorhabenNummer;

    @IndexedEmbedded
    @Embedded
    private Adresse adresse;

    /**
     * Technisches, abgeleitetes Feld zur Indexierung der {@code verortung}-Eigenschaft.
     * <p>
     * Es wird nicht in der Datenbank gespeichert ({@link Transient}), sondern dient nur
     * als Bridge, um die Geometrie {@link Adresse} über die
     * {@link AdresseValueBridge} als JSON-String in das Suchindex-Feld
     * {@code verortungJson} zu serialisieren.
     * <p>
     * Dadurch kann die Verortung im Index projiziert werden ({@code Projectable.YES}),
     * ist aber nicht durchsuchbar ({@code Searchable.NO}).
     * <p>
     * Die Annotation {@link IndexingDependency} stellt sicher, dass dieses Feld
     * automatisch neu berechnet und reindexiert wird, wenn sich die Eigenschaft
     * {@code verortung} ändert.
     */
    @Transient
    @KeywordField(
        name = "adresseJson",
        projectable = Projectable.YES,
        searchable = Searchable.NO,
        valueBridge = @ValueBridgeRef(type = AdresseValueBridge.class)
    )
    @IndexingDependency(derivedFrom = { @ObjectPath(@PropertyValue(propertyName = "adresse")) })
    public Adresse getAdresseJson() {
        return this.adresse;
    }

    @IndexedEmbedded
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private VerortungMultiPolygon verortung;

    /**
     * Technisches, abgeleitetes Feld zur Indexierung der {@code verortung}-Eigenschaft.
     * <p>
     * Es wird nicht in der Datenbank gespeichert ({@link Transient}), sondern dient nur
     * als Bridge, um die Geometrie {@link VerortungMultiPolygon} über die
     * {@link VerortungMultiPolygonValueBridge} als JSON-String in das Suchindex-Feld
     * {@code verortungJson} zu serialisieren.
     * <p>
     * Dadurch kann die Verortung im Index projiziert werden ({@code Projectable.YES}),
     * ist aber nicht durchsuchbar ({@code Searchable.NO}).
     * <p>
     * Die Annotation {@link IndexingDependency} stellt sicher, dass dieses Feld
     * automatisch neu berechnet und reindexiert wird, wenn sich die Eigenschaft
     * {@code verortung} ändert.
     */
    @Transient
    @KeywordField(
        name = "verortungJson",
        projectable = Projectable.YES,
        searchable = Searchable.NO,
        valueBridge = @ValueBridgeRef(type = VerortungMultiPolygonValueBridge.class)
    )
    @IndexingDependency(derivedFrom = { @ObjectPath(@PropertyValue(propertyName = "verortung")) })
    public VerortungMultiPolygon getVerortungJson() {
        return this.verortung;
    }

    @FullTextField
    @NonStandardField(
        name = "bebauungsplannummer" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column
    private String bebauungsplannummer;

    @Column
    private String fisNummer;

    @Column(length = 1000)
    private String anmerkung;

    @GenericField(name = "sobon_relevant_filter")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) not null check (sobon_relevant != 'UNSPECIFIED')")
    private UncertainBoolean sobonRelevant;

    @Enumerated(EnumType.STRING)
    @Column
    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<WesentlicheRechtsgrundlage> wesentlicheRechtsgrundlage;

    @Column(length = 1000)
    private String wesentlicheRechtsgrundlageFreieEingabe;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<ArtBaulicheNutzung> artFnp;

    @Column(length = 1000)
    private String artFnpFreieEingabe;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bauvorhaben_id")
    private List<Dokument> dokumente;

    @OneToOne
    @JoinColumn(name = "relevante_abfragevariante_id")
    private Abfragevariante relevanteAbfragevariante;
}
