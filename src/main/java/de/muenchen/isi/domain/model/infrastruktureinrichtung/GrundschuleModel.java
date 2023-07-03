/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@DiscriminatorValue("Grundschule")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GrundschuleModel extends InfrastruktureinrichtungModel {

    // TBD: Grundschulsprengel

    @Embedded
    public SchuleModel schule;
}
