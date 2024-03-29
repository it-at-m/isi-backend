/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Foerdermix;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "bezeichnung", "bezeichnungJahr" }) })
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FoerdermixStamm extends BaseEntity {

    @Embedded
    private Foerdermix foerdermix;
}
