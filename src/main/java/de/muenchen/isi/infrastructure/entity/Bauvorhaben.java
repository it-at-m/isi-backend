package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.adapter.search.StandVorhabenSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.StandVorhabenValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.Verortung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.BaugebietArt;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Planungsrecht;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(indexes = { @Index(name = "name_vorhaben_index", columnList = "nameVorhaben") })
@TypeDef(name = "json", typeClass = JsonType.class)
@Indexed
public class Bauvorhaben extends BaseEntity {

    /**
     * Einheitlicher indexierter Name des sortierbaren Attributs,
     * zur einheitlichen Sortierung der entitätsübergreifenden Suchergebnisse.
     */
    @KeywordField(name = "name_sort", sortable = Sortable.YES)
    @FullTextField
    @NonStandardField(
        name = "nameVorhaben" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column(nullable = false, unique = true)
    private String nameVorhaben;

    @Column(nullable = false)
    private String eigentuemer;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal grundstuecksgroesse;

    @FullTextField(valueBridge = @ValueBridgeRef(type = StandVorhabenValueBridge.class))
    @NonStandardField(
        name = "standVorhaben" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StandVorhabenSuggestionBinder.class)
    )
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StandVorhaben standVorhaben;

    @FullTextField
    @NonStandardField(
        name = "bauvorhabenNummer" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column(nullable = false)
    private String bauvorhabenNummer;

    @IndexedEmbedded
    @Embedded
    private Adresse adresse;

    @IndexedEmbedded
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Verortung verortung;

    @Column(nullable = true)
    private String allgemeineOrtsangabe;

    @FullTextField
    @NonStandardField(
        name = "bebauungsplannummer" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column(nullable = true)
    private String bebauungsplannummer;

    @Column(nullable = true)
    private String fisNummer;

    @Column(nullable = true)
    private String anmerkung;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) not null check (sobon_relevant != 'UNSPECIFIED')")
    private UncertainBoolean sobonRelevant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Planungsrecht planungsrecht;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<BaugebietArt> artFnp;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bauvorhaben_id")
    private List<Dokument> dokumente;
}
