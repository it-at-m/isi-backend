/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@DiscriminatorValue(InfrastruktureinrichtungTyp.Values.MITTELSCHULE)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Mittelschule extends Infrastruktureinrichtung {

    // TBD: Mittelschulsprengel

    @Embedded
    public Schule schule;
}
