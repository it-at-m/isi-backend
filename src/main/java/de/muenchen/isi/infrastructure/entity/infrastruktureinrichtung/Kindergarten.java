/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Kindergarten extends BaseEntity {

    @Embedded
    public Infrastruktureinrichtung infrastruktureinrichtung;

    @Column(nullable = false)
    private Integer anzahlKindergartenPlaetze;

    @Column(nullable = false)
    private Integer anzahlKindergartenGruppen;

    @Column(nullable = true)
    private Integer wohnungsnaheKindergartenPlaetze;
}
