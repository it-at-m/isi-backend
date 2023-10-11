/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(indexes = { @Index(name = "name_abfrage_index", columnList = "nameAbfrage") })
@Indexed
public class Infrastrukturabfrage extends BaseEntity {

    /**
     * Einheitlicher indexiertes sortierbares Namensattributs
     * zur einheitlichen entitätsübergreifenden Sortierung der Suchergebnisse.
     */
    @KeywordField(name = "name_sort", sortable = Sortable.YES, normalizer = "lowercase")
    @Transient
    @IndexingDependency(derivedFrom = @ObjectPath({ @PropertyValue(propertyName = "abfrage") }))
    public String getNameAbfrageSuche() {
        return abfrage.getNameAbfrage();
    }

    @IndexedEmbedded
    @Embedded
    public AbfrageAlt abfrage;

    @Column(nullable = false)
    private String sub;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) not null check (sobon_relevant != 'UNSPECIFIED')")
    private UncertainBoolean sobonRelevant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "abfrage_abfragevarianten_id", referencedColumnName = "id")
    @OrderBy("abfragevariantenNr asc")
    private List<Abfragevariante> abfragevarianten;

    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "abfrage_abfragevarianten_sachbearbeitung_id", referencedColumnName = "id")
    @OrderBy("abfragevariantenNr asc")
    private List<Abfragevariante> abfragevariantenSachbearbeitung;

    @Column(nullable = true)
    private String aktenzeichenProLbk;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) not null check (offizieller_Verfahrensschritt != 'UNSPECIFIED')")
    private UncertainBoolean offiziellerVerfahrensschritt;
}
