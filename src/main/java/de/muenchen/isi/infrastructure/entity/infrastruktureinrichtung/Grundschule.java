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
@DiscriminatorValue(InfrastruktureinrichtungTyp.Values.GRUNDSCHULE)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Grundschule extends Infrastruktureinrichtung {

    // TBD: Grundschulsprengel

    @Embedded
    public Schule schule;
}
