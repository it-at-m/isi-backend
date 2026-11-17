/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.infrastructure.entity.infrastruktureinrichtung;

import de.muenchen.isi.infrastructure.entity.enums.lookup.Einrichtungstraeger;
import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Entity
@DiscriminatorValue(InfrastruktureinrichtungTyp.Values.KINDERKRIPPE)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Indexed
public class Kinderkrippe extends Infrastruktureinrichtung {

    @Enumerated(EnumType.STRING)
    @Column
    private Einrichtungstraeger einrichtungstraeger;

    @Column
    private Integer fertigstellungsmonat; // MM

    @GenericField(name = "sobon_relevant_filter")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    private UncertainBoolean sobonRelevant;

    @GenericField(name = "muenchner_kitafoerderung_filter")
    @Column
    private Boolean muenchnerKitafoerderung;

    @GenericField(name = "mini_kita")
    @Column
    private Boolean miniKita;

    @GenericField(name = "integrationseinrichtung")
    @Column
    private Boolean integrationseinrichtung;

    @Column
    private String nameTraeger;

    @Column(name = "gesamtplaetze_nach_be")
    private Integer gesamtplaetzeNachBE;

    @Column(name = "kinderkrippenplaetze_nach_be")
    private Integer kinderkrippenplaetzeNachBE;

    @Column
    private Integer ueberregionaleKinderkrippenplaetze;

    @Column
    private Integer belegungKinderkrippenplaetze;

    @Column
    private Integer sollBelegungKinderkrippenplaetzeKePlus;

    @Column
    private Integer sollBelegungKinderkrippenplaetze;

    @Column
    private Integer integrationsplaetzeKinderkrippeKePlus;

    @Column
    private Integer integrationsplaetzeKinderkrippe;

    @Column
    private Integer belegungIntegrationsplaetzeKinderkrippe;

    @Column(name = "link_ke_plus")
    private String linkKePlus;
}
