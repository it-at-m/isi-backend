/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoerdermixModel {

    private BigDecimal anteilFreifinanzierterGeschosswohnungsbau;

    private BigDecimal anteilGefoerderterMietwohnungsbau;

    private BigDecimal anteilMuenchenModell;

    private BigDecimal anteilPreisgedaempfterMietwohnungsbau;

    private BigDecimal anteilKonzeptionellerMietwohnungsbau;

    private BigDecimal anteilBaugemeinschaften;

    private BigDecimal anteilEinUndZweifamilienhaeuser;

}
