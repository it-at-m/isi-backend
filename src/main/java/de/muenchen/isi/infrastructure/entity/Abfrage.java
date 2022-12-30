/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import de.muenchen.isi.infrastructure.entity.common.Adresse;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import de.muenchen.isi.infrastructure.entity.filehandling.Dokument;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Embeddable
@Data
public class Abfrage {

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Dokument> dokumente;

    @Column(nullable = true)
    private String allgemeineOrtsangabe;

    @Embedded
    private Adresse adresse;

    @Column(nullable = false)
    private LocalDate fristStellungnahme;

    @Column(nullable = true)
    private String anmerkung;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAbfrage statusAbfrage;

    @Column(nullable = true)
    private String bebauungsplannummer;

    @Column(nullable = false)
    private String nameAbfrage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StandVorhaben standVorhaben;

    @ManyToOne
    private Bauvorhaben bauvorhaben;

}
