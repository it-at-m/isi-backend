package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.adapter.search.StatusAbfrageSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.StatusAbfrageValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.Verortung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahrenBauleitplanverfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(indexes = { @Index(name = "name_index", columnList = "name") })
@Indexed
public class Bauleitplanverfahren extends BaseEntity {

    @KeywordField(name = "name_sort", sortable = Sortable.YES, normalizer = "lowercase")
    @FullTextField
    @NonStandardField(
        name = "name" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column(nullable = false, unique = true, length = 70)
    private String name;

    @FullTextField
    @NonStandardField(
        name = "bebauungsplannummer" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column(nullable = true)
    private String bebauungsplannummer;

    @ManyToOne
    private Bauvorhaben bauvorhaben;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) not null check (sobon_relevant != 'UNSPECIFIED')")
    private UncertainBoolean sobonRelevant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StandVerfahrenBauleitplanverfahren standVerfahren;

    @Column
    private String standVerfahrenFreieEingabe;

    @IndexedEmbedded
    @Embedded
    private Adresse adresse;

    @IndexedEmbedded
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Verortung verortung;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bauleitplanverfahren_id")
    private List<Dokument> dokumente;

    @Column(nullable = false)
    private LocalDate fristBearbeitung;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) not null check (sobon_relevant != 'UNSPECIFIED')")
    private UncertainBoolean offizielleMitzeichnung;

    @Column
    private String anmerkung;

    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "bauleitplanverfahren_abfragevarianten_id", referencedColumnName = "id")
    @OrderBy("abfragevariantenNr asc")
    private List<AbfragevarianteBauleitplanverfahren> abfragevarianten;

    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "bauleitplanverfahren_abfragevarianten_sachbearbeitung_id", referencedColumnName = "id")
    @OrderBy("abfragevariantenNr asc")
    private List<AbfragevarianteBauleitplanverfahren> abfragevariantenSachbearbeitung;

    @FullTextField(valueBridge = @ValueBridgeRef(type = StatusAbfrageValueBridge.class))
    @NonStandardField(
        name = "statusAbfrage" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StatusAbfrageSuggestionBinder.class)
    )
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAbfrage statusAbfrage;
}
