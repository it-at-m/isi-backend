/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BedarfsmeldungFachabteilungenModel extends BaseEntityModel {

    private Long anzahlEinrichtungen;

    private Long anzahlKinderkrippengruppen;

    private Long anzahlKindergartengruppen;

    private Long anzahlHortgruppen;

    private Long anzahlGrundschulzuege;
}
