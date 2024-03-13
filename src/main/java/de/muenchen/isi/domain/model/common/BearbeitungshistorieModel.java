package de.muenchen.isi.domain.model.common;

import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import lombok.Data;

@Data
public class BearbeitungshistorieModel {

    private StatusAbfrage zielStatus;

    private BearbeitendePersonModel bearbeitendePerson;
}
