/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import de.muenchen.isi.infrastructure.entity.enums.lookup.InfrastruktureinrichtungTyp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BedarfsmeldungFachabteilungenModel extends BaseEntityModel {

    private Long anzahlEinrichtungen;

    private InfrastruktureinrichtungTyp infrastruktureinrichtungTyp;

    private Long anzahlKinderkrippengruppen;

    private Long anzahlKindergartengruppen;

    private Long anzahlHortgruppen;

    private Long anzahlGrundschulzuege;
}
