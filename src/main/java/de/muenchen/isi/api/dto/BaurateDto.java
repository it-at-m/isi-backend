/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.validation.HasFoerdermixRequiredSum;
import de.muenchen.isi.api.validation.JahrDistributionValid;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JahrDistributionValid
public class BaurateDto extends BaseEntityDto {

    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer jahr; // JJJJ

    private Integer anzahlWeGeplant;

    private BigDecimal geschossflaecheWohnenGeplant;

    @Valid
    @NotNull
    @HasFoerdermixRequiredSum
    private FoerdermixDto foerdermix;
}
