/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.BaseEntity;
import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtGsNachmittagBetreuung;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class GsNachmittagBetreuung extends BaseEntity {

    @Embedded
    public Infrastruktureinrichtung infrastruktureinrichtung;

    // TBD: Grundschulsprengel

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private ArtGsNachmittagBetreuung artGsNachmittagBetreuung;

    @Column(nullable = false)
    private Integer anzahlHortPlaetze;

    @Column(nullable = false)
    private Integer anzahlHortGruppen;

    private Integer wohnungsnaheHortPlaetze;

}
