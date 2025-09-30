/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.adapter.listener.InfrastruktureinrichtungListener;
import de.muenchen.isi.infrastructure.adapter.search.AdresseValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.InfrastruktureinrichtungTypValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.ResultTypeValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StatusInfrastruktureinrichtungSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.StatusInfrastruktureinrichtungValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.VerortungMultiPolygonValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.VerortungPointValueBridge;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.BearbeitendePerson;
import de.muenchen.isi.infrastructure.entity.common.VerortungMultiPolygon;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ResultType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
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
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;
import org.hibernate.type.SqlTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@EntityListeners({ InfrastruktureinrichtungListener.class })
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "infrastruktureinrichtungTyp")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Infrastruktureinrichtung extends BaseEntity {

    private static final Logger log = LoggerFactory.getLogger(Infrastruktureinrichtung.class);

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
     * @return Der feste String "INFRASTRUKTUREINRICHTUNG", der diesen Objekttyp im Index markiert.
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
        return ResultType.INFRASTRUKTUREINRICHTUNG;
    }

    @Transient
    @GenericField(name = "bauvorhabenName", projectable = Projectable.YES)
    @IndexingDependency(derivedFrom = @ObjectPath(@PropertyValue(propertyName = "bauvorhaben")))
    public String getBauvorhabenName() {
        return bauvorhaben != null ? bauvorhaben.getNameVorhaben() : null;
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
     * Diese Methode gibt den Wert der {@link DiscriminatorColumn} zurück.
     * Ist kein {@link DiscriminatorValue} gesetzt, so wird null zurückgegeben.
     *
     * @return Wert der {@link DiscriminatorColumn}.
     */
    @Transient
    @KeywordField(
        name = "infrastruktureinrichtungTyp",
        projectable = Projectable.YES,
        searchable = Searchable.NO,
        valueBridge = @ValueBridgeRef(type = InfrastruktureinrichtungTypValueBridge.class)
    )
    @IndexingDependency(
        reindexOnUpdate = ReindexOnUpdate.NO,
        extraction = @ContainerExtraction(extract = ContainerExtract.NO)
    )
    public InfrastruktureinrichtungTyp getInfrastruktureinrichtungTyp() {
        final var discriminatorValue = this.getClass().getAnnotation(DiscriminatorValue.class);
        return ObjectUtils.isEmpty(discriminatorValue)
            ? null
            : EnumUtils.getEnum(InfrastruktureinrichtungTyp.class, discriminatorValue.value());
    }

    @Generated(event = EventType.INSERT)
    @Column(name = "lfdNr", columnDefinition = "serial", updatable = false)
    private Long lfdNr;

    @ManyToOne
    private Bauvorhaben bauvorhaben;

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
    private VerortungPoint verortung;

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
        name = "verortungPointJson",
        projectable = Projectable.YES,
        searchable = Searchable.NO,
        valueBridge = @ValueBridgeRef(type = VerortungPointValueBridge.class)
    )
    @IndexingDependency(derivedFrom = { @ObjectPath(@PropertyValue(propertyName = "verortung")) })
    public VerortungPoint getVerortungPointJson() {
        return this.verortung;
    }

    /**
     * Einheitlicher indexiertes sortierbares Namensattributs
     * zur einheitlichen entitätsübergreifenden Sortierung der Suchergebnisse.
     */
    @KeywordField(name = "name_sort", sortable = Sortable.YES, normalizer = "lowercase")
    @FullTextField
    @NonStandardField(
        name = "nameEinrichtung" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StringSuggestionBinder.class)
    )
    @Column(nullable = false)
    private String nameEinrichtung;

    @Column
    private Integer fertigstellungsjahr; // JJJJ

    @FullTextField(valueBridge = @ValueBridgeRef(type = StatusInfrastruktureinrichtungValueBridge.class))
    @NonStandardField(
        name = "status" + SearchwordSuggesterRepository.ATTRIBUTE_SUFFIX_SEARCHWORD_SUGGESTION,
        valueBinder = @ValueBinderRef(type = StatusInfrastruktureinrichtungSuggestionBinder.class)
    )
    @GenericField(name = "status_infrastruktureinrichtung_filter")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusInfrastruktureinrichtung status;

    @Column(precision = 10, scale = 2)
    private BigDecimal flaecheGesamtgrundstueck;

    @Column(precision = 10, scale = 2)
    private BigDecimal flaecheTeilgrundstueck;

    @Column(length = 255)
    private String idKibigWeb;
}
