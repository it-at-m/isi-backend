/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.common;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Adresse implements Cloneable, Serializable {

    @Column(nullable = true)
    private String strasse;

    @Column(nullable = true)
    private String hausnummer;

    @Column(nullable = true)
    private String plz;

    @Column(nullable = true)
    private String ort;

    @Embedded
    private Wgs84 coordinate;
}
