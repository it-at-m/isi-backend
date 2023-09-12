package de.muenchen.isi.api.dto.list;

import de.muenchen.isi.api.dto.common.StadtbezirkDto;
import de.muenchen.isi.domain.model.enums.AbfrageTyp;
import de.muenchen.isi.infrastructure.entity.enums.lookup.SobonVerfahrensgrundsaetzeJahr;
import de.muenchen.isi.infrastructure.entity.enums.lookup.StatusAbfrage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AbfrageListElementDto {

    private UUID id;

    @Size(max = 70, message = "Es sind maximal {max} Zeichen erlaubt")
    private String nameAbfrage;

    private Set<StadtbezirkDto> stadtbezirke;

    private StatusAbfrage statusAbfrage;

    private LocalDate fristStellungnahme;

    private AbfrageTyp type;

    private SobonVerfahrensgrundsaetzeJahr sobonJahr;

    private LocalDateTime createdDateTime;

    private UUID bauvorhaben;
}
