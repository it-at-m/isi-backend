package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import lombok.Data;

@Data
public class BearbeitungshistorieDto {

    private StatusAbfrage zielStatus;

    private BearbeitendePersonDto bearbeitendePerson;
}
