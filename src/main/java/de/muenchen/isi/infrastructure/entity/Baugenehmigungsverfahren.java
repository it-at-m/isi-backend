package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.adapter.search.StandVerfahrenSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.Wgs84;
import de.muenchen.isi.infrastructure.entity.enums.EntityType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
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
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;
import org.hibernate.type.SqlTypes;

@Entity
@DiscriminatorValue(ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Indexed
public class Baugenehmigungsverfahren extends Abfrage {

    @Enumerated(EnumType.STRING)
    @GenericField
    private EntityType entityType = EntityType.BAUGENEHMIGUNGSVERFAHREN;

    @Column
    private String aktenzeichenProLbk;

    @FullTextField
    @NonStandardField(
        name = "bebauungsplannummer" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column
    private String bebauungsplannummer;

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

    @IndexedEmbedded
    @Embedded
    private Adresse adresse;

    @IndexedEmbedded
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private VerortungMultiPolygon verortung;

    @Column
    @GenericField
    @AttributeOverrides(
        {
            @AttributeOverride(name = "latitude", column = @Column(name = "search_result_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "search_result_longitude")),
        }
    )
    private Wgs84 abfrageCoordinate;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "baugenehmigungsverfahren_id")
    private List<Dokument> dokumente;

    @Column(nullable = false)
    @GenericField
    private LocalDate fristBearbeitung;

    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "abfrgvar_baugnhmgsverfhrn_id", referencedColumnName = "id")
    @OrderBy("abfragevariantenNr asc")
    private List<AbfragevarianteBaugenehmigungsverfahren> abfragevariantenBaugenehmigungsverfahren;

    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "abfrgvar_schbrbtng_baugnhmgsverfhrn_id", referencedColumnName = "id")
    @OrderBy("abfragevariantenNr asc")
    private List<AbfragevarianteBaugenehmigungsverfahren> abfragevariantenSachbearbeitungBaugenehmigungsverfahren;
}
