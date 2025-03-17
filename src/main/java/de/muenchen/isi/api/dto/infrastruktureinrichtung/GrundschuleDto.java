/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.muenchen.isi.infrastructure.entity.enums.EntityType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GrundschuleDto extends InfrastruktureinrichtungDto {

    @JsonIgnore
    private EntityType entityType = EntityType.GRUNDSCHULE;

    @Valid
    @NotNull
    private SchuleDto schule;
    // TBD: Grundschulsprengel

}
