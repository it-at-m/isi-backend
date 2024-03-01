/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto;

import de.muenchen.isi.api.validation.FoerdermixNotEmptyValid;
import de.muenchen.isi.api.validation.HasFoerdermixRequiredSum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaurateDto extends BaseEntityDto {

    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer jahr; // JJJJ

    private Integer weGeplant;

    private BigDecimal gfWohnenGeplant;

    @Valid
    @NotNull
    @FoerdermixNotEmptyValid
    @HasFoerdermixRequiredSum
    private FoerdermixDto foerdermix;
}
