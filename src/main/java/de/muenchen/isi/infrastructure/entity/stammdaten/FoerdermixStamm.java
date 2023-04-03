/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.stammdaten;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.Foerdermix;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"bezeichnung", "bezeichnungJahr"})})
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FoerdermixStamm extends BaseEntity {

    @Column(nullable = false)
    private String bezeichnungJahr;

    @Column(length = 80, nullable = false)
    private String bezeichnung;

    @OneToOne(cascade = CascadeType.ALL)
    private Foerdermix foerdermix;
}
