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
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = { @Index(name = "name_abfrage_index", columnList = "nameAbfrage") })
public class Infrastrukturabfrage extends BaseEntity {

    @Embedded
    public Abfrage abfrage;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) not null check (sobon_relevant != 'UNSPECIFIED')")
    private UncertainBoolean sobonRelevant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Abfragevariante> abfragevarianten;

    @Column(nullable = true)
    private String aktenzeichenProLbk;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) not null check (offizieller_Verfahrensschritt != 'UNSPECIFIED')")
    private UncertainBoolean offiziellerVerfahrensschritt;
}
