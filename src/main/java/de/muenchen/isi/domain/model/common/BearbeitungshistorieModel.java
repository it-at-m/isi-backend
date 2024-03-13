package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BearbeitungshistorieModel {

    private StatusAbfrage zielStatus;

    private LocalDateTime zeitpunkt;

    private BearbeitendePersonModel bearbeitendePerson;
}
