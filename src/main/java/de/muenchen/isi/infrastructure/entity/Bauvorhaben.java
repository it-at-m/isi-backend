package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.adapter.listener.BauvorhabenListener;
import de.muenchen.isi.infrastructure.adapter.search.MultiPolygonGeometryValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.SobonRelevantValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StandVerfahrenSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.BearbeitendePerson;
import de.muenchen.isi.infrastructure.entity.common.MultiPolygonGeometry;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import de.muenchen.isi.infrastructure.entity.enums.EntityType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtBaulicheNutzung;
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
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;
import org.hibernate.type.SqlTypes;

@Entity
@EntityListeners({ BauvorhabenListener.class })
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(indexes = { @Index(name = "bauvorhaben_name_index", columnList = "nameVorhaben") })
@Indexed
public class Bauvorhaben extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @GenericField
    private EntityType entityType = EntityType.BAUVORHABEN;

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
    @GenericField
    private BigDecimal grundstuecksgroesse;

    @NonStandardField(
        name = "stand_verfahren_filter" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StandVerfahrenSuggestionBinder.class)
    )
    @GenericField(name = "stand_verfahren_filter")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StandVerfahren standVerfahren;

    @Column(length = 1000)
    private String standVerfahrenFreieEingabe;

    @Column
    @GenericField
    @AttributeOverrides(
        {
            @AttributeOverride(name = "latitude", column = @Column(name = "search_result_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "search_result_longitude")),
        }
    )
    private Wgs84 bauvorhabenCoordinate;

    @GenericField(valueBridge = @ValueBridgeRef(type = MultiPolygonGeometryValueBridge.class))
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private MultiPolygonGeometry umgriff;

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

    @IndexedEmbedded
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private VerortungMultiPolygon verortung;

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

    @FullTextField(valueBridge = @ValueBridgeRef(type = SobonRelevantValueBridge.class))
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
