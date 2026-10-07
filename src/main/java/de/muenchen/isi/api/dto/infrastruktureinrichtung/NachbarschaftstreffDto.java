/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.api.dto.infrastruktureinrichtung;

import de.muenchen.isi.api.dto.filehandling.DokumentDto;
import de.muenchen.isi.api.validation.HasAllowedNumberOfDocuments;
import de.muenchen.isi.infrastructure.entity.enums.lookup.Kooperation;
import de.muenchen.isi.infrastructure.entity.enums.lookup.UncertainBoolean;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NachbarschaftstreffDto extends InfrastruktureinrichtungDto {

    private Kooperation kooperation;

    private String kooperationFreieEingabe;

    private UncertainBoolean sobonRelevant;

    @HasAllowedNumberOfDocuments
    private List<@Valid DokumentDto> dokumente;
}
