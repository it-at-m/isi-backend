/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.Baugebiet;
import de.muenchen.isi.infrastructure.entity.Bauvorhaben;
import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusInfrastruktureinrichtung;
import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.math.BigDecimal;

@Embeddable
@Data
public class Infrastruktureinrichtung {

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

    @Column(nullable = false)
    private Integer fertigstellungsjahr; // JJJJ

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusInfrastruktureinrichtung status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Einrichtungstraeger einrichtungstraeger;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal flaecheGesamtgrundstueck;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal flaecheTeilgrundstueck;

    @OneToOne
    private Baugebiet zugeordnetesBaugebiet;

}
