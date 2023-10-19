/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.ArtGsNachmittagBetreuung;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@DiscriminatorValue(InfrastruktureinrichtungTyp.Values.GS_NACHMITTAG_BETREUUNG)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Indexed
public class GsNachmittagBetreuung extends Infrastruktureinrichtung {

    // TBD: Grundschulsprengel

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private ArtGsNachmittagBetreuung artGsNachmittagBetreuung;

    @Column(nullable = false)
    private Integer anzahlHortPlaetze;

    @Column(nullable = false)
    private Integer anzahlHortGruppen;

    private Integer wohnungsnaheHortPlaetze;

    @Enumerated(EnumType.STRING)
    @Column
    private Einrichtungstraeger einrichtungstraeger;
}
