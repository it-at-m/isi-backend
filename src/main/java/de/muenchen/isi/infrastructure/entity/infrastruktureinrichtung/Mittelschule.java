/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Mittelschule extends BaseEntity {

    @Embedded
    public Infrastruktureinrichtung infrastruktureinrichtung;

    @Embedded
    public Schule schule;
    // TBD: Mittelschulsprengel

}
