/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Baugebiet;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "infrastruktureinrichtung_typ")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Infrastruktureinrichtung extends BaseEntity {

    @Setter(AccessLevel.NONE)
    @Column(insertable = false, updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    @Generated(GenerationTime.INSERT)
    @Column(name = "lfdNr", columnDefinition = "serial", updatable = false)
    private Long lfdNr;

    @ManyToOne
    private Bauvorhaben bauvorhaben;

    @Column(nullable = true)
    private String allgemeineOrtsangabe;

    @Embedded
    private Adresse adresse;

    @Column(nullable = false)
    private String nameEinrichtung;

    @Column
    private Integer fertigstellungsjahr; // JJJJ

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusInfrastruktureinrichtung status;

    @Enumerated(EnumType.STRING)
    @Column
    private Einrichtungstraeger einrichtungstraeger;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal flaecheGesamtgrundstueck;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal flaecheTeilgrundstueck;

    @OneToOne
    private Baugebiet zugeordnetesBaugebiet;
}
