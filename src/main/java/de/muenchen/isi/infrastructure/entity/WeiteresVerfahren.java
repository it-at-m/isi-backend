package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.adapter.search.StandVerfahrenSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.StandVerfahrenValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;
import org.hibernate.type.SqlTypes;

@Entity
@DiscriminatorValue(ArtAbfrage.Values.WEITERES_VERFAHREN)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Indexed
public class WeiteresVerfahren extends Abfrage {

    @Column
    private String aktenzeichenProLbk;

    @FullTextField
    @NonStandardField(
        name = "bebauungsplannummer" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column
    private String bebauungsplannummer;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) not null check (sobon_relevant != 'UNSPECIFIED')")
    private UncertainBoolean sobonRelevant;

    @Enumerated(EnumType.STRING)
    @Column
    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @FullTextField(valueBridge = @ValueBridgeRef(type = StandVerfahrenValueBridge.class))
    @NonStandardField(
        name = "standVerfahren" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StandVerfahrenSuggestionBinder.class)
    )
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StandVerfahren standVerfahren;

    @Column
    private String standVerfahrenFreieEingabe;

    @IndexedEmbedded
    @Embedded
    private Adresse adresse;

    @IndexedEmbedded
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private VerortungMultiPolygon verortung;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "weiteres_verfahren_id")
    private List<Dokument> dokumente;

    @Column(nullable = false)
    private LocalDate fristBearbeitung;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) not null check (sobon_relevant != 'UNSPECIFIED')")
    private UncertainBoolean offizielleMitzeichnung;

    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "abfrgvar_weitrs_vrfhrn_id", referencedColumnName = "id")
    @OrderBy("abfragevariantenNr asc")
    private List<AbfragevarianteWeiteresVerfahren> abfragevariantenWeiteresVerfahren;

    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "abfrgvar_schbrbtng_weitrs_vrfhrn_id", referencedColumnName = "id")
    @OrderBy("abfragevariantenNr asc")
    private List<AbfragevarianteWeiteresVerfahren> abfragevariantenSachbearbeitungWeiteresVerfahren;
}
