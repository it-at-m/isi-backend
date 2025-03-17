/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.EntityType;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@DiscriminatorValue(InfrastruktureinrichtungTyp.Values.GRUNDSCHULE)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Indexed
public class Grundschule extends Infrastruktureinrichtung {

    @Enumerated(EnumType.STRING)
    private EntityType entityType = EntityType.GRUNDSCHULE;

    // TBD: Grundschulsprengel

    @Embedded
    public Schule schule;
}
