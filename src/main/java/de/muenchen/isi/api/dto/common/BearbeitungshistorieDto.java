package de.muenchen.isi.api.dto.common;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BearbeitungshistorieDto {

    private StatusAbfrage zielStatus;

    private LocalDateTime zeitpunkt;

    private BearbeitendePersonDto bearbeitendePerson;
}
