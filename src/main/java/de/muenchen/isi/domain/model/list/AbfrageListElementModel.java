package de.muenchen.isi.domain.model.list;

import de.muenchen.isi.domain.model.common.StadtbezirkModel;
import de.muenchen.isi.domain.model.enums.AbfrageTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class AbfrageListElementModel {

    private UUID id;

    private String nameAbfrage;

    private Set<StadtbezirkModel> stadtbezirke;

    private StatusAbfrage statusAbfrage;

    private LocalDate fristStellungnahme;

    private AbfrageTyp type;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;
}
