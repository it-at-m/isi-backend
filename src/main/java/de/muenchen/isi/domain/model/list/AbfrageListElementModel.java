package de.muenchen.isi.domain.model.list;

import de.muenchen.isi.domain.model.enums.AbfrageTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StandVorhaben;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AbfrageListElementModel {

    private UUID id;

    private String nameAbfrage;

    private StandVorhaben standVorhaben;

    private StatusAbfrage statusAbfrage;

    private LocalDate fristStellungnahme;

    private AbfrageTyp type;

}
