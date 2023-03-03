/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
public class FoerdermixDto {

    @Min(0)
    private BigDecimal anteilFreifinanzierterGeschosswohnungsbau;

    @Min(0)
    private BigDecimal anteilGefoerderterMietwohnungsbau;

    @Min(0)
    private BigDecimal anteilMuenchenModell;

    @Min(0)
    private BigDecimal anteilPreisgedaempfterMietwohnungsbau;

    @Min(0)
    private BigDecimal anteilKonzeptionellerMietwohnungsbau;

    @Min(0)
    private BigDecimal anteilBaugemeinschaften;

    @Min(0)
    private BigDecimal anteilEinUndZweifamilienhaeuser;

}
