/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaurateModel extends BaseEntityModel {

    private Integer jahr; // JJJJ

    private Integer anzahlWeGeplant;

    private BigDecimal geschossflaecheWohnenGeplant;

    private FoerdermixModel foerdermix;
}
