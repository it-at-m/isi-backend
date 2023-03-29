/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import javax.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class Schule {

    @Column(nullable = false)
    private Integer anzahlKlassen;

    @Column(nullable = false)
    private Integer anzahlPlaetze;
}
