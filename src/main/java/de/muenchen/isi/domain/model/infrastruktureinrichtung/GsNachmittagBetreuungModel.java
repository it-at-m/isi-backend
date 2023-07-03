/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtGsNachmittagBetreuung;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@DiscriminatorValue("GsNachmittagBetreuung")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GsNachmittagBetreuungModel extends InfrastruktureinrichtungModel {

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
