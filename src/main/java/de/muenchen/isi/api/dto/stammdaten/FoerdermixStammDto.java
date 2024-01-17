/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.stammdaten;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.api.validation.HasFoerdermixRequiredSum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FoerdermixStammDto extends BaseEntityDto {

    @NotNull
    @Valid
    @HasFoerdermixRequiredSum
    private FoerdermixDto foerdermix;
}
