/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@DiscriminatorValue(InfrastruktureinrichtungTyp.Values.HAUS_FUER_KINDER)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Indexed
public class HausFuerKinder extends Infrastruktureinrichtung {

    @Column
    private Integer anzahlKinderkrippePlaetze;

    @Column
    private Integer anzahlKindergartenPlaetze;

    @Column
    private Integer anzahlHortPlaetze;

    @Column
    private Integer anzahlKinderkrippeGruppen;

    @Column
    private Integer anzahlKindergartenGruppen;

    @Column
    private Integer anzahlHortGruppen;

    @Column
    private Integer wohnungsnaheKinderkrippePlaetze;

    @Column
    private Integer wohnungsnaheKindergartenPlaetze;

    @Column
    private Integer wohnungsnaheHortPlaetze;

    @Enumerated(EnumType.STRING)
    @Column
    private Einrichtungstraeger einrichtungstraeger;
}
