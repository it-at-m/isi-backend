/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.EntityType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GrundschuleModel extends InfrastruktureinrichtungModel {

    // TBD: Grundschulsprengel
    private EntityType entityType = EntityType.GRUNDSCHULE;

    public SchuleModel schule;
}
