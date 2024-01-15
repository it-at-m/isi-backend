/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.adapter.search.StatusInfrastruktureinrichtungSuggestionBinder;
import de.muenchen.isi.infrastructure.adapter.search.StatusInfrastruktureinrichtungValueBridge;
import de.muenchen.isi.infrastructure.adapter.search.StringSuggestionBinder;
import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.common.VerortungPoint;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import de.muenchen.isi.infrastructure.repository.search.SearchwordSuggesterRepository;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBinderRef;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.NonStandardField;
import org.hibernate.type.SqlTypes;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "infrastruktureinrichtungTyp")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Infrastruktureinrichtung extends BaseEntity {

    /**
     * Diese Methode gibt den Wert der {@link DiscriminatorColumn} zurück.
     * Ist kein {@link DiscriminatorValue} gesetzt, so wird null zurückgegeben.
     *
     * @return Wert der {@link DiscriminatorColumn}.
     */
    @Transient
    public InfrastruktureinrichtungTyp getInfrastruktureinrichtungTyp() {
        final var discriminatorValue = this.getClass().getAnnotation(DiscriminatorValue.class);
        return ObjectUtils.isEmpty(discriminatorValue)
            ? null
            : EnumUtils.getEnum(InfrastruktureinrichtungTyp.class, discriminatorValue.value());
    }

    @Generated(GenerationTime.INSERT)
    @Column(name = "lfdNr", columnDefinition = "serial", updatable = false)
    private Long lfdNr;

    @ManyToOne
    private Bauvorhaben bauvorhaben;

    @IndexedEmbedded
    @Embedded
    private Adresse adresse;

    @IndexedEmbedded
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private VerortungPoint verortung;

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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusInfrastruktureinrichtung status;

    @Column(precision = 10, scale = 2)
    private BigDecimal flaecheGesamtgrundstueck;

    @Column(precision = 10, scale = 2)
    private BigDecimal flaecheTeilgrundstueck;
}
