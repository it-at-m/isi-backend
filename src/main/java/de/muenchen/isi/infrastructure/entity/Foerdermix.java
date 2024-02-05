/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OrderBy;
import java.util.List;
import lombok.Data;

@Data
@Embeddable
public class Foerdermix {

    @Column(nullable = false)
    private String bezeichnungJahr;

    @Column(length = 80, nullable = false)
    private String bezeichnung;

    @ElementCollection
    @OrderBy("bezeichnung asc")
    private List<Foerderart> foerderarten;
}
