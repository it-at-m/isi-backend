/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import de.muenchen.isi.api.dto.BaseEntityDto;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MittelschuleDto extends BaseEntityDto {

    @Valid
    @NotNull
    private InfrastruktureinrichtungDto infrastruktureinrichtung;

    @Valid
    @NotNull
    private SchuleDto schule;
    // TBD: Mittelschulsprengel

}
