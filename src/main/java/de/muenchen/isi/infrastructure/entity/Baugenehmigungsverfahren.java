package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.adapter.search.StandVerfahrenSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.StandVerfahrenValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.Verortung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtAbfrage;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVerfahren;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;

@Entity
@DiscriminatorValue(ArtAbfrage.Values.BAUGENEHMIGUNGSVERFAHREN)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Indexed
public class Baugenehmigungsverfahren extends Abfrage {

    @Column
    private String aktenzeichenProLbk;

    @FullTextField
    @NonStandardField(
        name = "bebauungsplannummer" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column(nullable = true)
    private String bebauungsplannummer;

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
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Verortung verortung;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "baugenehmigungsverfahren_id")
    private List<Dokument> dokumente;

    @Column(nullable = false)
    private LocalDate fristBearbeitung;

    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "baugenehmigungsverfahren_abfragevarianten_id", referencedColumnName = "id")
    @OrderBy("abfragevariantenNr asc")
    private List<AbfragevarianteBaugenehmigungsverfahren> abfragevarianten;

    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "baugenehmigungsverfahren_abfragevarianten_sachbearbeitung_id", referencedColumnName = "id")
    @OrderBy("abfragevariantenNr asc")
    private List<AbfragevarianteBaugenehmigungsverfahren> abfragevariantenSachbearbeitung;
}
