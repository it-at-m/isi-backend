/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.stammdaten;

import de.muenchen.isi.api.dto.BaseEntityDto;
import de.muenchen.isi.api.dto.FoerdermixDto;
import de.muenchen.isi.api.validation.HasFoerdermixRequiredSum;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
